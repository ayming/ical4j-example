ical4j-example
==============

Simple example to read iCal with ical4j

* read iCal from url
* read iCal from string
* get the event details in calendar
* filter the events with rules

Code implementation is placed in MainActivity.java and `app/build.gradle`.

```
android {
    packagingOptions {
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/NOTICE.txt'
    }
}
```

## Requirement

Android Studio 0.8.14

[ical4j](https://github.com/ical4j/ical4j/wiki)
