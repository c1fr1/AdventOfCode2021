package day9

import loadDay

fun main() {
	val data = loadDay(9)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val board = data.trim().split("\n").map { r -> r.trim().map { it.toString().toInt() } }

	var ret = 0L

	for (y in board.indices) {
		val row = board[y]
		for (x in row.indices) {
			var neighbors = listOf(Pair(x + 1, y), Pair(x - 1, y), Pair(x, y + 1), Pair(x, y - 1))
			val cellVal = row[x]
			neighbors = neighbors.filter { it.first in row.indices && it.second in board.indices }
			if (neighbors.all { board[it.second][it.first] > cellVal}) ret += cellVal + 1
		}
	}

	return ret
}

fun gold(data : String) : Int {
	val board = data.trim().split("\n").map { r -> r.trim().map { it.toString().toInt() } }

	val lowPoints = ArrayList<Pair<Int, Int>>()

	for (y in board.indices) {
		val row = board[y]
		for (x in row.indices) {
			var neighbors = listOf(Pair(x + 1, y), Pair(x - 1, y), Pair(x, y + 1), Pair(x, y - 1))
			val cellVal = row[x]
			neighbors = neighbors.filter { it.first in row.indices && it.second in board.indices }
			if (neighbors.all { board[it.second][it.first] > cellVal }) lowPoints.add(Pair(x, y))
		}
	}

	val basins = ArrayList<Basin>()

	for (lp in lowPoints) {
		if (basins.all { it.all { bp -> bp.first != lp.first || bp.second != lp.second } }) {
			basins.add(findBasin(lp, board))
		}
	}

	basins.sortByDescending { it.size }

	return basins[0].size * basins[1].size * basins[2].size
}
typealias Basin = List<Pair<Int, Int>>

fun findBasin(start : Pair<Int, Int>, board : List<List<Int>>) : Basin {
	val ret = arrayListOf(start)
	var prevSize = 0
	while (ret.size > prevSize) {
		prevSize = ret.size
		for (loc in ret) {
			val x = loc.first
			val y = loc.second
			var neighbors = listOf(Pair(x + 1, y), Pair(x - 1, y), Pair(x, y + 1), Pair(x, y - 1))
			neighbors = neighbors.filter { it.first in board[0].indices && it.second in board.indices }
			neighbors = neighbors.filter { ret.all { bloc -> it.first != bloc.first || it.second != bloc.second } }
			neighbors = neighbors.filter { board[it.second][it.first] != 9 }
			ret.addAll(neighbors)
		}
	}
	return ret
}
