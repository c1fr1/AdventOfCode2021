package day7

import loadDay
import platform.posix.abs

fun main() {
	val data = loadDay(7)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val positions = data.trim().split(",").mapNotNull { it.toIntOrNull() }
	val min = positions.minOf { it }
	val max = positions.maxOf { it }
	return (min..max).minOf { x -> positions.sumOf { abs(x - it).toLong() } }
}

fun gold(data : String) : Long {
	val positions = data.trim().split(",").mapNotNull { it.toIntOrNull() }
	val min = positions.minOf { it }
	val max = positions.maxOf { it }
	return (min..max).minOf { x -> positions.sumOf { abs(x - it) * (abs(x - it) + 1)/2L } }
}
