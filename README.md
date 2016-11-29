Raiburari [ ![Download](https://api.bintray.com/packages/eyedol/maven/Raiburari/images/download.svg) ](https://bintray.com/eyedol/maven/Raiburari/_latestVersion)
=========

This is a reusable Android SDK for writing Android apps. The need came as a result of repeatedly constructing the same directory structure and base classes when starting a new Android project.

Modules
=======

The SDK comes with three main modules.

raiburari-annotation
--------------------

Annotations for the SDK. 

Comes with two annotations. `@TransformEntity` - for class and `@Transform` for fields.

raiburari-processor
-------------------
A processor for the annotations.

The SDK comes with a multi-layer Models or Entities. This necessitates the need to have an object mapper to map / transform from one layer's entity to another layer's model. Writing this transformer or object mapper class can be a bit boring. This annotation processor aims to automate the process by providing a set of annotations you can apply to a model class and fields so it generates the source for the transformer class for you.

raiburari
---------
Main module that contains all the base classes, custom views, and reusable components.


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
  <artifactId>raiburari-annotations</artifactId>
  <version>LATEST</version>
</dependency>

<dependency>
  <groupId>com.addhen.android</groupId>
  <artifactId>raiburari-processor</artifactId>
  <version>LATEST</version>
</dependency>

<dependency>
  <groupId>com.addhen.android</groupId>
  <artifactId>raiburari</artifactId>
  <version>LATEST</version>
</dependency>
```

Credits
--------
This is heavily based on other people's work. Here includes:

1. Fernando Cejas' [Android clean architecture][1] example.
2. Uncle bob's [clean architecture blog][2] post. 
3. Hannes Dorfmann's [annotation processing 101][3] blog post.
4. Chris Banes' [Cheese Square][4] Android app.
5. Jake Wharton's [ButterKinife][5].
6. Kaushik Gopal's [RxJava Android][6] samples.


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
    

[1]: https://github.com/android10/Android-CleanArchitecture/
[2]: http://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html
[3]: http://hannesdorfmann.com/annotation-processing/annotationprocessing101
[4]: https://github.com/chrisbanes/cheesesquare
[5]: https://github.com/JakeWharton/butterknife
[6]: https://github.com/kaushikgopal/RxJava-Android-Samples