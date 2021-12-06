package day6

import loadDay

const val MAX_DAY = 8
const val REPRODUCTION_INTERVAL = 6

fun main() {
	val data = loadDay(6)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	return simulateCycles(data, 80)
}

fun gold(data : String) : Long {
	return simulateCycles(data, 256)
}

fun simulateCycles(data : String, cycles : Int) : Long {
	val timers = data.split(",").mapNotNull { it.trim().toIntOrNull() }
	val timerCounts = LongArray(MAX_DAY + 1) { i -> timers.count {it == i}.toLong()}

	repeat(cycles) {
		val newFishCount = timerCounts[0]
		for (i in 0 until MAX_DAY) {
			timerCounts[i] = timerCounts[i + 1]
		}
		timerCounts[REPRODUCTION_INTERVAL] += newFishCount
		timerCounts[MAX_DAY] = newFishCount
	}

	return timerCounts.sum()
}