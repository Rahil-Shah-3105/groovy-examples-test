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
package groovy.swing;

import groovy.lang.GroovyObject;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyClassLoader;
import groovy.util.GroovyTestCase;

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * 
 * @author <a href="mailto:james@coredevelopers.net">James Strachan</a>
 */
public class Demo extends GroovyTestCase {
	private static final Logger log = Logger.getLogger(Demo.class.getName());
    ClassLoader parentLoader = getClass().getClassLoader();
    protected GroovyClassLoader loader =
        (GroovyClassLoader) AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
				log.debug(">>> Entering run()");
				log.debug("<<< Exiting run()");
                return new GroovyClassLoader(parentLoader);
            }
        });

    public static void main(String[] args) throws Exception {
		log.debug(">>> Entering main(args={})", args);
        Demo demo = new Demo();
		log.debug("main(args={}): demo → {}", args, demo);
        GroovyObject object = demo.compile("src/examples/groovy/swing/SwingDemo.groovy");
		log.debug("main(args={}): object → {}", args, object);
        object.invokeMethod("run", null);
		log.debug("<<< Exiting main(args={})", args);
    }

    protected GroovyObject compile(String fileName) throws Exception {
		log.debug(">>> Entering compile(fileName={})", fileName);
        Class groovyClass = loader.parseClass(new GroovyCodeSource(new File(fileName)));
        GroovyObject object = (GroovyObject) groovyClass.newInstance();
		log.debug("compile(fileName={}): object → {}", fileName, object);
		log.debug("compile(fileName={}): groovyClass → {}", fileName, groovyClass);
        assertTrue(object != null);
		log.debug("<<< Exiting compile(fileName={})", fileName);
        return object;
    }
}
