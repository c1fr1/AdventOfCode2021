package day20

import loadDay
import loadExample

fun main() {
	val data = loadDay(20)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val splits = data.trim().split("\n\n").map{it.trim()}
	val indexes = splits[0].map { it == '#' }
	var map = splits[1].split('\n').map { it.map{ c -> c == '#' }.toTypedArray() }.toTypedArray()
	map = copyToLarger(map, 10)
	repeat(2) {map = iterate(map, indexes)}
	printMap(map)
	map = cropOut(map, 2)
	return map.sumOf { it.count { b -> b} }.toLong()
}
//5560

fun gold(data : String) : Long {
	val splits = data.trim().split("\n\n").map{it.trim()}
	val indexes = splits[0].map { it == '#' }
	var map = splits[1].split('\n').map { it.map{ c -> c == '#' }.toTypedArray() }.toTypedArray()
	map = copyToLarger(map, 100)
	repeat(50) {
		println(it)
		map = iterate(map, indexes)
	}
	map = cropOut(map, 100)
	return map.sumOf { it.count { b -> b} }.toLong()
}
//17548
//19825

fun copyToLarger(map : Array<Array<Boolean>>, by : Int) : Array<Array<Boolean>> {
	val out = Array(map.size + 2 * by) {y -> Array(map[0].size + 2 * by) {x ->
		if (y - by in map.indices && x - by in map[0].indices) {
			map[y - by][x - by]
		} else {
			false
		}
	} }
	return out
}

fun cropOut(map : Array<Array<Boolean>>, by : Int) : Array<Array<Boolean>> {
	val out = Array(map.size - 2 * by) {y -> Array(map[0].size - 2 * by) {x ->
		if (y + by in map.indices && x + by in map[0].indices) {
			map[y + by][x + by]
		} else {
			false
		}
	} }
	return out
}

fun iterate(map : Array<Array<Boolean>>, indexes : List<Boolean>) : Array<Array<Boolean>> {
	val out = Array(map.size + 2) {Array(map[0].size + 2) {false} }

	for (row in out.indices) {
		for (col in out[row].indices) {
			val num = Array(9) {false}
			for (dr in -1..1) {
				if (row + dr - 1 in map.indices) {
					for (dc in -1..1) {
						if (col + dc - 1 in map[row + dr - 1].indices) {
							num[3 * (dr + 1) + dc + 1] = map[row + dr - 1][col + dc - 1]
						}
					}
				}
			}
			out[row][col] = indexes[toNum(num)]
		}
	}
	return out
}

fun printMap(map : Array<Array<Boolean>>) {
	for (row in map) {
		for (col in row) {
			print(if (col) '#' else '.')
		}
		println()
	}
	println()
}

fun toNum(bits : Array<Boolean>) : Int {
	var ret = 0
	for (i in bits.indices) {
		if (bits[i]) ret += 1 shl (bits.size - 1 - i)
	}
	return ret
}