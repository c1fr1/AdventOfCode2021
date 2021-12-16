package day15

import loadDay
import loadExample

lateinit var bestCosts : Array<Array<Int>>

fun main() {
	val data = loadDay(15)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val tile = data.trim().split("\n").map { it.map { c -> "$c".toInt() } }
	val board = Array(tile.size) {y -> Array(tile[0].size) {x -> tile[y][x]} }
	return solveBoard(board)
}

fun gold(data : String) : Long {
	val tile = data.trim().split("\n").map { it.map { c -> "$c".toInt() } }
	val board = Array(tile.size * 5) {y -> Array(tile[0].size * 5) {x ->
		val tileX = x % tile[0].size
		val tileY = y % tile.size
		val tileInc = x / tile[0].size + y / tile.size
		(tileInc + tile[tileY][tileX] - 1) % 9 + 1
	} }
	return solveBoard(board)
}

fun solveBoard(board : Array<Array<Int>>) : Long {
	val targx = board[0].size - 1
	val targy = board.size - 1
	val nodes = arrayListOf(Node(targx + targy, 0, 0, 0, -1, -1))
	bestCosts = Array(board.size) { Array(board[0].size) {9999} }

	while (nodes[0].x != targx || nodes[0].y != targy) {
		val oldNode = nodes.removeFirst()
		for (n in oldNode.neighbors(board)) {
			nodes.addNode(n)
		}
	}
	return nodes[0].accCost.toLong()
}

class Node(val hCost : Int, val accCost : Int, val x : Int, val y : Int, val disallowX : Int, val disallowY: Int) {
	fun neighbors(board: Array<Array<Int>>): List<Node> {
		return arrayListOf(
			Pair(x + 1, y),
			Pair(x - 1, y),
			Pair(x, y + 1),
			Pair(x, y - 1),
		).filter { it.first != disallowX || it.second != disallowY }
			.filter { it.first in board[0].indices && it.second in board.indices }
			.filter { bestCosts[it.second][it.first] > accCost }
			.map {
				val nAccCost = accCost + board[it.second][it.first]
				val nHCost = 1 * (board.size - 1 + board.size - 1 - it.first - it.second) + nAccCost
				bestCosts[it.second][it.first] = accCost
				Node(nHCost, nAccCost, it.first, it.second, x, y)
			}
	}
}

typealias SortedQueue = ArrayList<Node>

fun SortedQueue.addNode(n : Node) {
	var i = 0
	while (i < size) {
		if (n.hCost < this[i].hCost) {
			add(i, n)
			return
		}
		++i
	}
	add(n)
}