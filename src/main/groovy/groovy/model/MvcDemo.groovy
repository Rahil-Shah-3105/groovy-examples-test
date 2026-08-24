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
package groovy.model

import groovy.swing.SwingBuilder
/**
 * 
 */
class MvcDemo {
    
    def frame
    def swing
    
    void run() {
		println("[DEBUG] >>> Entering run()")
        swing = new SwingBuilder()
        
		println("[DEBUG] run(): swing → ${swing}")
        def frame = swing.frame(title:'MVC Demo', location:[200,200], size:[300,200]) {
			println("[DEBUG] <<< Exiting run()")
            menuBar {
                menu(text:'Help') {
                    menuItem() {
                        action(name:'About', closure:{ showAbout() })
                    }
                }
            }
            panel {
                borderLayout()
                scrollPane(constraints:CENTER) {
                    table() {
                        tableModel(list:[ ['name':'James', 'location':'London'], ['name':'Bob', 'location':'Atlanta'] ]) {
                            propertyColumn(header:'Name', propertyName:'name')
                            propertyColumn(header:'Location', propertyName:'location')
                        }
                    }
                }
            }
        }        
        frame.show()
    }
 
    void showAbout() {
		println("[DEBUG] >>> Entering showAbout()")
		long __logStart = System.currentTimeMillis()
         def pane = swing.optionPane(message:'This demo shows how you can create UI models from simple MVC models')
         def dialog = pane.createDialog(frame, 'About MVC Demo')
		println("[DEBUG] <<< Exiting showAbout()")
		println("[INFO] showAbout(): database query executed in ${System.currentTimeMillis() - __logStart} ms")
         dialog.show()
    }
}
