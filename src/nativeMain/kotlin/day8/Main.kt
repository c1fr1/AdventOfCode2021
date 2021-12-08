package day8

import loadDay
import platform.posix.pow

fun main() {
	val data = loadDay(8)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val lines = data.split("\n").map { l -> l.trim().split("|")
		.map { s -> s.trim().split(" ")} }.filter { it.size > 1 }
	for (l in lines) {
		println(l)
	}
	return lines.sumOf { identifyNumbers(it[0], it[1])
		.sumOf { n -> if (n == 1 || n == 4 || n == 7 || n == 8) 1 else 0L} }
}

fun gold(data : String) : Long {
	val lines = data.split("\n").map { l -> l.trim().split("|")
		.map { s -> s.trim().split(" ")} }.filter { it.size > 1 }
	return lines.sumOf { l ->
		val nums = identifyNumbers(l[0], l[1])
		nums.indices.sumOf { nums.asReversed()[it] * pow(10.0, it.toDouble()).toLong() }
	}
}

fun identifyNumbers(digits : List<String>, signals : List<String>) : List<Int> {
	val dict = IntArray(10) {-1}
	dict[digits.indexOfFirst { it.length == 2 }] = 1
	println("got 1")
	dict[digits.indexOfFirst { it.length == 4 }] = 4
	println("got 4")
	dict[digits.indexOfFirst { it.length == 3 }] = 7
	println("got 7")
	dict[digits.indexOfFirst { it.length == 7 }] = 8
	println("got 8")
	dict[digits.indexOfFirst { it.length == 5 && it.count { c -> digits[dict.indexOf(7)].contains(c) } == 3 }] = 3
	println("got 3")
	dict[digits.indexOfFirst { it.length == 6 && it.count { c -> digits[dict.indexOf(3)].contains(c) } == 5 }] = 9
	println("got 9")
	dict[digits.indexOfFirst { it.length == 6 &&
			it.count { c -> digits[dict.indexOf(7)].contains(c) } == 3 && dict[digits.indexOf(it)] == -1 } ] = 0
	println("got 0")
	dict[digits.indexOfFirst { it.length == 6 && dict[digits.indexOf(it)] == -1} ] = 6
	println("got 6")
	dict[digits.indexOfFirst { it.length == 5 &&
			it.count { c -> digits[dict.indexOf(9)].contains(c) } == 5 && dict[digits.indexOf(it)] == -1} ] = 5
	println("got 5")
	dict[digits.indexOfFirst { dict[digits.indexOf(it)] == -1} ] = 2
	println("got 2")
	println(dict.joinToString { it.toString() })
	return signals.map { s -> dict[digits.indexOfFirst { d -> s.length == d.length && s.all { d.contains(it) } }] }
}

/*
0 : 6!
1 : 2!
2 : 5
3 : 5!
4 : 4!
5 : 5
6 : 6!
7 : 3!
8 : 7!
9 : 6!
 */