# Predefined Annotations

## Number

`Number`: `Int`, `Long`, `Short`, `Byte`, `Float`, `Double`

|          Annotation           | Will check                                |     Supported      |
|:-----------------------------:|:------------------------------------------|:------------------:|
|          `@NonZero`           | `value != 0`                              | :white_check_mark: |
|          `@Positive`          | `value > 0`                               | :white_check_mark: |
|          `@Negative`          | `value < 0`                               | :white_check_mark: |
|        `@NonPositive`         | `value <= 0`                              | :white_check_mark: |
|        `@NonNegative`         | `value >= 0`                              | :white_check_mark: |
|        `@GreaterThan`         | `value  > threshold`                      | :white_check_mark: |
|    `@GreaterThanOrEquals`     | `value >= threshold`                      | :white_check_mark: |
|          `@LessThan`          | `value < threshold`                       | :white_check_mark: |
|      `@LessThanOrEquals`      | `value <= threshold`                      | :white_check_mark: |
|           `@Ranged`           | `value >(=) start && value <(=) end `     | :white_check_mark: |
|     `@GreaterThanDecimal`     | decimal version of `@GreaterThan`         | :white_check_mark: |
| `@GreaterThanOrEqualsDecimal` | decimal version of `@GreaterThanOrEquals` | :white_check_mark: |
|      `@LessThanDecimal`       | decimal version of `@LessThan`            | :white_check_mark: |
|  `@LessThanOrEqualsDecimal`   | decimal version of `@LessThanOrEquals`    | :white_check_mark: |
|       `@RangedDecimal`        | decimal version of `@Ranged`              | :white_check_mark: |

## String

|  Annotation   | Will check                                 |     Supported      |
|:-------------:|:-------------------------------------------|:------------------:|
|  `@NonEmpty`  | `value != ""`                              | :white_check_mark: |
|  `@NonBlank`  | `value != "" or value != " "`              | :white_check_mark: |
|   `@Regex`    | `Regex(pattern).matches(value) == true`    | :white_check_mark: |
| `@Alphabetic` | `value` contains only alphabet characters. | :white_check_mark: |
|  `@Numeric`   | `value` contains only numeric characters.  | :white_check_mark: |
|   `@Length`   | `value.length = length`                    | :white_check_mark: |
| `@MinLength`  | `value.length >= length`                   | :white_check_mark: |
| `@MaxLength`  | `value.length <= length`                   | :white_check_mark: |
| `@UpperCased` | `value.upppercase() == value`              | :white_check_mark: |
| `@LowerCased` | `value.lowercase() == value`               | :white_check_mark: |
|  `@Prefixed`  | `value.startsWith(prefix)`                 | :white_check_mark: |
|  `@Suffixed`  | `value.endsWith(suffix)`                   | :white_check_mark: |

## Char

|  Annotation   | Will check                        |     Supported      |
|:-------------:|:----------------------------------|:------------------:|
| `@UpperCased` | `value.upppercaseChar() == value` | :white_check_mark: |
| `@LowerCased` | `value.lowercaseChar() == value`  | :white_check_mark: |
