package day22

import loadDay
import loadExample

fun main() {
	val data = loadDay(22)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val board = Array(101) {Array(101) {Array(101) {false} } }
	for (l in data.trim().split('\n')) {
		val targState = l.substringBefore(' ') == "on"
		val coords = l.split(',').map { it.trim() }
		val xr = coords[0].substringAfter('=').split("..").map { it.toInt() + 50 }
		val yr = coords[1].substringAfter('=').split("..").map { it.toInt() + 50 }
		val zr = coords[2].substringAfter('=').split("..").map { it.toInt() + 50 }
		for (x in xr[0]..xr[1]) {
			if (x !in board.indices) continue
			for (y in yr[0]..yr[1]) {
				if (y !in board.indices) continue
				for (z in zr[0]..zr[1]) {
					if (z !in board.indices) continue
					board[x][y][z] = targState
				}
			}
		}
	}
	return board.sumOf { slice -> slice.sumOf { dim -> dim.count { it } } }.toLong()
}

fun gold(data : String) : Long {
	val cuboids = ArrayList<SimpleCuboid>()
	var lineI = 0
	for (l in data.trim().split('\n')) {
		val targState = l.substringBefore(' ') == "on"
		val coords = l.split(',').map { it.trim() }
		val xr = coords[0].substringAfter('=').split("..").map { it.toInt() }
		val yr = coords[1].substringAfter('=').split("..").map { it.toInt() }
		val zr = coords[2].substringAfter('=').split("..").map { it.toInt() }

		println(lineI++)

		val newCuboid = SimpleCuboid(xr[0], xr[1], yr[0], yr[1], zr[0], zr[1], targState)


		val size = cuboids.size
		var i = 0
		while (i < size && i < cuboids.size) {
			val c = cuboids[i]
			if (c.intersectsOther(newCuboid)) {
				cuboids.remove(c)
				cuboids.addAll(c.split(newCuboid))
			} else {++i}
		}
		if (newCuboid.state) {
			cuboids.add(newCuboid)
		}
	}
	return cuboids.sumOf { it.countOn() }
}
//1165737675574776
//
//43163635711605984 too high

//39769202352882
//2758514936282235

interface Cuboid {
	val minx : Int
	val maxx : Int
	val miny : Int
	val maxy : Int
	val minz : Int
	val maxz : Int

	fun intersectsOther(o : Cuboid) : Boolean {
		return !(maxx < o.minx || minx > o.maxx ||
				maxy < o.miny || miny > o.maxy ||
				maxz < o.minz || minz > o.maxz)
	}

	fun contains(x : Int, y : Int, z : Int) : Boolean {
		return x in minx..maxx && y in miny..maxy && z in minz..maxz
	}

	fun getValue(x : Int, y : Int, z : Int) : Boolean

	fun countOn() : Long
}

class SimpleCuboid(
	override val minx : Int,
	override val maxx : Int,
	override val miny : Int,
	override val maxy : Int,
	override val minz : Int,
	override val maxz : Int,
	val state : Boolean
) : Cuboid {
	override fun getValue(x : Int, y : Int, z : Int) : Boolean {
		return state
	}

	override fun countOn() : Long {
		return if (state) {
			(maxx - minx + 1) * (maxy - miny + 1) * (maxz - minz + 1).toLong()
		} else {
			0
		}
	}

	fun split(o : SimpleCuboid) : ArrayList<SimpleCuboid> {

		val ret = ArrayList<SimpleCuboid>()

		//a b a b // a b-1, b a
		//a b b a // a b-1, b b, b+1 a

		//b a a b // a a
		//b a b a // a b, b+1 a

		val xranges = if (minx >= o.minx && maxx <= o.maxx) {
			arrayOf(Pair(minx, maxx))
		} else if (minx < o.minx && maxx <= o.maxx) {
			arrayOf(Pair(minx, o.minx - 1), Pair(o.minx, maxx))
		} else if (minx < o.minx && maxx > o.maxx) {
			arrayOf(Pair(minx, o.minx - 1), Pair(o.minx, o.maxx), Pair(o.maxx + 1, maxx))
		} else if (minx >= o.minx && maxx > o.maxx) {
			arrayOf(Pair(minx, o.maxx), Pair(o.maxx + 1, maxx))
		} else {
			println("fak")
			arrayOf()
		}

		val yranges = if (miny >= o.miny && maxy <= o.maxy) {
			arrayOf(Pair(miny, maxy))
		} else if (miny < o.miny && maxy <= o.maxy) {
			arrayOf(Pair(miny, o.miny - 1), Pair(o.miny, maxy))
		} else if (miny < o.miny && maxy > o.maxy) {
			arrayOf(Pair(miny, o.miny - 1), Pair(o.miny, o.maxy), Pair(o.maxy + 1, maxy))
		} else if (miny >= o.miny && maxy > o.maxy) {
			arrayOf(Pair(miny, o.maxy), Pair(o.maxy + 1, maxy))
		} else {
			println("fak")
			arrayOf()
		}

		val zranges = if (minz >= o.minz && maxz <= o.maxz) {
			arrayOf(Pair(minz, maxz))
		} else if (minz < o.minz && maxz <= o.maxz) {
			arrayOf(Pair(minz, o.minz - 1), Pair(o.minz, maxz))
		} else if (minz < o.minz && maxz > o.maxz) {
			arrayOf(Pair(minz, o.minz - 1), Pair(o.minz, o.maxz), Pair(o.maxz + 1, maxz))
		} else if (minz >= o.minz && maxz > o.maxz) {
			arrayOf(Pair(minz, o.maxz), Pair(o.maxz + 1, maxz))
		} else {
			arrayOf()
		}

		for (xr in xranges) {
			for (yr in yranges) {
				for (zr in zranges) {
					val cuboid = SimpleCuboid(
						xr.first, xr.second,
						yr.first, yr.second,
						zr.first, zr.second, true)

					if (intersectsOther(cuboid) && cuboid.isValid() && !o.intersectsOther(cuboid)) {
						ret.add(cuboid)
					}
				}
			}
		}

		return ret
	}

	fun isValid() : Boolean {
		return minx <= maxx && miny <= maxy && minz <= maxz
	}

	override fun toString() : String {
		return "$minx..$maxx $miny..$maxy $minz..$maxz"
	}
}