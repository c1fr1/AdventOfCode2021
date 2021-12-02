package day2

import loadDay

fun main() {
	val data = loadDay(2)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val commands = data.split("\n").mapNotNull { s -> Command(s) }
	var horizontal = 0L
	var depth = 0L
	for (command in commands) {
		when (command.direction) {
			Direction.FORWARD -> horizontal += command.distance
			Direction.DOWN -> depth += command.distance
			Direction.UP -> depth -= command.distance
		}
	}
	return horizontal * depth
}

fun gold(data : String) : Long {
	val commands = data.split("\n").mapNotNull { s -> Command(s) }
	var horizontal = 0L
	var depth = 0L
	var aim = 0L
	for (command in commands) {
		when (command.direction) {
			Direction.FORWARD -> {
				horizontal += command.distance
				depth += aim * command.distance
			}
			Direction.DOWN -> aim += command.distance
			Direction.UP -> aim -= command.distance
		}
	}
	return horizontal * depth
}

enum class Direction {
	FORWARD,
	DOWN,
	UP;

	companion object {
		operator fun invoke(s : String) : Direction? {
			return when (s) {
				"forward" -> FORWARD
				"down" -> DOWN
				"up" -> UP
				else -> null
			}
		}
	}
}

class Command(val direction : Direction, val distance : Long) {
	override fun toString(): String {
		return "$direction $distance"
	}
	companion object {
		operator fun invoke(s : String) : Command? {
			val tokens = s.split(" ")
			return Command(Direction(tokens[0]) ?: return null, tokens[1].toLongOrNull() ?: return null)
		}
	}
}