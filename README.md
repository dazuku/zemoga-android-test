# Zemoga Android Test

Basic test application that lists all messages and their details from [JSONPlaceholder](https://jsonplaceholder.typicode.com)

# Running the App
## Requeriments
* Android Studio 3.1.2
* JRE 1.8.0_152
* Android SDK API 27

I attached the APK in the root of the project if you want to check the application running

# External Libraries
* [Volley](https://github.com/google/volley): Volley is an HTTP library that makes networking for Android apps easier and, most importantly, faster.
* [Android Support Libraries](https://developer.android.com/topic/libraries/support-library/): A large amount of the support libraries provide backward compatibility for newer framework classes and methods. For example, the Fragment support class provides support for fragments on devices running versions earlier than Android 3.0 (API level 11).

# Arquitecture
I used the [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/) but only the ViewModel part because this is a small app, so, It's not necessary to use the whole arquitecture. I used the the ViewModel to manage persistency and cache in each Activity.
