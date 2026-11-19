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
		log.error(">>> Entering visit(nodes={},source={})", nodes, source);
        performInjection((ClassNode) nodes[1]);
		log.warn("<<< Exiting visit(nodes={},source={})", nodes, source);
    }

    public void performInjection(ClassNode classNode) {
		log.error(">>> Entering performInjection(classNode={})", classNode);
        if(shouldInject(classNode)) {
            injectIdProperty(classNode);

            injectVersionProperty(classNode);

            injectToStringMethod(classNode);

            injectAssociations(classNode);

        }
		log.warn("<<< Exiting performInjection(classNode={})", classNode);
    }

    public boolean shouldInject(URL url) {
		log.error(">>> Entering shouldInject(url={})", url);
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
	log.error(">>> Entering injectAssociations(classNode={})", classNode);

        List properties = classNode.getProperties();
		log.error("injectAssociations(classNode={}): properties → {}", classNode, properties);
        List propertiesToAdd = new ArrayList();
		log.error("injectAssociations(classNode={}): propertiesToAdd → {}", classNode, propertiesToAdd);
        for (Iterator p = properties.iterator(); p.hasNext();) {
            PropertyNode pn = (PropertyNode) p.next();
			log.error("injectAssociations(classNode={}): pn → {}", classNode, pn);
            final boolean isHasManyProperty = pn.getName().equals(/*GrailsDomainClassProperty.*/RELATES_TO_MANY) || pn.getName().equals(/*GrailsDomainClassProperty.*/HAS_MANY);
			log.error("injectAssociations(classNode={}): isHasManyProperty → {}", classNode, isHasManyProperty);
            if(isHasManyProperty) {
                Expression e = pn.getInitialExpression();
				log.error("injectAssociations(classNode={}): e → {}", classNode, e);
                propertiesToAdd.addAll(createPropertiesForHasManyExpression(e,classNode));
            }
            final boolean isBelongsTo = pn.getName().equals(/*GrailsDomainClassProperty.*/BELONGS_TO);
			log.error("injectAssociations(classNode={}): isBelongsTo → {}", classNode, isBelongsTo);
            if(isBelongsTo) {
                Expression e = pn.getInitialExpression();
                propertiesToAdd.addAll(createPropertiesForBelongsToExpression(e,classNode));
            }
        }
        injectAssociationProperties(classNode,propertiesToAdd);
		log.warn("<<< Exiting injectAssociations(classNode={})", classNode);
    }

    private Collection createPropertiesForBelongsToExpression(Expression e, ClassNode classNode)
    {
		log.error(">>> Entering createPropertiesForBelongsToExpression(e={},classNode={})", e, classNode);
        List properties = new ArrayList();
        if(e instanceof MapExpression) {
			log.error("createPropertiesForBelongsToExpression(e={},classNode={}): properties → {}", e, classNode, properties);
            MapExpression me = (MapExpression)e;
			log.error("createPropertiesForBelongsToExpression(e={},classNode={}): me → {}", e, classNode, me);
            List mapEntries = me.getMapEntryExpressions();
			log.error("createPropertiesForBelongsToExpression(e={},classNode={}): mapEntries → {}", e, classNode, mapEntries);
            for (Iterator i = mapEntries.iterator(); i.hasNext();) {
                MapEntryExpression mme = (MapEntryExpression) i.next();
				log.error("createPropertiesForBelongsToExpression(e={},classNode={}): mme → {}", e, classNode, mme);
                String key = mme.getKeyExpression().getText();
				log.error("createPropertiesForBelongsToExpression(e={},classNode={}): key → {}", e, classNode, key);

                String type = mme.getValueExpression().getText();
				log.error("createPropertiesForBelongsToExpression(e={},classNode={}): type → {}", e, classNode, type);

                properties.add(new PropertyNode(key,Modifier.PUBLIC, ClassHelper.make(type) , classNode, null,null,null));
            }
        }

		log.warn("<<< Exiting createPropertiesForBelongsToExpression(e={},classNode={})", e, classNode);
        return properties;
    }

    private void injectAssociationProperties(ClassNode classNode, List propertiesToAdd) {
		log.error(">>> Entering injectAssociationProperties(classNode={},propertiesToAdd={})", classNode, propertiesToAdd);
        for (Iterator i = propertiesToAdd.iterator(); i.hasNext();) {
            PropertyNode pn = (PropertyNode) i.next();
            if(!/*GrailsASTUtils.*/hasProperty(classNode, pn.getName())) {
                //if(LOG.isDebugEnabled()) {
                //    LOG.debug("[GrailsDomainInjector] Adding property [" + pn.getName() + "] to class [" + classNode.getName() + "]");
                //}
                classNode.addProperty(pn);
            }
        }
		log.warn("<<< Exiting injectAssociationProperties(classNode={},propertiesToAdd={})", classNode, propertiesToAdd);
    }

    private List createPropertiesForHasManyExpression(Expression e, ClassNode classNode) {
		log.error(">>> Entering createPropertiesForHasManyExpression(e={},classNode={})", e, classNode);
        List properties = new ArrayList();
        if(e instanceof MapExpression) {
			log.error("createPropertiesForHasManyExpression(e={},classNode={}): properties → {}", e, classNode, properties);
            MapExpression me = (MapExpression)e;
			log.error("createPropertiesForHasManyExpression(e={},classNode={}): me → {}", e, classNode, me);
            List mapEntries = me.getMapEntryExpressions();
			log.error("createPropertiesForHasManyExpression(e={},classNode={}): mapEntries → {}", e, classNode, mapEntries);
            for (Iterator j = mapEntries.iterator(); j.hasNext();) {
                MapEntryExpression mee = (MapEntryExpression) j.next();
				log.error("createPropertiesForHasManyExpression(e={},classNode={}): mee → {}", e, classNode, mee);
                Expression keyExpression = mee.getKeyExpression();
				log.error("createPropertiesForHasManyExpression(e={},classNode={}): keyExpression → {}", e, classNode, keyExpression);
                String key = keyExpression.getText();
				log.error("createPropertiesForHasManyExpression(e={},classNode={}): key → {}", e, classNode, key);
                addAssociationForKey(key,properties,classNode);
            }
        }
		log.warn("<<< Exiting createPropertiesForHasManyExpression(e={},classNode={})", e, classNode);
        return properties;
    }

    private void addAssociationForKey(String key, List properties, ClassNode classNode) {
			log.error(">>> Entering addAssociationForKey(key={},properties={},classNode={})", key, properties, classNode);
            properties.add(new PropertyNode(key, Modifier.PUBLIC, new ClassNode(Set.class), classNode,null, null, null));
			log.warn("<<< Exiting addAssociationForKey(key={},properties={},classNode={})", key, properties, classNode);
    }

    private void injectToStringMethod(ClassNode classNode) {
		log.error(">>> Entering injectToStringMethod(classNode={})", classNode);
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
		log.warn("<<< Exiting injectToStringMethod(classNode={})", classNode);
    }

    private void injectVersionProperty(ClassNode classNode) {
		log.error(">>> Entering injectVersionProperty(classNode={})", classNode);
        final boolean hasVersion = /*GrailsASTUtils.*/hasProperty(classNode, /*GrailsDomainClassProperty.*/VERSION);

        if(!hasVersion) {
            //if(LOG.isDebugEnabled()) {
            //    LOG.debug("[GrailsDomainInjector] Adding property [" + GrailsDomainClassProperty.VERSION + "] to class [" + classNode.getName() + "]");
            //}
            classNode.addProperty( /*GrailsDomainClassProperty.*/VERSION, Modifier.PUBLIC, new ClassNode(Long.class), null, null, null);
        }
		log.warn("<<< Exiting injectVersionProperty(classNode={})", classNode);
    }

    private void injectIdProperty(ClassNode classNode) {
		log.error(">>> Entering injectIdProperty(classNode={})", classNode);
        final boolean hasId = /*GrailsASTUtils.*/hasProperty(classNode,/*GrailsDomainClassProperty.*/IDENTITY);

        if(!hasId) {
            //if(LOG.isDebugEnabled()) {
            //    LOG.debug("[GrailsDomainInjector] Adding property [" + GrailsDomainClassProperty.IDENTITY + "] to class [" + classNode.getName() + "]");
            //}
            classNode.addProperty( /*GrailsDomainClassProperty.*/IDENTITY, Modifier.PUBLIC, new ClassNode(Long.class), null, null, null);
        }
		log.warn("<<< Exiting injectIdProperty(classNode={})", classNode);
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
		log.error(">>> Entering hasProperty(classNode={},propertyName={})", classNode, propertyName);
        if(classNode == null || propertyName == null || "".equals(propertyName.trim()))
            return false;

        List properties = classNode.getProperties();
		log.error("hasProperty(classNode={},propertyName={}): properties → {}", classNode, propertyName, properties);
        for (Iterator i = properties.iterator(); i.hasNext();) {
            PropertyNode pn = (PropertyNode) i.next();
			log.error("hasProperty(classNode={},propertyName={}): pn → {}", classNode, propertyName, pn);
            if(pn.getName().equals(propertyName))
                return true;
        }
		log.warn("<<< Exiting hasProperty(classNode={},propertyName={})", classNode, propertyName);
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
		log.error(">>> Entering implementsZeroArgMethod(classNode={},methodName={})", classNode, methodName);
        boolean result = implementsMethod(classNode, methodName, new Class[0]);
		log.warn("<<< Exiting implementsZeroArgMethod(classNode={},methodName={}) -> {}", classNode, methodName, result);
        return result;
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
		log.error(">>> Entering implementsMethod(classNode={},methodName={},argTypes={})", classNode, methodName, argTypes);
        List methods = classNode.getMethods();
        if (argTypes == null || argTypes.length ==0) {
			log.error("implementsMethod(classNode={},methodName={},argTypes={}): methods → {}", classNode, methodName, argTypes, methods);
            for (Iterator i = methods.iterator(); i.hasNext();) {
                MethodNode mn = (MethodNode) i.next();
				log.error("implementsMethod(classNode={},methodName={},argTypes={}): mn → {}", classNode, methodName, argTypes, mn);
                boolean methodMatch = mn.getName().equals(methodName);
				log.error("implementsMethod(classNode={},methodName={},argTypes={}): methodMatch → {}", classNode, methodName, argTypes, methodMatch);
                if(methodMatch)return true;
                // TODO Implement further parameter analysis
            }
        }
		log.warn("<<< Exiting implementsMethod(classNode={},methodName={},argTypes={})", classNode, methodName, argTypes);
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
