# Kondition

![version](https://img.shields.io/badge/version-0.0.0--alpha02-blue)
![platform](https://img.shields.io/badge/platform-jvm-purple)

Kondition makes sure that your Kotlin code runs under certain conditions.
Functions or class constructors are invoked after checking specified conditions are met.

> [!IMPORTANT]
> 🚧 THIS PROJECT IS STILL A WORK IN PROGRESS 🚧

## Installation

Make sure you are using Kotlin 2.0.0 or above.

```kotlin
repositories {
    mavenCentral()
}

plugins {
    id("io.github.kitakkun.kondition") version "0.0.0-alpha02"
}
```

## Usage

For example:

```kotlin
fun playWithInt(@Ranged(0, 10) val value) {
    println(value)
}
```

will be transformed to:

```kotlin
fun playWithInt(@Ranged(0, 10) val value) {
    require(value in 0..10) { "\"value\" in playWithInt must be in range 0..10" }
    println(value)
}
```

> [!NOTE]
> You can play with sample projects under the [examples](examples).

## Available Annotations

See [Predefined Annotations](docs/predefined_annotations.md) for details.

## Define Custom Konditions (Planned)

### Combine predefined annotations

You can create a new kondition by combine existent annotations.

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
