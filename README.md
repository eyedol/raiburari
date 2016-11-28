Raiburari [ ![Download](
) ](https://bintray.com/eyedol/maven/Raiburari/_latestVersion)
=========

This is a resuable Android SDK for writing Android apps. The need came as a result of repeatedly constructing the same directory stucture and base classes when starting a new Android project.

Modules
=======

The SDK comes with three main modules.

raiburari-annotation
--------------------

Annotations for the SDK. 

Comes with two annotations. `@TransformEntity` - for class and `@Transform` for fields.

raiburari-processor
-------------------
Processor for the annotations.

The SDK comes with a multi-layer Models or Entities. This necessitates the need to have an object mapper to map / transform from one layer's entity to another layer's model. Writing this transformer or object mapper class can be a bit boring. This annotation processor aims to automate the process by providing a set of annotations you can apply to a model class and fields so it generates the source for the transformer class for you.

raiburari
---------
Main module that contains all the base classes, custom views and resuable components.


Setup
=====
You can use [gradleplease](http://gradleplease.appspot.com/#raiburari) to find the latest.

Gradle
------
```groovy
   dependencies {
       compile 'com.addhen.android:raiburari-annotation:<latest_version>'
       compile 'com.addhen.android:raiburari-processor:<latest_version>'
       compile 'com.addhen.android:raiburari:<latest_version>'
   }
```

Maven
-----
```groovy
<dependency>
  <groupId>com.addhen.android</groupId>
  <artifactId>raiburari</artifactId>
  <version>LATEST</version>
</dependency>
```


License
--------

    Copyright 2015 - 2016 Henry Addo

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.