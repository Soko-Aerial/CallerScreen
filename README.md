The Caller Screen

****Description****
This is project displays a caller screen when there is an incoming call and give you the ability to Answer or Decline the call


****Features****
1. The caller screen which diplasys the callers name, the Answer and Decline button and a message button
2. The answer button accepts the call and opens the Answer screen
3. The Decline button ends the call
4. A quick message button which displays a quick messages to send to the caller
5. It has the ability to pick the ringtone of the phone and play it during an incoming call, vibrate when on vibration and silent depending on the mode 


****Importing Into Your Projec****

Add it in your root settings.gradle at the end of repositories:
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

Add the dependency:
dependencies {
	        implementation 'com.github.Soko-Aerial:CallerScreen:1.0.1'
	}

 
