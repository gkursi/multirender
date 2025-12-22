package multirender.wm.util

fun <T> List<T>.lastOrNull(): T? {
    if (isEmpty())
        return null
    return this[lastIndex]
}

fun <T> List<T>.reverseFirstOrNull(predicate: (T) -> Boolean): T? {
    for (i in lastIndex downTo 0) {
        val el = this[i]
        if (predicate(el)) return el
    }
    return null
}