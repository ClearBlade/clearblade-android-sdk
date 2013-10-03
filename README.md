Android-API
===========
The android-api project provides source and binary for interacting with the ClearBlade platform.  Javadoc can be found in the source
download and publically hosted here https://platform.clearblade.com/static/docs/android/index.html

Project Management
------------------
This project is managed via jazzhub.  https://hub.jazz.net/project/AaronAllsbrook2/ClearBlade%20Android-API

Dependencies
------------
The android-api depends on the Paho MQTT java drivers. http://www.eclipse.org/paho/   This source for this project is found here http://git.eclipse.org/c/paho/org.eclipse.paho.mqtt.java.git/

As of October 3, 2013 the java paho driver in use is version .4


Setup as Android Library 
------------------------



Setup as Eclipse project
------------------------

Android App Configuration
-------------------------
The Android-API attempts to assist in the threading needs of android.  To use the background android service provided by this libary you will need to add the following declaration to your android manifest.
     <service
		  android:name="com.clearblade.platform.api.internal.MessageService"
		  android:icon="@drawable/ic_launcher"
		  android:label="@string/message_service"
		  >
		</service> 
This declaration is included in the test application.  Failing to include this declration will mostly likely result in a noop behavior from the app and a quiet runtime exception in the console.

Additional Credits and helpful links
------------------------------------
Everything here is written fresh but like anything its inspired and bug fixed via others works. MQTT especially.  Credit where credit is due

-  https://github.com/JesseFarebro/Android-Paho-MQTT-Service
-  http://mqtt.org/wiki/doku.php/mqtt_on_the_android_platform
-  http://dalelane.co.uk/blog/?p=1599#code
-  http://www.hardill.me.uk/wordpress/?p=204
-  http://www.vogella.com/articles/AndroidServices/article.html
-http://android-coding.blogspot.com/2011/11/interactive-between-activity-and.html

About MQTT
----------
This is a pretty exciting new mobile messaging specification and we are excited to make it available in the cloud.  Learn more at
-http://mqtt.org/






