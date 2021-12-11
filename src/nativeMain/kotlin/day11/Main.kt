package day11

import loadDay
import loadExample

fun main() {
	val data = loadDay(11)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val board = data.trim().split("\n").map { it.trim().map { n -> n.toString().toInt() }.toIntArray() }
		.toTypedArray()
	return Array(100) {step(board)}.sum().toLong()
}

fun gold(data : String) : Long {
	val board = data.trim().split("\n").map { it.trim().map { n -> n.toString().toInt() }.toIntArray() }
		.toTypedArray()
	val targ = board.size * board[0].size
	var step = 0L
	while (true) {
		++step
		if (step(board) == targ) {
			return step
		}
	}
}

fun step(board : Array<IntArray>) : Int {
	for (by in board.indices) for (bx in board[by].indices) {
		++board[by][bx]
	}

	var ret = 0
	var oldRet = -1
	while (oldRet < ret) {
		oldRet = ret
		for (by in board.indices) for (bx in board[by].indices) if (board[by][bx] > 9) {
			++ret
			board[by][bx] = -1
			for (x in (bx - 1)..(bx + 1)) for (y in (by - 1)..(by + 1)) {
				if (y in board.indices && x in board[0].indices) if (board[y][x] >= 0) {
					++board[y][x]
				}
			}
		}
	}
	for (by in board.indices) for (bx in board[by].indices) if (board[by][bx] < 0) {
		board[by][bx] = 0
	}
	return ret
}