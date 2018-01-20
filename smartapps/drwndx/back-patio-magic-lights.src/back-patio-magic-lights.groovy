/**
 *  Back Patio Magic Lights
 *
 *  Copyright 2018 MIKE ROSE
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */
definition(
    name: "Back Patio Magic Lights",
    namespace: "drwndx",
    author: "MIKE ROSE",
    description: "Turns on back patio lights based on trigger",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Open/Close Sensor") {
    	input "openCloseSensor","capability.contactSensor", title: "Choose contact sensor", required: true
    }
	section("Light switches to turn on") {
    	input "lightSwitches", "capability.switch", title: "Choose light switches", required: true, multiple: true
  	}
	section("Delay before turning off") {
    	input "delayMins", "number", title: "Keep lights on for how many minutes?", required: true
 	}
    section("Turn on between what times?") {
        input "fromTime", "time", title: "From", required: true
        input "toTime", "time", title: "To", required: true
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
    subscribe(openCloseSensor, "contact.open", contactHandler)
}

def contactHandler(evt) {

    // Door is opened. Now check if the current time is within the visiting hours window
    def between = timeOfDayIsBetween(fromTime, toTime, new Date(), location.timeZone)
    def delayTime = 1000 * 60 * delayMins - 1000
    if (between) {
        lightSwitches.on()
        log.debug "light switch on event"
        lightSwitches.off([delay: delayTime])
        log.debug "light switch off event"
    } else {
        // do nothing
    }
}