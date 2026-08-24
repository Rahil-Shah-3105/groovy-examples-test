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
/*
 * Created by IntelliJ IDEA.
 * User: Danno.Ferrin
 * Date: Apr 26, 2008
 * Time: 8:32:03 AM
 */
package groovy.swing.greet

import groovy.beans.Bindable
import groovy.swing.SwingBuilder
import javax.swing.JOptionPane

class Greet {

    TwitterAPI api
    Binding view

    @Bindable boolean allowLogin = true
    @Bindable boolean allowSelection = true
    @Bindable boolean allowTweet = true
    @Bindable def focusedUser = ""
    @Bindable def friends  = []
    @Bindable def tweets   = []
    @Bindable def timeline = []
    @Bindable def statuses = []

    void startUp() {
		println("[DEBUG] >>> Entering startUp()")
        setAllowSelection(false)
        setAllowTweet(false)
        view.greetFrame.show()
        view.loginDialog.show()
		println("[DEBUG] <<< Exiting startUp()")
    }

    void login(evt) {
		println("[DEBUG] >>> Entering login(evt=${evt})")
        setAllowLogin(false)
        view.doOutside {
            try {
                if (api.login(view.twitterNameField.text, view.twitterPasswordField.password)) {
                    setFriends(api.getFriends(api.authenticatedUser))
                    friends.each {it.status.user = [screen_name:it.screen_name, profile_image_url:it.profile_image_url] }
                    setStatuses(friends.collect {it.status})
                    selectUser(api.authenticatedUser)
                    view.greetFrame.show()
                    view.loginDialog.dispose()
                } else {
                    JOptionPane.showMessageDialog(view.loginDialog, "Login failed")
                }
            } catch (Exception e) {
                e.printStackTrace()
            } finally {
                view.edt {
                    setAllowLogin(true)
                    setAllowSelection(true)
                    setAllowTweet(true)
                }
            }
        }
		println("[DEBUG] <<< Exiting login(evt=${evt})")
    }

    void filterTweets(evt = null) {
		println("[DEBUG] >>> Entering filterTweets(evt=${evt})")
        setAllowSelection(false)
        setAllowTweet(false)
        view.doOutside {
            try {
                setStatuses(
                    friends.collect {it.status}.findAll {it.text =~ view.searchField.text}
                )
                setTimeline(
                    api.getFriendsTimeline(focusedUser).findAll {it.text =~ view.searchField.text}
                )
                setTweets(
                    api.getTweets(focusedUser).findAll {it.text =~ view.searchField.text}
                )
            } catch (Exception e) {
                e.printStackTrace()
            } finally {
                view.edt {
                    setAllowSelection(true)
                    setAllowTweet(true)
                }
            }
        }
		println("[DEBUG] <<< Exiting filterTweets(evt=${evt})")
    }

    def userSelected(evt) {
		println("[DEBUG] >>> Entering userSelected(evt=${evt})")
        view.doOutside {
            selectUser(view.users.selectedItem)
        }
		println("[DEBUG] <<< Exiting userSelected(evt=${evt})")
    }

    def selectUser(user) {
		println("[DEBUG] >>> Entering selectUser(user=${user})")
        setAllowSelection(false)
        setAllowTweet(false)
        try {
            setFocusedUser(api.getUser(user.screen_name as String))
            setTweets(api.getTweets(focusedUser).findAll {it.text =~ view.searchField.text})
            setTimeline(api.getFriendsTimeline(focusedUser).findAll {it.text =~ view.searchField.text})
        } finally {
            view.edt {
                setAllowSelection(true)
                setAllowTweet(true)
            }
        }
		println("[DEBUG] <<< Exiting selectUser(user=${user})")
    }

    def tweet(evt = null) {
		println("[DEBUG] >>> Entering tweet(evt=${evt})")
        setAllowTweet(false)
        view.doOutside {
            try {
                api.tweet(view.tweetBox.text)
                // true story: it froze w/o the EDT call here
                view.edt {tweetBox.text = ""}
                filterTweets()
            } finally {
                setAllowTweet(true)
            }
        }
		println("[DEBUG] <<< Exiting tweet(evt=${evt})")
    }

    public static void main(String[] args) {
		println("[DEBUG] >>> Entering main(args=${args})")
        def model = new TwitterAPI()
        def controller = new Greet()
        def view = new SwingBuilder()

        controller.api = model
		println("[DEBUG] main(args=${args}): controller.api → ${controller.api}")
		println("[DEBUG] main(args=${args}): controller.view → ${controller.view}")
        controller.view = view

        view.controller = controller
		println("[DEBUG] main(args=${args}): view.controller → ${view.controller}")

        view.build(View)
        view.view = view
		println("[DEBUG] main(args=${args}): view.view → ${view.view}")

        controller.startUp()
		println("[DEBUG] <<< Exiting main(args=${args})")
    }
}