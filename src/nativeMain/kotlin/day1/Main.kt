package day1

import loadDay

fun main() {
	val data = loadDay(1)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Int {
	val readings = data.split('\n').mapNotNull { it.toLongOrNull() }
	var prev = Long.MAX_VALUE
	var ret = 0
	for (reading in readings) {
		if (reading > prev) ret++
		prev = reading
	}
	return ret
}

fun gold(data : String) : Int {
	val readings = data.split('\n').mapNotNull { it.toLongOrNull() }

	val expectedQueueLen = 3
	val queue = ArrayList<Long>()

	var ret = 0

	for (reading in readings) {
		if (expectedQueueLen == queue.size) {
			val prevSum = queue.sum()
			queue.removeLast()
			queue.add(0, reading)
			val newSum = queue.sum()
			if (newSum > prevSum) ret++
		} else queue.add(0, reading)
	}
	return ret
}