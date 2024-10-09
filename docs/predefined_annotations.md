# Predefined Annotations

## Number

`Number`: `Int`, `Long`, `Short`, `Byte`, `Float`, `Double`

|       Annotation       | Will check                            | Supported |
|:----------------------:|:--------------------------------------|:---------:|
|       `@NonZero`       | `value != 0`                          |     x     |
|      `@Positive`       | `value > 0`                           |     x     |
|      `@Negative`       | `value < 0`                           |     x     |
|     `@NonPositive`     | `value <= 0`                          |     x     |
|     `@NonNegative`     | `value >= 0`                          |     x     |
|     `@GreaterThan`     | `value  > threshold`                  |     x     |
| `@GreaterThanOrEquals` | `value >= threshold`                  |     x     |
|      `@LessThan`       | `value < threshold`                   |     x     |
|  `@LessThanOrEquals`   | `value <= threshold`                  |     x     |
|      `@RangedInt`      | `value >(=) start && value <(=) end ` |     x     |
|     `@RangedLong`      | same as above for `Long`              |     x     |
|     `@RangedShort`     | same as above for `Short`             |     x     |
|     `@RangedByte`      | same as above for `Byte`              |     x     |

## String

|  Annotation   | Will check                                 | Supported |
|:-------------:|:-------------------------------------------|:---------:|
|  `@NonEmpty`  | `value != ""`                              |     x     |
|  `@NonBlank`  | `value != "" or value != " "`              |     x     |
|   `@Regex`    | `Regex(pattern).matches(value) == true`    |     x     |
| `@Alphabetic` | `value` contains only alphabet characters. |     x     |
|  `@Numeric`   | `value` contains only numeric characters.  |     x     |
|   `@Length`   | `value.length = length`                    |     x     |
| `@MinLength`  | `value.length >= length`                   |     x     |
| `@MaxLength`  | `value.length <= length`                   |     x     |
