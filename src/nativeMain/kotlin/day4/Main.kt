package day4

import loadDay

fun main() {
	val data = loadDay(4)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val segments = data.split("\n\n")
	val numbers = segments[0].split(",").mapNotNull { it.toLongOrNull() }
	val boards = segments.map { Board(it) }.filter { it.tiles.isNotEmpty() }

	for (n in numbers) {
		val score = boards.mapNotNull { it.callNum(n) }.maxOrNull()
		if (score != null) {
			return score
		}
	}
	return -1
}

fun gold(data : String) : Long {
	val segments = data.split("\n\n")
	val numbers = segments[0].split(",").mapNotNull { it.toLongOrNull() }
	var boards = segments.map { Board(it) }.filter { it.tiles.isNotEmpty() }
	var i = 0
	while (boards.isNotEmpty()) {
		val scores = boards.map { it.callNum(numbers[i]) }
		boards = boards.filterIndexed { j, _ -> scores[j] == null}
		if (boards.isEmpty()) return scores.mapNotNull { it }.minOrNull() ?: -1
		++i
	}
	return -1
}

class Tile(val number : Long, var marked : Boolean = false)

class Board(s : String) {
	val tiles = s.split("\n")
		.map { row -> row.split(" ").mapNotNull { tile -> tile.toLongOrNull()?.let { Tile(it) } } }
		.filter { it.isNotEmpty() }

	operator fun get(x : Int, y : Int) = tiles[y][x]

	fun checkRow(y : Int) = tiles[y].all { it.marked }

	fun checkCol(x : Int) = tiles.map { it[x] }.all { it.marked }

	fun calculateScore(lastNum : Long) =
		tiles.sumOf { row -> row.sumOf { tile -> if (tile.marked) 0 else tile.number } } * lastNum

	fun callNum(num : Long) : Long? {
		for (y in tiles.indices) {
			for (x in tiles[y].indices) {
				if (tiles[y][x].number == num) {
					tiles[y][x].marked = true
					if (checkRow(y) || checkCol(x)) {
						return calculateScore(num)
					}
					return null
				}
			}
		}
		return null
	}
}