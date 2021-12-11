package day10

import loadDay

fun main() {
	val data = loadDay(10)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val lines = data.trim().split("\n").map { it.trim() }
	return lines.sumOf { l -> corruptionScore(l) ?: 0 }
}

fun gold(data : String) : Long {
	val scores = data.trim().split("\n").map { it.trim() }.filter {
		corruptionScore(it) == null
	}.map { l ->
		getIncomplete(l).fold(0L) { inc, c -> inc * 5 + getCompScore(c) }
	}.sortedBy { it }
	return scores[scores.size / 2]
}

fun isOpen(c : Char) : Boolean {
	return when (c) {
		'(' -> true
		'[' -> true
		'{' -> true
		'<' -> true
		else -> false
	}
}

fun getOpposing(c : Char) : Char {
	return when (c) {
		'(' -> ')'
		'[' -> ']'
		'{' -> '}'
		'<' -> '>'
		else -> 'F'
	}
}

fun getScore(c : Char) : Long {
	return when (c) {
		')' -> 3
		']' -> 57
		'}' -> 1197
		'>' -> 25137
		else -> 0
	}
}

fun getCompScore(c : Char) : Long {
	return when (c) {
		')' -> 1
		']' -> 2
		'}' -> 3
		'>' -> 4
		else -> 0
	}
}

fun corruptionScore(s : String) : Long? {
	val expected = ArrayList<Char>()
	for (char in s) {
		if (isOpen(char)) expected.add(0, getOpposing(char)) else {
			if (expected[0] == char) expected.removeFirst() else return getScore(char)
		}
	}
	return null
}

fun getIncomplete(s : String) : ArrayList<Char> {
	val expected = ArrayList<Char>()
	for (char in s) {
		if (isOpen(char)) expected.add(0, getOpposing(char)) else {
			if (expected[0] == char) expected.removeFirst() else return ArrayList()
		}
	}
	return expected
}