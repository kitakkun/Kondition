# Kondition

[![Maven Central](https://img.shields.io/maven-central/v/com.kitakkun.kondition/core)](https://central.sonatype.com/search?namespace=com.kitakkun.kondition)
[![Kotlin](https://img.shields.io/badge/kotlin-2.0.0--2.1.0-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![License](https://img.shields.io/badge/license-Apache-blue.svg)](https://github.com/kitakkun/Kondition/blob/master/LICENSE)
![Platform](https://img.shields.io/badge/platform-Android_JVM_iOS_macOS_watchOS_tvOS_Linux_Windows-blue)

Kondition is a multiplatform compiler plugin which ensures that your Kotlin code runs under certain
conditions.
You can provide requirements to parameters simply by annotating them.

## Example

For instance:

```kotlin
fun myFunction(@Ranged(0, 10) value: Int) {
    println(value)
}
```

will be transformed to:

```kotlin
fun myFunction(@Ranged(0, 10) value: Int) {
    require(value in 0..10) { "\"value\" in playWithInt must be in range 0..10" }
    println(value)
}
```

> [!IMPORTANT]
> 🚧 THIS PROJECT IS STILL A WORK IN PROGRESS. 🚧
> API IS UNSTABLE AND MAY CHANGE IN THE FUTURE.

## Installation

Make sure you are using Kotlin 2.0.0 or above.

```kotlin
repositories {
    mavenCentral()
}

plugins {
    id("com.kitakkun.kondition") version "<version>"
}
```

## Available Annotations

See [Predefined Annotations](docs/predefined_annotations.md) for details.

## Define Custom Konditions (Planned)

### Combine predefined annotations

You can create a new Kondition by combine existent annotations.

```kotlin
@Combine(with = CombineRule.AND)
@Ranged(0, 10)
@Ranged(5, 10)
annotation class MyAnnotation
```

### Define custom kondition

```kotlin
@CustomKondition(implementationClass = MyCustomConditionImpl::class)
annotation class MyCustomCondition

class MyCustomConditionImpl : Kondition {
    override fun check(type: KClass<*>, value: Any?) {
        // TODO: check the value
    }
}
```
