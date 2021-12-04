package day3

import loadDay

fun main() {
	val data = loadDay(3)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val numbList = data.split("\n").map { line -> line.map { it == '1' } }
	val numberBits : Int = numbList.first().size
	val filter = (1L shl numberBits) - 1
	val countPos = Array(numberBits) {0}
	for (numb in numbList) {
		for (i in numb.indices) {
			if (numb[i]) countPos[i]++ else countPos[i]--
		}
	}
	var gamma = 0L
	for (i in countPos.indices) {
		if (countPos[i] > 0) gamma += 1 shl (numberBits - i - 1)
	}
	val epsilon = gamma xor filter
	return gamma * epsilon
}

fun gold(data : String) : Long {
	val numbList = data.split("\n").map { line -> line.map { it == '1' } }.filter { it.isNotEmpty() }
	val firstMode = numbList.count { it.first() } * 2 >= numbList.size
	var oxygenList = numbList.filter { it.first() == firstMode }
	var scrubberList = numbList.filter { it.first() != firstMode }

	var i = 1
	while (oxygenList.size > 1) {
		val mode = oxygenList.count { it[i] } * 2 >= oxygenList.size
		oxygenList = oxygenList.filter { it[i] == mode }
		++i
	}
	i = 1
	while (scrubberList.size > 1) {
		val mode = scrubberList.count { it[i] } * 2 >= scrubberList.size
		scrubberList = scrubberList.filter { it[i] != mode }
		++i
	}

	val oxygenVal = oxygenList.first()
	val scrubberVal = scrubberList.first()

	var oxygenNumb = 0L
	var scrubberNumb = 0L

	for (j in oxygenList.first().indices) {
		if (oxygenVal[j]) oxygenNumb += 1 shl (oxygenVal.size - j - 1)
		if (scrubberVal[j]) scrubberNumb += 1 shl (oxygenVal.size - j - 1)
	}

	return oxygenNumb * scrubberNumb
}
