package day21

import loadDay

fun main() {
	val data = loadDay(21)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val startingPoses = data.trim().split('\n').map { it.substringAfter(':').trim().toInt() }.toTypedArray()
	val scores = arrayOf(0, 0)
	var firstRoll = 1
	var timesRolled = 0

	val termScore = 1000

	while (scores.all { it < termScore }) {
		repeat(startingPoses.size) {
			if (scores[0] < termScore) {
				val rollRes = roll(firstRoll)
				firstRoll = rollRes.first
				startingPoses[it] = (startingPoses[it] - 1 + rollRes.second) % 10 + 1
				scores[it] += startingPoses[it]
				timesRolled += 3
			}
		}
	}
	return scores.first {it < termScore} * timesRolled.toLong()
}

fun gold(data : String) : Long {
	val startingPoses = data.trim().split('\n').map { it.substringAfter(':').trim().toInt() }.toTypedArray()
	val wins = findWinningCombos(0, 0, startingPoses[0], startingPoses[1])
	return wins.first.coerceAtLeast(wins.second)
}

fun findWinningCombos(aScore : Int, bScore : Int, aPos : Int, bPos : Int) : Pair<Long, Long> {
	var accA = 0L
	var accB = 0L

	val possibleRollSums = arrayOf(
		Pair(9, 1),
		Pair(8, 3),
		Pair(7, 6),
		Pair(6, 7),
		Pair(5, 6),
		Pair(4, 3),
		Pair(3, 1),
	)

	for (ars in possibleRollSums) {
		val nposA = (aPos + ars.first - 1) % 10 + 1
		if (aScore + nposA >= 21) {
			accA += ars.second
		} else {
			for (brs in possibleRollSums) {
				val nposB = (bPos + brs.first - 1) % 10 + 1
				if (bScore + nposB >= 21) {
					accB += brs.second * ars.second
				} else {
					val wins = findWinningCombos(aScore + nposA, bScore + nposB, nposA, nposB)
					accA += wins.first * brs.second * ars.second
					accB += wins.second * brs.second * ars.second
				}
			}
		}
	}
	return Pair(accA, accB)
}

fun roll(state : Int) : Pair<Int, Int> {
	val totalMove = if (state > 98) {
		state + (state % 100 + 1) + ((state + 1) % 100 + 1)
	} else {
		3 * (state + 1)
	}
	return Pair((state + 2) % 100 + 1, totalMove)
}
//3 = 1
//4 = 3
//5 = 6
//6 = 7
//7 = 6
//8 = 3
//9 = 1