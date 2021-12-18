package day18

import loadDay
import loadExample

fun main() {
	val data = loadDay(18)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val numbers = data.trim().split("\n").map {SnailNumber(it.trim())}
	var ret = numbers.first()
	for (i in 1 until numbers.size) {
		ret += numbers[i]
	}
	return ret.magnitude()
}

fun gold(data : String) : Long {
	val numbers = data.trim().split("\n").map {SnailNumber(it.trim())}
	var max = 0L
	for (a in numbers) {
		for (b in numbers) {
			if (a !== b) {
				max = max.coerceAtLeast((a + b).magnitude())
			}
		}
	}
	return max
}

class SnailNumber {
	var isNumber : Boolean
	val isPair : Boolean get() = !isNumber
	var value : Long
	var a : SnailNumber? = null
	var b : SnailNumber? = null

	var parent : SnailNumber? = null

	constructor(o : SnailNumber) {
		isNumber = o.isNumber
		value = o.value
		if (o.isPair) {
			a = SnailNumber(o.a!!)
			b = SnailNumber(o.b!!)
			a?.parent = this
			b?.parent = this
		}
	}

	constructor(a : SnailNumber, b : SnailNumber) {
		isNumber = false
		value = -1
		this.a = a
		this.b = b
		a.parent = this
		b.parent = this
	}

	constructor(v : Long) {
		isNumber = true
		value = v
	}

	constructor(s : String) {
		if (s.startsWith("[")) {
			var depth = 0
			var i = 1
			while (i < s.length) {
				if (depth == 0 && s[i] == ',') break
				if (s[i] == '[') depth++
				if (s[i] == ']') depth--
				++i
			}
			isNumber = false
			value = 0
			a = SnailNumber(s.substring(1 until i))
			b = SnailNumber(s.substring((i + 1) until (s.length - 1)))
			a?.parent = this
			b?.parent = this
		} else {
			isNumber = true
			value = s.toLong()
		}
	}

	operator fun plus(other : SnailNumber) : SnailNumber {
		val ret = SnailNumber(SnailNumber(this), SnailNumber(other))
		while (true) {
			if (ret.tryExplode()) continue
			if (ret.trySplit()) continue
			break
		}
		return ret
	}

	fun tryExplode(currentDepth : Int = 0) : Boolean {
		if (currentDepth == 4 && isPair) {
			//explode
			parent?.addToLeft(a!!.value, this)
			parent?.addToRight(b!!.value, this)
			isNumber = true
			value = 0
			a = null
			b = null
			return true
		} else if (isPair) {
			if (a!!.tryExplode(currentDepth + 1)) return true
			if (b!!.tryExplode(currentDepth + 1)) return true
		}
		return false
	}

	fun trySplit() : Boolean {
		if (isNumber && value >= 10) {
			isNumber = false
			a = SnailNumber(value / 2)
			b = SnailNumber(value / 2 + value % 2)
			a!!.parent = this
			b!!.parent = this
			return true
		} else if (isPair) {
			if (a!!.trySplit()) return true
			if (b!!.trySplit()) return true
		}
		return false
	}

	fun magnitude() : Long {
		return if (isNumber) value else 3 * a!!.magnitude() + 2 * b!!.magnitude()
	}

	fun addRight(v : Long) {
		if (isNumber) {
			value += v
		} else {
			b!!.addRight(v)
		}
	}

	fun addLeft(v : Long) {
		if (isNumber) {
			value += v
		} else {
			a!!.addLeft(v)
		}
	}

	fun addToLeft(v : Long, from : SnailNumber) {
		if (b === from) {
			a!!.addRight(v)
		} else {
			parent?.addToLeft(v, this)
		}
	}

	fun addToRight(v : Long, from : SnailNumber) {
		if (a === from) {
			b!!.addLeft(v)
		} else {
			parent?.addToRight(v, this)
		}
	}

	override fun toString() : String {
		return if (isNumber) value.toString() else "[${a!!},${b!!}]"
	}
}