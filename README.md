## High level requirements

* Fetch data from a web service and display the returned data in a RecyclerView
* Display the full details of a selected store on a separate screen.

## Requirements

* Build app using Android Studio utilizing the latest Android SDK
* Use Kotlin as much as possible
* Cache the data using a method of your choice
* Display a message to the user if a data connection or cache is not available
* Fetch the data asynchronously
* Display the returned data in a Recycler View

At a minimum, display the following items:
* Store Logo
* Phone number
* Address


## Concepts applied

* Built using Android Studio 3.6 and with a target SDK version of 29 and min SDK of 21.
* 100% in Kotlin
* Consumed data via Retrofit2
* Cached data via Room persistence library
* Utilized NetworkBoundResource, using suspending functions / coroutines for handling cached data and network calls - in this configuration will always attempt to refresh data after cache, but show cached data if there is no connection. These are called on the IO Dispatcher and results updated on main thread.

* MVVM architecture, utilizing Dagger2, ViewModels, Repository layer
* Navigation components for navigating between screens
* Live data for updating UI from view model data and updating user network status

* Recyclerview on main screen shows store logo, store name, and address
* Detail view shows a backdrop image (used a stock photo, ideally we have some more images to show here), store information and map location with actions to call or navigate to the store.


* Screen shots attached and apk attached
* Maps api key in project root
