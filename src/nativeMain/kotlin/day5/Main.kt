package day5

import loadDay
import loadExample
import platform.posix.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

fun main() {
	val data = loadDay(5)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Int {
	val lines = data.trim().split("\n").map { Line(it) }

	val buffer = Array(999) { Array(999) {0} }

	for (line in lines) {
		if (line.start.x == line.end.x) {
			val min = min(line.start.y, line.end.y)
			val max = max(line.start.y, line.end.y)
			for (y in min..max) {
				buffer[line.start.x][y] += 1
			}
		} else if (line.start.y == line.end.y) {
			val min = min(line.start.x, line.end.x)
			val max = max(line.start.x, line.end.x)
			for (x in min..max) {
				buffer[x][line.start.y] += 1
			}
		}
	}

	return buffer.sumOf { col -> col.count { it > 1 } }
}

fun gold(data : String) : Int {
	val lines = data.trim().split("\n").map { Line(it) }

	val buffer = Array(1000) { Array(1000) {0} }

	for (line in lines) {
		if (line.start.x == line.end.x) {
			val min = min(line.start.y, line.end.y)
			val max = max(line.start.y, line.end.y)
			for (y in min..max) {
				buffer[line.start.x][y] += 1
			}
		} else if (line.start.y == line.end.y) {
			val min = min(line.start.x, line.end.x)
			val max = max(line.start.x, line.end.x)
			for (x in min..max) {
				buffer[x][line.start.y] += 1
			}
		} else {
			val xsign = (line.end.x - line.start.x).sign
			val ysign = (line.end.y - line.start.y).sign
			for (inc in 0..abs(line.start.x - line.end.x)) {
				buffer[line.start.x + inc * xsign][line.start.y + inc * ysign] += 1
			}
		}
	}

	return buffer.sumOf { col -> col.count { it > 1 } }
}

class Coordinate(val x : Int, val y : Int) {
	override fun equals(other : Any?) : Boolean {
		return when (other) {
			is Coordinate -> other.x == x && other.y == y
			else -> super.equals(other)
		}
	}

	companion object {
		operator fun invoke(s : String) : Coordinate {
			val xy = s.split(",").map { it.toInt() }
			return Coordinate(xy[0], xy[1])
		}
	}
}

class Line(val start : Coordinate, val end : Coordinate) {
	companion object {
		operator fun invoke(s : String) : Line {
			val positions = s.split(" -> ").map { Coordinate(it) }
			return Line(positions[0], positions[1])
		}
	}
}
//1322