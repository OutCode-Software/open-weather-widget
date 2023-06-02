# open-weather-widget

Step 1. Add the JitPack repository to your build file
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency
dependencies {
	        implementation 'com.github.outcode-aashutosh:open-weather-widget:1.0.0'
	}


Example use has been provided on the app folder.

Project is setup for Open Weather Api Network Calls using MVVM
App automatically updates weather data when distance is increased by 10km from last stored location. This is done with Work Manager that runs on the background and refreshes the weather data from Api
Widget is built using Jetpack Compose
