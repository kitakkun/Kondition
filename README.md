# Kondition

Kondition makes sure that your Kotlin code runs under certain conditions.
Functions or class constructors are invoked after checking specified conditions are met.

> [!IMPORTANT]
> 🚧 THIS PROJECT IS STILL A WORK IN PROGRESS 🚧

## Installation

WIP

## Usage

For example:

```kotlin
fun hoge(@RangedInt(0, 10) val value) {
    println(value)
}
```

will be transformed to:

```kotlin
fun hoge(@RangedInt(0, 10) val value) {
    require(value in 0..10) { "the parameter \"value\" has to be in the range from 0 to 10." }
    println(value)
}
```

## Available Annotations

See [Predefined Annotations](docs/predefined_annotations.md) for details.

## Define Custom Konditions (Planned)

### Combine predefined annotations

You can create a new kondition by combine existent annotations.

```kotlin
@Combine(with = CombineRule.AND)
@RangedInt(0, 10)
@RangedInt(5, 10)
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
