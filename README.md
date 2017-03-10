# scodec Cheatsheet

[scodec](http://scodec.org/) comes with a large number of predefined codecs
and combinators.

This list is a work-in-progress overview over the predefined codecs.

## Primitive Codecs

| Name       | Element Type | Description                                                                 | Min Bits | Max Bits | Example Value           | Example Encoding                                               |
|------------|--------------|-----------------------------------------------------------------------------|----------|----------|-------------------------|----------------------------------------------------------------|
| `bits`     | `BitVector`  | All the remaining bits                                                      | 0        | ∞        | `bin"11010110101"`      | `11010110101₂` (11 bits)                                       |
| `bytes`    | `ByteVector` | All the remaining bytes                                                     | 0        | ∞        | `hex"5468697320697320"` | `54 68 69 73 20 69 73 20₁₆` (8 bytes)                          |
| `byte`     | `Byte`       | A single byte                                                               | 8        | 8        | `89: Byte`              | `59₁₆` (1 byte)                                                |
| `ushort8`  | `Short`      | 8-bit unsigned short                                                        | 8        | 8        | `240: Short`            | `f0₁₆` (1 byte)                                                |
| `short16`  | `Short`      | 16-bit big-endian short                                                     | 16       | 16       | `0x1234: Short`         | `12 34₁₆` (2 bytes)                                            |
| `int8`     | `Int`        | 8-bit integer                                                               | 8        | 8        | `0x12`                  | `12₁₆` (1 byte)                                                |
| `int16`    | `Int`        | 16-bit big-endian integer                                                   | 16       | 16       | `0x1234`                | `12 34₁₆` (2 bytes)                                            |
| `int24`    | `Int`        | 24-bit big-endian integer                                                   | 24       | 24       | `0x123456`              | `12 34 56₁₆` (3 bytes)                                         |
| `int32`    | `Int`        | 32-bit big-endian integer                                                   | 32       | 32       | `0x12345678`            | `12 34 56 78₁₆` (4 bytes)                                      |
| `int64`    | `Long`       | 64-bit big-endian integer                                                   | 64       | 64       | `0x12345678abcdefL`     | `00 12 34 56 78 ab cd ef₁₆` (8 bytes)                          |
| `uint2`    | `Int`        | 2-bit unsigned integer                                                      | 2        | 2        | `0x3`                   | `11₂` (2 bits)                                                 |
| `uint4`    | `Int`        | 4-bit unsigned integer                                                      | 4        | 4        | `0x6`                   | `0110₂` (4 bits)                                               |
| `uint8`    | `Int`        | 8-bit unsigned integer                                                      | 8        | 8        | `0xad`                  | `ad₁₆` (1 byte)                                                |
| `uint16`   | `Int`        | 16-bit big-endian unsigned integer                                          | 16       | 16       | `0xadac`                | `ad ac₁₆` (2 bytes)                                            |
| `uint32`   | `Long`       | 32-bit big-endian unsigned integer                                          | 32       | 32       | `0x81234567L`           | `81 23 45 67₁₆` (4 bytes)                                      |
| `int8L`    | `Int`        | 8-bit little-endian integer                                                 | 8        | 8        | `0x12`                  | `12₁₆` (1 byte)                                                |
| `int16L`   | `Int`        | 16-bit little-endian integer                                                | 16       | 16       | `0x1234`                | `34 12₁₆` (2 bytes)                                            |
| `int24L`   | `Int`        | 24-bit little-endian integer                                                | 24       | 24       | `0x123456`              | `56 34 12₁₆` (3 bytes)                                         |
| `int32L`   | `Int`        | 32-bit little-endian integer                                                | 32       | 32       | `0x12345678`            | `78 56 34 12₁₆` (4 bytes)                                      |
| `int64L`   | `Long`       | 64-bit little-endian integer                                                | 64       | 64       | `0x12345678abcdefL`     | `ef cd ab 78 56 34 12 00₁₆` (8 bytes)                          |
| `uint2L`   | `Int`        | 2-bit little-endian unsigned integer                                        | 2        | 2        | `0x3`                   | `11₂` (2 bits)                                                 |
| `uint4L`   | `Int`        | 4-bit little-endian unsigned integer                                        | 4        | 4        | `0x6`                   | `0110₂` (4 bits)                                               |
| `uint8L`   | `Int`        | 8-bit little-endian unsigned integer                                        | 8        | 8        | `0xad`                  | `ad₁₆` (1 byte)                                                |
| `uint16L`  | `Int`        | 16-bit little-endian unsigned integer                                       | 16       | 16       | `0xadac`                | `ac ad₁₆` (2 bytes)                                            |
| `uint32L`  | `Long`       | 32-bit little-endian unsigned integer                                       | 32       | 32       | `0x81234567L`           | `67 45 23 81₁₆` (4 bytes)                                      |
| `vint`     | `Int`        | Variable-length big-endian integer                                          | 1        | 5        | `0x8123456`             | `d6 e8 c8 40₁₆` (4 bytes)                                      |
| `vintL`    | `Int`        | Variable-length little-endian integer                                       | 1        | 5        | `0x123456`              | `c8 e8 56₁₆` (3 bytes)                                         |
| `vlong`    | `Long`       | Variable-length big-endian long                                             | 1        | 9        | `0x81234567abcdL`       | `cd d7 9e ab b4 a4 20₁₆` (7 bytes)                             |
| `vlongL`   | `Long`       | Variable-length little-endian long                                          | 1        | 9        | `0x81234567abcdL`       | `a0 a4 b4 ab 9e d7 4d₁₆` (7 bytes)                             |
| `vpbcd`    | `Long`       | Variable-length packed decimal longs                                        | 1        | 9        | `12345678901L`          | `00010010001101000101011001111000100100000001₂` (44 bits)      |
| `float`    | `Float`      | 32-bit big-endian IEEE 754 floating point number                            | 32       | 32       | `1.234234387f`          | `3f 9d fb 64₁₆` (4 bytes)                                      |
| `floatL`   | `Float`      | 32-bit little-endian IEEE 754 floating point number                         | 32       | 32       | `1.234234387f`          | `64 fb 9d 3f₁₆` (4 bytes)                                      |
| `double`   | `Double`     | 64-bit big-endian IEEE 754 floating point number                            | 64       | 64       | `1.234234387d`          | `3f f3 bf 6c 8e 7c 37 bd₁₆` (8 bytes)                          |
| `doubleL`  | `Double`     | 64-bit little-endian IEEE 754 floating point number                         | 64       | 64       | `1.234234387d`          | `bd 37 7c 8e 6c bf f3 3f₁₆` (8 bytes)                          |
| `bool`     | `Boolean`    | 1-bit boolean                                                               | 1        | 1        | `true`                  | `1₂` (1 bit)                                                   |
| `ascii`    | `String`     | `US-ASCII` String consuming remaining input                                 | 0        | ∞        | `"The lazy dog"`        | `54 68 65 20 6c 61 7a 79 20 64 6f 67₁₆` (12 bytes)             |
| `utf8`     | `String`     | `UTF8` String consuming remaining input                                     | 0        | ∞        | `"The lazy dog"`        | `54 68 65 20 6c 61 7a 79 20 64 6f 67₁₆` (12 bytes)             |
| `cstring`  | `String`     | Null-terminated `US-ASCII` String                                           | 0        | ∞        | `"The lazy dog"`        | `54 68 65 20 6c 61 7a 79 20 64 6f 67 00₁₆` (13 bytes)          |
| `ascii32`  | `String`     | `US-ASCII` String prefixed by 32-bit 2s complement big-endian size field    | 32       | ∞        | `"The lazy dog"`        | `00 00 00 0c 54 68 65 20 6c 61 7a 79 20 64 6f 67₁₆` (16 bytes) |
| `ascii32L` | `String`     | `US-ASCII` String prefixed by 32-bit 2s complement little-endian size field | 32       | ∞        | `"The lazy dog"`        | `0c 00 00 00 54 68 65 20 6c 61 7a 79 20 64 6f 67₁₆` (16 bytes) |
| `utf8_32`  | `String`     | `UTF8` String prefixed by 32-bit 2s complement big-endian size field        | 32       | ∞        | `"The lazy dog"`        | `00 00 00 0c 54 68 65 20 6c 61 7a 79 20 64 6f 67₁₆` (16 bytes) |
| `utf8_32L` | `String`     | `UTF8` String prefixed by 32-bit 2s complement little-endian size field     | 32       | ∞        | `"The lazy dog"`        | `0c 00 00 00 54 68 65 20 6c 61 7a 79 20 64 6f 67₁₆` (16 bytes) |
| `uuid`     | `UUID`       | `UUID` as 2 64-bit big-endian longs                                         | 128      | 128      | `UUID.randomUUID()`     | `2b 2f 9b f4 53 04 42 ad b2 bb 77 e6 29 51 54 e1₁₆` (16 bytes) |

## Combinators

## Contributing

 * Run `sbt "test:run-main scodec.CheatSheetGenerator` to generate the table
 * Format the markdown using the http://markdowntable.com/

## License

This cheat sheet was original created by Johannes Rudolph. This document and the
accompanying source code is freely available under the [CC0 license](https://creativecommons.org/publicdomain/zero/1.0/).