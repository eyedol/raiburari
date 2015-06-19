Change Log
=========

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