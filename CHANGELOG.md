Change Log
==========
## v2.16.0
_2016-08-19_

* Upgrade support library to `v24.2.0`.
* Upgrade dagger to `v2.6`.
* Upgrade Otto.
* Upgrade ButterKnife.
* Remove unused test dependencies.

## v2.16.0
_2016-06-24_

* Remove Timber as a dependency.
* Upgrade support library to `v24.0.0`.
* Upgrade dagger to `v2.5`.
* Add `SerializedName` with `id` value to data entity to be usable by Gson.

## v2.15.0
_2016-06-02_

* Remove Picasso as a dependency
* Upgrade ButterKnife to v8.0.1. It now uses `apt` for annotation-processing so you need to add it as a dependency.

## v2.14.2
_2016-05-24_

* Add the name of a styleable as a prefix to `attrs` name otherwise some of the name conflicts with Android's `attrs` name

## v2.14.1
_2016-05-24_

* Add Rai prefix to `attrs` name otherwise some of the name conflicts with Android's `attrs` name 


## v2.14.0
_2016-05-12_

* Update support library to v23.4.0 

## v2.13.2
_2016-05-11_

* Change `getLasKnowLocation` to `getLastKnownLocation` minor typo 
* Make `LocationUpdatesObservable` and `LastKnownLocationObservable` constructors protected. 

## v2.13.1
_2016-05-11_

* Call onComplete right after onNext is called because we're calling onError 
* Make `LocationUpdatesObservable` and `LastKnownLocationObservable` constructors public

## v2.13.0
_2016-05-07_

* Add android-check for static analysis 
* Lot's of code and resources clean up
* Fix bug with location observable throwing error when location object is null

## v2.12.0
_2016-04-22_

* Update Android support and design libraries to v23.3.0
* Downgrade `Timber` to v3.0.0 for minSDK 9 support
* Update `buildTools` to 24.0.0 rc2

## v2.11.0
_2016-03-13_

* Update Android support and design libraries to v23.2.1
* Update `Timber` to v4.1.1
* Update `Gson` to v2.6.2
* Update `buildTools` to 24.0.0 rc1

## v2.10.0
_2016-02-25_

* Update Android support and design libraries to v23.2.0
* Update Dagger 2 to v2.0.2
* Update `RxJava` to v1.1.1 

## v2.9.0
_2016-01-30_

* Add a workaround for ButterKnife not binding the super class BloatedRecyclerview when the child class binds it views using butterknife
* Upgrade Android gradle plugin

## v2.8.0
_2015-11-13_

* Update Android support library to v23.1.1.
* Implement Rx base location manager for fetching the users location via GPS or the network.

## v2.7.0
_2015-10-16_

* Update Android support library to v23.1.0
* Update most of the dependencies to their latest version
* Fix typo in a method name. Now `showSnabackar` has been renamed to `showSnackbar`

## v2.6.1
_2015-09-09_

* Update Android support library to v23.0.1.

## v2.6.0
_2015-08-22_

* Update Android support library to v23.0.0.
* Target Android API level 23

## v2.5.0
_2015-07-23_

* Update Android support library to v2.2.1.

## v2.4.0
_2015-07-09_

* Make TypedPreference support devices below Gingerbread.

## v2.3.1
_2015-07-03_

* Unbind fragment's views after it has been destroyed.

## v2.3.0
_2015-07-01_

* Upgrade `ButterKnife` to `7.0.1` to make use of the new APIs.
* Add `Inject` to the Typed SharedPreferences so they can be injected via Dagger.

## v2.2.0
_2015-06-19_

* Downgrade minimum Android SDK support from `14` to `8`
* Upgrade build tools.
* Add change log file.
* Remove `ContextMenu` because it uses API from SDK 12.
* Switch to using the `LruCache` from the support library.

## v2.1.0
_2015-05-23_

* Make `ApplicationState` class abstract to allow child classes to extend from it.

## v2.0.0
_2015-05-23_

* Used the support design library for theming and widgets.
* Replaced custom implementation of FAB with the standard one from the support library.
* Upgrade support library.
* Improved ContextMenu widget to handle app scroll and orientation changes.

## v1.3.0
_2015-05-23_

* Upgraded Snackbar library version.

## v1.2.0
_2015-05-28_

* Implemented a `SharedPreferences` with `RxJava` support.

## v1.1.0
_2015-05-20_

* Optimize build time.
* Add sample code demonstrating the use of Mapbox's Android SDK.
* Add custom widget to provide context menu for taking secondary actions from a list item.
* Add license to source code headers.

## v1.0.0
_2015-05-13_

* Added CRUD methods to domain's `Repository` interface.

## v0.7.1
_2015-05-08_

* Refactor presentation layer's model to remove getters and setters.
* Properly pad navigation drawer's content to match material design spec.

## v0.7.0
_2015-05-08_

* Styled sample app match toolbar to match the selected parent theme.

## v0.6.0
_2015-05-07_

* Turned on maven central sync option on bintray.
* Rename base data entity class name to `DataEntity` to match it being a base class for data entities.

## v0.5.0
_2015-05-05_

* Added missing lib version number field to enable uploads to bintray.

## v0.4.0
_2015-05-05_

* Initial base implementation of the [clean architecture](https://blog.8thlight.com/uncle-bob/2011/11/22/Clean-Architecture.html)