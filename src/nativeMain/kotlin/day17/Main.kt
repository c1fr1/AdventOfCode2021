package day17

import loadDay

fun main() {
	val data = loadDay(17)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val nums = data.substringAfter(":").trim().split(", ")
		.map { it.substringAfter("=").split("..").map {n -> n.toInt()} }
	//val xRange = nums[0][0]..nums[0][1]
	val yRange = nums[1][0]..nums[1][1]

	var retVel = yRange.maxOf { it }
	var ret = retVel
	while (true) {
		var currentY = 0
		var currentVel = retVel
		var maxY = 0
		while (currentY > yRange.maxOf { it } || currentVel > 0) {
			currentY += currentVel
			currentVel -= 1
			maxY = maxY.coerceAtLeast(currentY)
			if (currentY in yRange) {
				break
			}
		}
		if (currentY !in yRange && retVel > 500) {
			break
		}
		retVel++
		if (currentY in yRange) {
			ret = ret.coerceAtLeast(maxY)
		}
	}

	return ret.toLong()
}

fun gold(data : String) : Long {
	val nums = data.substringAfter(":").trim().split(", ")
		.map { it.substringAfter("=").split("..").map {n -> n.toInt()} }
	val xRange = nums[0][0]..nums[0][1]
	val yRange = nums[1][0]..nums[1][1]

	var ret = 0
	for (xv in 22..xRange.maxOf { it }) {
		println(xv)
		for (yv in yRange.minOf { it }..100) {
			if (eventuallyReaches(xRange, yRange, xv, yv)) {
				ret++
				//println("($xv, $yv)")
			}
		}
	}
	return ret.toLong()
}
//50520

fun eventuallyReaches(xRange : IntRange, yRange : IntRange, xv : Int, yv : Int) : Boolean {
	var currentX = 0
	var currentY = 0
	var currentXV = xv
	var currentYV = yv

	while (currentY > yRange.minOf { it }) {
		currentX += currentXV
		currentY += currentYV
		if (currentXV > 0) currentXV -= 1
		currentYV -= 1
		if (currentX in xRange && currentY in yRange) return true
	}
	return false
}