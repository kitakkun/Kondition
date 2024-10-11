import io.github.kitakkun.kondition.core.annotation.Ranged

fun main() {
    playWithInt(2) // OK
    try {
        playWithInt(-1) // NG
    } catch (e: Throwable) {
        e.printStackTrace()
    }
}

fun playWithInt(@Ranged(0, 5) value: Int) {
    println(value)
}
