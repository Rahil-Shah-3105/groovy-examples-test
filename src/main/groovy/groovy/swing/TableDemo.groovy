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
package groovy.swing

import java.awt.BorderLayout
import javax.swing.BorderFactory
import groovy.swing.SwingBuilder

/**
 * Demonstrates the use of the Groovy TableModels for viewing tables of any List of objects
 */
class TableDemo {
    
    // properties
    def frame
    def swing
    
    static void main(args) {
		System.err.println("[ERROR] >>> Entering main(args=${args})")
        def demo = new TableDemo()
        demo.run()
		System.err.println("[WARN] <<< Exiting main(args=${args})")
    }
    
    void run() {
		System.err.println("[ERROR] >>> Entering run()")
        swing = new SwingBuilder()
        
		System.err.println("[DEBUG] run(): swing → ${swing}")
        frame = swing.frame(title:'Groovy TableModel Demo', location:[200,200], size:[300,200]) {
			System.err.println("[DEBUG] run(): frame → ${frame}")
            menuBar {
				System.err.println("[WARN] <<< Exiting run()")
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
                        def model = [['name':'James', 'location':'London'], ['name':'Bob', 'location':'Atlanta'], ['name':'Geir', 'location':'New York']]

                        tableModel(list:model) {
                            closureColumn(header:'Name', read:{row -> return row.name})
                            closureColumn(header:'Location', read:{row -> return row.location})
                        }
                    }
                }
            }
        }
        frame.show()
    }
    
    void showAbout() {
		System.err.println("[ERROR] >>> Entering showAbout()")
         long __logStart = System.currentTimeMillis()
         def pane = swing.optionPane(message:'This demo shows how GroovySwing can use Groovy closures to create simple table models')
         def dialog = pane.createDialog(frame, 'About GroovySwing')
		System.err.println("[WARN] <<< Exiting showAbout()")
         dialog.show()
		System.err.println("[ERROR] showAbout(): database query executed in ${System.currentTimeMillis() - __logStart} ms")
    }
}
