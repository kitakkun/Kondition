# Behaviors on Fail

When checking conditions failed, the method will throw an `IllegalArgumentException` by default.
However, you can specify the behavior of the method by specifying the `@AbortWith(AbortStrategy)` annotation.

```kotlin
enum class AbortWith {
    Return, // only for the functions which return Unit
    ReturnWithNull, // only for the functions which return nullable value
    ThrowError,
}
```

## Return strategy

WIP

## ReturnWithNull strategy

WIP

## ThrowError(Default) strategy

WIP
