# Value Fitting

Value fitting allows you to preprocess parameters before using them.
For example, if you annotated `value` with `@CoerceIn` like below, `value` will be fit into the range from 0 to 10.

```kotlin
fun myFunction(@CoerceIn(0, 10) value: Int) {
    println(value) // only 0..10 value will be printed out
}
```

## Supported Annotations

For Number:

|                           Annotation                           | Preprocess                                   |
|:--------------------------------------------------------------:|:---------------------------------------------|
|      `@CoerceIn(minimumValue: Long, maximumValue: Long)`       | `value.coerceIn(minimumValue, maximumValue)` |
|              `@CoerceAtMost(maximumValue: Long)`               | `value.coerceAtMost(maximumValue)`           |
|              `@CoerceAtLeast(minimumValue: Long)`              | `value.coerceAtLeast(minimumValue)`          |
| `@CoerceInDecimal(minimumValue: Double, maximumValue: Double)` | `value.coerceIn(minimumValue, maximumValue)` |
|          `@CoerceAtMostDecimal(maximumValue: Double)`          | `value.coerceAtMost(maximumValue)`           |
|         `@CoerceAtLeastDecimal(mimimumValue: Double)`          | `value.coerceAtLeast(minimumValue)`          |

For String:

|           Annotation            | Preprocess                   |
|:-------------------------------:|:-----------------------------|
|             `@Trim`             | `value.trim()`               |
|          `@TrimStart`           | `value.trimStart()`          |
|           `@TrimEnd`            | `value.trimEnd()`            |
|  `@AddPrefix(prefix: String)`   | `"$prefix$value"`            |
| `@RemovePrefix(prefix: String)` | `value.removePrefix(prefix)` |
|  `@AddSuffix(prefix: String)`   | `"$value$suffix"`            |
| `@RemoveSuffix(prefix: String)` | `value.removeSuffix(suffix)` |
