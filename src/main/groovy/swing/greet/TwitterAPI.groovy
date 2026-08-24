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
/**
 * Created by IntelliJ IDEA.
 * User: Danno.Ferrin
 * Date: Apr 25, 2008
 * Time: 9:47:20 PM
 */
package groovy.swing.greet

import groovy.beans.Bindable

class TwitterAPI {

    @Bindable String status = "\u00a0"
    def authenticatedUser
    XmlSlurper slurper = new XmlSlurper()
    def imageMap = [:]

    def withStatus(status, c) {
		println("[DEBUG] >>> Entering withStatus(status=${status},c=${c})")
        setStatus(status)
        try {
            def o = c()
            setStatus("\u00a0")
            return o
        } catch (Throwable t) {
			println("[ERROR] Exception in withStatus(status=${status},c=${c}): ${t.getMessage()}")
            setStatus("Error $status : ${t.message =~ '400'?'Rate Limit Reached':t}")
		println("[DEBUG] <<< Exiting withStatus(status=${status},c=${c})")
            throw t
        }
    }


    boolean login(def name, def password) {
		println("[DEBUG] >>> Entering login(name=${name},password=<redacted>)")
        withStatus("Logging in") {
            Authenticator.setDefault(
                [getPasswordAuthentication : {
                    return new PasswordAuthentication(name, password) }
                ] as Authenticator)
            authenticatedUser = getUser(name)
			println("[DEBUG] login(name=${name},password=<redacted>): authenticatedUser → ${authenticatedUser}")
		println("[DEBUG] <<< Exiting login(name=${name},password=<redacted>)")
            return true
        }
    }

    def getFriends() {
		println("[DEBUG] >>> Entering getFriends()")
        getFriends(authenticatedUser)
		println("[DEBUG] <<< Exiting getFriends()")
    }

    def getFriends(String user) {
		println("[DEBUG] >>> Entering getFriends(user=${user})")
		println("[ERROR] Exception in getFriends(user=${user}): ${e.message}")
		println("[DEBUG] <<< Exiting getFriends(user=${user})")
        return getFriends(getUser(user))
    }

    def getFriends(user) {
        def friends = [user]
        withStatus("Loading Friends") {
            def page = 1
            def list = slurper.parse(new URL("http://twitter.com/statuses/friends/${user.screen_name}.xml").openStream())
            while (list.length) {
                list.user.collect(friends) {it}
                page++
                try {
                  list = slurper.parse("http://twitter.com/statuses/friends/${user.screen_name}.xml&page=$page")
                } catch (Exception e) { break }
				println("[ERROR] Exception in getFriends(user=${user}): ${e.message}")
            }
        }
        withStatus("Loading Friends Images") {
            return friends.each {
                loadImage(it.profile_image_url as String)
            }
        }
    }

    def getFriendsTimeline() {
		println("[DEBUG] >>> Entering getFriendsTimeline()")
        getFriendsTimeline(user)
		println("[DEBUG] <<< Exiting getFriendsTimeline()")
    }

    def getFriendsTimeline(String friend) {
		println("[DEBUG] >>> Entering getFriendsTimeline(friend=${friend})")
        getFriendsTimeline(getUser(friend))
		println("[DEBUG] <<< Exiting getFriendsTimeline(friend=${friend})")
    }

    def getFriendsTimeline(user) {
		println("[DEBUG] >>> Entering getFriendsTimeline(user=${user})")
        def timeline = []
        withStatus("Loading Timeline") {
            timeline =  slurper.parse(
                    new URL("http://twitter.com/statuses/friends_timeline/${user.screen_name}.xml").openStream()
                ).status.collect{it}
        }
        withStatus("Loading Timeline Images") {
            return timeline.each {
                loadImage(it.user.profile_image_url as String)
            }
        }
		println("[DEBUG] <<< Exiting getFriendsTimeline(user=${user})")
    }

    def getTweets() {
		println("[DEBUG] >>> Entering getTweets()")
		println("[DEBUG] <<< Exiting getTweets()")
        return getTweets(user)
    }

    def getTweets(String friend) {
		println("[DEBUG] >>> Entering getTweets(friend=${friend})")
		println("[DEBUG] <<< Exiting getTweets(friend=${friend})")
        return getTweets(getUser(frield))
    }

    def getTweets(friend) {
        def tweets = []
        withStatus("Loading Tweets") {
            tweets = slurper.parse(
                    new URL("http://twitter.com/statuses/user_timeline/${friend.screen_name}.xml").openStream()
                ).status.collect{it}
        }
        withStatus("Loading Tweet Images") {
            return tweets.each {
                loadImage(it.user.profile_image_url as String)
            }
        }
    }

    def getUser(String screen_name) {
		println("[DEBUG] >>> Entering getUser(screen_name=${screen_name})")
        withStatus("Loading User $screen_name") {
            if (screen_name.contains('@')) {
                return slurper.parse(
                        new URL("http://twitter.com/users/show.xml?email=${screen_name}").openStream()
                    )
            } else {
                return slurper.parse(
                        new URL("http://twitter.com/users/show/${screen_name}.xml").openStream()
                    )
            }
        }
		println("[DEBUG] <<< Exiting getUser(screen_name=${screen_name})")
    }

    def tweet(message) {
		println("[DEBUG] >>> Entering tweet(message=${message})")
        withStatus("Tweeting") {
            def urlConnection = new URL("http://twitter.com/statuses/update.xml").openConnection()
            urlConnection.doOutput = true
			println("[DEBUG] tweet(message=${message}): urlConnection.doOutput → ${urlConnection.doOutput}")
            urlConnection.outputStream << "status=${URLEncoder.encode(message, 'UTF-8')}"
		println("[DEBUG] <<< Exiting tweet(message=${message})")
            return slurper.parse(urlConnection.inputStream)
        }
    }

    // no need to read these, swing seems to cache these so the EDT won't stall
    def loadImage(image) {
		println("[DEBUG] >>> Entering loadImage(image=${image})")
        if (!imageMap[image]) {
            Thread.start {imageMap[image] = new javax.swing.ImageIcon(new URL(image))}
        }
		println("[DEBUG] <<< Exiting loadImage(image=${image})")
    }

}