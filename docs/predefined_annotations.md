# Predefined Annotations

## Number

`Number`: `Int`, `Long`, `Short`, `Byte`, `Float`, `Double`

|       Annotation       | Will check                            |     Supported      |
|:----------------------:|:--------------------------------------|:------------------:|
|       `@NonZero`       | `value != 0`                          | :white_check_mark: |
|      `@Positive`       | `value > 0`                           | :white_check_mark: |
|      `@Negative`       | `value < 0`                           | :white_check_mark: |
|     `@NonPositive`     | `value <= 0`                          | :white_check_mark: |
|     `@NonNegative`     | `value >= 0`                          | :white_check_mark: |
|     `@GreaterThan`     | `value  > threshold`                  |         x          |
| `@GreaterThanOrEquals` | `value >= threshold`                  |         x          |
|      `@LessThan`       | `value < threshold`                   |         x          |
|  `@LessThanOrEquals`   | `value <= threshold`                  |         x          |
|       `@Ranged`        | `value >(=) start && value <(=) end ` | :white_check_mark: |
|    `@RangedDecimal`    | decimal range version of the above    | :white_check_mark: |

## String

|  Annotation   | Will check                                 |     Supported      |
|:-------------:|:-------------------------------------------|:------------------:|
|  `@NonEmpty`  | `value != ""`                              | :white_check_mark: |
|  `@NonBlank`  | `value != "" or value != " "`              | :white_check_mark: |
|   `@Regex`    | `Regex(pattern).matches(value) == true`    | :white_check_mark: |
| `@Alphabetic` | `value` contains only alphabet characters. | :white_check_mark: |
|  `@Numeric`   | `value` contains only numeric characters.  | :white_check_mark: |
|   `@Length`   | `value.length = length`                    |         x          |
| `@MinLength`  | `value.length >= length`                   |         x          |
| `@MaxLength`  | `value.length <= length`                   |         x          |
