/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.codehaus.groovy.grails.compiler.injection;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.*;

/**
 * This is substantially the same code from Grails, except some references de-referenced
 * and the macro class added.
 *
 * Default implementation of domain class injector interface that adds the 'id'
 * and 'version' properties and other previously boilerplate code
 *
 * @author Graeme Rocher
 *
 * @since 0.2
 *
 * Created: 20th June 2006
 */
@GroovyASTTransformation(phase= CompilePhase.CANONICALIZATION)
public class DefaultGrailsDomainClassInjector implements ASTTransformation {
		private static final Logger log = Logger.getLogger(DefaultGrailsDomainClassInjector.class.getName());
        //GrailsDomainClassInjector {

    //private static final Log LOG  = LogFactory.getLog(DefaultGrailsDomainClassInjector.class);


    public void visit(ASTNode[] nodes, SourceUnit source) {
		log.debug(">>> Entering visit(nodes={},source={})", nodes, source);
        performInjection((ClassNode) nodes[1]);
		log.debug("<<< Exiting visit(nodes={},source={})", nodes, source);
    }

    public void performInjection(ClassNode classNode) {
		log.debug(">>> Entering performInjection(classNode=<redacted>)");
        if(shouldInject(classNode)) {
            injectIdProperty(classNode);

            injectVersionProperty(classNode);

            injectToStringMethod(classNode);

            injectAssociations(classNode);

        }
		log.debug("<<< Exiting performInjection(classNode=<redacted>)");
    }

    public boolean shouldInject(URL url) {
		log.debug(">>> Entering shouldInject(url={})", url);
        return true; //return GrailsResourceUtils.isDomainClass(url);
    }

    private boolean shouldInject(ClassNode classNode) {
        //String fullName = GrailsASTUtils.getFullName(classNode);
        //String mappingFile = GrailsDomainConfigurationUtil.getMappingFileName(fullName);

        //if(getClass().getResource(mappingFile)!=null) {
            //if(LOG.isDebugEnabled()) {
                //LOG.debug("[GrailsDomainInjector] Mapping file ["+mappingFile+"] found. Skipping property injection.");
            //}
            //return false;
        //}
        return true;
    }

    private void injectAssociations(ClassNode classNode) {
	log.debug(">>> Entering injectAssociations(classNode=<redacted>)");

        List properties = classNode.getProperties();
		log.debug("injectAssociations(classNode=<redacted>): properties → {}", properties);
        List propertiesToAdd = new ArrayList();
		log.debug("injectAssociations(classNode=<redacted>): propertiesToAdd → {}", propertiesToAdd);
        for (Iterator p = properties.iterator(); p.hasNext();) {
			log.debug("injectAssociations(classNode=<redacted>): p → {}", p);
            PropertyNode pn = (PropertyNode) p.next();
			log.debug("injectAssociations(classNode=<redacted>): pn → {}", pn);
            final boolean isHasManyProperty = pn.getName().equals(/*GrailsDomainClassProperty.*/RELATES_TO_MANY) || pn.getName().equals(/*GrailsDomainClassProperty.*/HAS_MANY);
			log.debug("injectAssociations(classNode=<redacted>): isHasManyProperty → {}", isHasManyProperty);
            if(isHasManyProperty) {
                Expression e = pn.getInitialExpression();
				log.debug("injectAssociations(classNode=<redacted>): e → {}", e);
                propertiesToAdd.addAll(createPropertiesForHasManyExpression(e,classNode));
            }
            final boolean isBelongsTo = pn.getName().equals(/*GrailsDomainClassProperty.*/BELONGS_TO);
			log.debug("injectAssociations(classNode=<redacted>): isBelongsTo → {}", isBelongsTo);
            if(isBelongsTo) {
                Expression e = pn.getInitialExpression();
                propertiesToAdd.addAll(createPropertiesForBelongsToExpression(e,classNode));
            }
        }
        injectAssociationProperties(classNode,propertiesToAdd);
		log.debug("<<< Exiting injectAssociations(classNode=<redacted>)");
    }

    private Collection createPropertiesForBelongsToExpression(Expression e, ClassNode classNode)
    {
		log.debug(">>> Entering createPropertiesForBelongsToExpression(e={},classNode=<redacted>)", e);
        List properties = new ArrayList();
        if(e instanceof MapExpression) {
			log.debug("createPropertiesForBelongsToExpression(e={},classNode=<redacted>): properties → {}", e, properties);
            MapExpression me = (MapExpression)e;
			log.debug("createPropertiesForBelongsToExpression(e={},classNode=<redacted>): me → {}", e, me);
            List mapEntries = me.getMapEntryExpressions();
			log.debug("createPropertiesForBelongsToExpression(e={},classNode=<redacted>): mapEntries → {}", e, mapEntries);
            for (Iterator i = mapEntries.iterator(); i.hasNext();) {
                MapEntryExpression mme = (MapEntryExpression) i.next();
				log.debug("createPropertiesForBelongsToExpression(e={},classNode=<redacted>): mme → {}", e, mme);
                String key = mme.getKeyExpression().getText();
				log.warn("createPropertiesForBelongsToExpression(e={},classNode=<redacted>): key → {}", e, key);

                String type = mme.getValueExpression().getText();
				log.debug("createPropertiesForBelongsToExpression(e={},classNode=<redacted>): type → {}", e, type);

                properties.add(new PropertyNode(key,Modifier.PUBLIC, ClassHelper.make(type) , classNode, null,null,null));
            }
        }

		log.debug("<<< Exiting createPropertiesForBelongsToExpression(e={},classNode=<redacted>)", e);
        return properties;
    }

    private void injectAssociationProperties(ClassNode classNode, List propertiesToAdd) {
		log.debug(">>> Entering injectAssociationProperties(classNode=<redacted>,propertiesToAdd={})", propertiesToAdd);
        for (Iterator i = propertiesToAdd.iterator(); i.hasNext();) {
            PropertyNode pn = (PropertyNode) i.next();
            if(!/*GrailsASTUtils.*/hasProperty(classNode, pn.getName())) {
                //if(LOG.isDebugEnabled()) {
                //    LOG.debug("[GrailsDomainInjector] Adding property [" + pn.getName() + "] to class [" + classNode.getName() + "]");
                //}
                classNode.addProperty(pn);
            }
        }
		log.debug("<<< Exiting injectAssociationProperties(classNode=<redacted>,propertiesToAdd={})", propertiesToAdd);
    }

    private List createPropertiesForHasManyExpression(Expression e, ClassNode classNode) {
		log.debug(">>> Entering createPropertiesForHasManyExpression(e={},classNode=<redacted>)", e);
        List properties = new ArrayList();
        if(e instanceof MapExpression) {
			log.debug("createPropertiesForHasManyExpression(e={},classNode=<redacted>): properties → {}", e, properties);
            MapExpression me = (MapExpression)e;
			log.debug("createPropertiesForHasManyExpression(e={},classNode=<redacted>): me → {}", e, me);
            List mapEntries = me.getMapEntryExpressions();
			log.debug("createPropertiesForHasManyExpression(e={},classNode=<redacted>): mapEntries → {}", e, mapEntries);
            for (Iterator j = mapEntries.iterator(); j.hasNext();) {
                MapEntryExpression mee = (MapEntryExpression) j.next();
				log.debug("createPropertiesForHasManyExpression(e={},classNode=<redacted>): mee → {}", e, mee);
                Expression keyExpression = mee.getKeyExpression();
				log.debug("createPropertiesForHasManyExpression(e={},classNode=<redacted>): keyExpression → {}", e, keyExpression);
                String key = keyExpression.getText();
				log.warn("createPropertiesForHasManyExpression(e={},classNode=<redacted>): key → {}", e, key);
                addAssociationForKey(key,properties,classNode);
            }
        }
		log.debug("<<< Exiting createPropertiesForHasManyExpression(e={},classNode=<redacted>)", e);
        return properties;
    }

    private void addAssociationForKey(String key, List properties, ClassNode classNode) {
			log.debug(">>> Entering addAssociationForKey(key={},properties={},classNode=<redacted>)", key, properties);
            properties.add(new PropertyNode(key, Modifier.PUBLIC, new ClassNode(Set.class), classNode,null, null, null));
			log.debug("<<< Exiting addAssociationForKey(key={},properties={},classNode=<redacted>)", key, properties);
    }

    private void injectToStringMethod(ClassNode classNode) {
		log.debug(">>> Entering injectToStringMethod(classNode=<redacted>)");
        final boolean hasToString = /*GrailsASTUtils.*/implementsZeroArgMethod(classNode, "toString");

        if(!hasToString) {
            GStringExpression ge = new GStringExpression(classNode.getName() + " : ${id}");
            ge.addString(new ConstantExpression(classNode.getName()+" : "));
            ge.addValue(new VariableExpression("id"));
            Statement s = new ReturnStatement(ge);
            MethodNode mn = new MethodNode("toString",Modifier.PUBLIC,new ClassNode(String.class), new Parameter[0],new ClassNode[0],s);
            //if(LOG.isDebugEnabled()) {
            //    LOG.debug("[GrailsDomainInjector] Adding method [toString()] to class [" + classNode.getName() + "]");
            //}
            classNode.addMethod(mn);
        }
		log.debug("<<< Exiting injectToStringMethod(classNode=<redacted>)");
    }

    private void injectVersionProperty(ClassNode classNode) {
		log.debug(">>> Entering injectVersionProperty(classNode=<redacted>)");
        final boolean hasVersion = /*GrailsASTUtils.*/hasProperty(classNode, /*GrailsDomainClassProperty.*/VERSION);

        if(!hasVersion) {
            //if(LOG.isDebugEnabled()) {
            //    LOG.debug("[GrailsDomainInjector] Adding property [" + GrailsDomainClassProperty.VERSION + "] to class [" + classNode.getName() + "]");
            //}
            classNode.addProperty( /*GrailsDomainClassProperty.*/VERSION, Modifier.PUBLIC, new ClassNode(Long.class), null, null, null);
        }
		log.debug("<<< Exiting injectVersionProperty(classNode=<redacted>)");
    }

    private void injectIdProperty(ClassNode classNode) {
		log.debug(">>> Entering injectIdProperty(classNode=<redacted>)");
        final boolean hasId = /*GrailsASTUtils.*/hasProperty(classNode,/*GrailsDomainClassProperty.*/IDENTITY);

        if(!hasId) {
            //if(LOG.isDebugEnabled()) {
            //    LOG.debug("[GrailsDomainInjector] Adding property [" + GrailsDomainClassProperty.IDENTITY + "] to class [" + classNode.getName() + "]");
            //}
            classNode.addProperty( /*GrailsDomainClassProperty.*/IDENTITY, Modifier.PUBLIC, new ClassNode(Long.class), null, null, null);
        }
		log.debug("<<< Exiting injectIdProperty(classNode=<redacted>)");
    }


    //***************************************************************
    // from GrailsASTUtils
    //***************************************************************
    /**
     * Returns whether a classNode has the specified property or not
     *
     * @param classNode The ClassNode
     * @param propertyName The name of the property
     * @return True if the property exists in the ClassNode
     */
    public static boolean hasProperty(ClassNode classNode, String propertyName) {
		log.debug(">>> Entering hasProperty(classNode=<redacted>,propertyName={})", propertyName);
        if(classNode == null || propertyName == null || "".equals(propertyName.trim()))
            return false;

        List properties = classNode.getProperties();
		log.debug("hasProperty(classNode=<redacted>,propertyName={}): properties → {}", propertyName, properties);
        for (Iterator i = properties.iterator(); i.hasNext();) {
            PropertyNode pn = (PropertyNode) i.next();
			log.debug("hasProperty(classNode=<redacted>,propertyName={}): pn → {}", propertyName, pn);
		log.debug("<<< Exiting hasProperty(classNode=<redacted>,propertyName={})", propertyName);
            if(pn.getName().equals(propertyName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests whether the ClasNode implements the specified method name
     *
     * @param classNode The ClassNode
     * @param methodName The method name
     * @return True if it does implement the method
     */
    public static boolean implementsZeroArgMethod(ClassNode classNode, String methodName) {
		log.debug(">>> Entering implementsZeroArgMethod(classNode=<redacted>,methodName={})", methodName);
		log.debug("<<< Exiting implementsZeroArgMethod(classNode=<redacted>,methodName={})", methodName);
        return implementsMethod(classNode, methodName, new Class[0]);
    }


    /**
     * Tests whether the ClassNode implements the specified method name
     *
     * @param classNode The ClassNode
     * @param methodName The method name
     * @param argTypes
     * @return True if it implements the method
     */
    private static boolean implementsMethod(ClassNode classNode, String methodName, Class[] argTypes) {
		log.debug(">>> Entering implementsMethod(classNode=<redacted>,methodName={},argTypes={})", methodName, argTypes);
        List methods = classNode.getMethods();
        if (argTypes == null || argTypes.length ==0) {
			log.debug("implementsMethod(classNode=<redacted>,methodName={},argTypes={}): methods → {}", methodName, argTypes, methods);
            for (Iterator i = methods.iterator(); i.hasNext();) {
                MethodNode mn = (MethodNode) i.next();
				log.debug("implementsMethod(classNode=<redacted>,methodName={},argTypes={}): mn → {}", methodName, argTypes, mn);
                boolean methodMatch = mn.getName().equals(methodName);
				log.debug("implementsMethod(classNode=<redacted>,methodName={},argTypes={}): methodMatch → {}", methodName, argTypes, methodMatch);
                if(methodMatch)return true;
                // TODO Implement further parameter analysis
            }
        }
		log.debug("<<< Exiting implementsMethod(classNode=<redacted>,methodName={},argTypes={})", methodName, argTypes);
        return false;
    }

    //***************************************************************
    // from GrailsDomainClassProperty
    //***************************************************************
    private static final String RELATES_TO_MANY = "relatesToMany";
    private static final String BELONGS_TO = "belongsTo";
    private static final String HAS_MANY = "hasMany";
    private static final String IDENTITY = "id";
    private static final String VERSION = "version";
}
