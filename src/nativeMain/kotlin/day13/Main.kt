package day13

import loadDay
import loadExample

fun main() {
	val data = loadDay(13)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val segments = data.trim().split("\n\n").map { it.trim() }
	val dots = segments[0].split("\n").map {it.trim()}
		.map { Pair(it.substringBefore(',').toInt(), it.substringAfter(',').toInt()) }
	val folds = segments[1].split('\n').map {it.trim()}
		.map {Pair(it.substringBefore('=').last() == 'x', it.substringAfter('=').toInt())}
	return foldDots(dots, folds[0].first, folds[0].second).size.toLong()
}

fun gold(data : String) : Long {
	val segments = data.trim().split("\n\n").map { it.trim() }
	var dots = segments[0].split("\n").map {it.trim()}
		.map { Pair(it.substringBefore(',').toInt(), it.substringAfter(',').toInt()) }
	val folds = segments[1].split('\n').map {it.trim()}
		.map {Pair(it.substringBefore('=').last() == 'x', it.substringAfter('=').toInt())}
	for (fold in folds) dots = foldDots(dots, fold.first, fold.second)
	printDots(dots)
	return dots.size.toLong()
}

fun foldDots(dots : List<Pair<Int, Int>>, foldIsX : Boolean, coord : Int) : List<Pair<Int, Int>> {
	return getUnique(dots.map {
		if (foldIsX) {
			if (it.first > coord) Pair(2 * coord - it.first, it.second) else it
		} else {
			if (it.second > coord) Pair(it.first, 2 * coord - it.second) else it
		}
	})
}

fun getUnique(l : List<Pair<Int, Int>>) : List<Pair<Int, Int>> {
	val ret = ArrayList<Pair<Int, Int>>()
	for (pair in l) {
		if (ret.all { it.first != pair.first || it.second != pair.second }) ret.add(pair)
	}
	return ret
}

fun printDots(dots : List<Pair<Int, Int>>) {
	val width = dots.maxOf { it.first } + 1
	val height = dots.maxOf { it.second } + 1
	val table = Array(height) {y -> Array(width) {x -> dots.any {it.first == x && it.second == y}} }
	for (y in table.indices) {
		for (x in table[0].indices) {
			print(if (table[y][x]) "#" else " ")
		}
		println()
	}
}