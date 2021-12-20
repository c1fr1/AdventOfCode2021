package day19

import loadDay
import loadExample
import platform.posix.abs

const val MINOVERLAP = 12

fun main() {
	val data = loadDay(19)
	val silverAnswer = 0//silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val scanners = data.trim().split("\n\n").map {
		val beaconLines = it.trim().split("\n")
		beaconLines.slice(1 until beaconLines.size).map { coords -> Vec3i(coords) }
	}
	val scannerPositions : Array<ScannerPos?> = Array(scanners.size) {null}
	scannerPositions[0] = ScannerPos(0, 0, 0) { v -> Vec3i(v.x, v.y, v.z) }

	val discoveredIndex = IntArray(scanners.size) {-1}
	discoveredIndex[0] = 0

	for (i in scanners.indices) {
		if (discoveredIndex.all { it >= 0 }) {
			break
		}

		for (ai in scanners.indices.filter { discoveredIndex[it] == i }) {
			for (bi in scanners.indices.filter { discoveredIndex[it] == -1 }) {
				val pos = tryPair(scanners[ai].map { scannerPositions[ai]!!(it) }, scanners[bi])
				if (pos != null) {
					discoveredIndex[bi] = i + 1
					scannerPositions[bi] = pos
					println("scanner $bi at $pos")
				}
			}
		}

		//println(discoveredIndex.joinToString { it.toString() })
	}

	println(discoveredIndex.joinToString { it.toString() })

	val positions = ArrayList<Vec3i>()
	for (i in scanners.indices) {
		positions.addAll(scanners[i].map { scannerPositions[i]!!(it) }.filter { !positions.contains(it) })
		for (b in scanners[i]) {
			val t = scannerPositions[i]!!(b)
			println("$i ${t.x},${t.y},${t.z}")
		}
	}
	for (pos in positions.sortedBy { it.x }) {
		println("${pos.x},${pos.y},${pos.z}")
	}

	return positions.size.toLong()
}

fun tryPair(a : List<Vec3i>, b : List<Vec3i>) : ScannerPos? {
	val inversedTransforms = arrayOf(
		{v : Vec3i -> Vec3i(v.x, v.y, v.z)},
		{v : Vec3i -> Vec3i(v.x, -v.y, -v.z)},
		{v : Vec3i -> Vec3i(v.x, v.z, -v.y)},
		{v : Vec3i -> Vec3i(v.x, -v.z, v.y)},
		{v : Vec3i -> Vec3i(-v.x, v.y, -v.z)},
		{v : Vec3i -> Vec3i(-v.x, -v.y, v.z)},
		{v : Vec3i -> Vec3i(-v.x, v.z, v.y)},
		{v : Vec3i -> Vec3i(-v.x, -v.z, -v.y)},

		{v : Vec3i -> Vec3i(v.y, v.x, -v.z)},
		{v : Vec3i -> Vec3i(v.y, -v.x, v.z)},
		{v : Vec3i -> Vec3i(v.y, v.z, v.x)},
		{v : Vec3i -> Vec3i(v.y, -v.z, -v.x)},
		{v : Vec3i -> Vec3i(-v.y, v.x, v.z)},
		{v : Vec3i -> Vec3i(-v.y, -v.x, -v.z)},
		{v : Vec3i -> Vec3i(-v.y, v.z, -v.x)},
		{v : Vec3i -> Vec3i(-v.y, -v.z, v.x)},

		{v : Vec3i -> Vec3i(v.z, v.y, -v.x)},
		{v : Vec3i -> Vec3i(v.z, -v.y, v.x)},
		{v : Vec3i -> Vec3i(v.z, v.x, v.y)},
		{v : Vec3i -> Vec3i(v.z, -v.x, -v.y)},
		{v : Vec3i -> Vec3i(-v.z, v.y, v.x)},
		{v : Vec3i -> Vec3i(-v.z, -v.y, -v.x)},
		{v : Vec3i -> Vec3i(-v.z, v.x, -v.y)},
		{v : Vec3i -> Vec3i(-v.z, -v.x, v.y)},
	)

	for (transformI in inversedTransforms.indices) {
		val aBeacons = a
		val bBeacons = b
		for (beaconA in aBeacons) {
			for (beaconB in bBeacons.map{ inversedTransforms[transformI](it) }) {
				val offset = beaconA - beaconB
				if (tryTransform(inversedTransforms[transformI], offset, aBeacons, bBeacons)) {
					return ScannerPos(offset.x, offset.y, offset.z, inversedTransforms[transformI])
				}
			}
		}
	}
	return null
}

fun gold(data : String) : Long {
	val data = arrayOf(
		Vec3i(1382,-30,-20),
		Vec3i(172,2,-1300),
		Vec3i(1280,-1226,-40),
		Vec3i(2477,-47,2),
		Vec3i(1267,42,1112),
		Vec3i(1273,1108,78),
		Vec3i(150,60,-2414),
		Vec3i(1258,-1243,1158),
		Vec3i(1367,-2453,-57),
		Vec3i(2490,-5,-1182),
		Vec3i(2450,92,1130),
		Vec3i(1253,14,2421),
		Vec3i(1205,1142,-1150),
		Vec3i(1321,2335,49),
		Vec3i(2565,1122,-1218),
		Vec3i(2480,-3,-2458),
		Vec3i(3624,61,-1278),
		Vec3i(1254,-1265,2471),
		Vec3i(3785,48,1211),
		Vec3i(2468,-2482,55),
		Vec3i(1370,-2400,-1165),
		Vec3i(1276,54,3592),
		Vec3i(2537,1246,-2376),
		Vec3i(2566,-1130,-2367),
		Vec3i(3607,-2449,-97),
		Vec3i(3752,1215,-1257),
		Vec3i(3687,-1240,-1176),
		Vec3i(4847,-15,-1282),
		Vec3i(2530,2402,-1217),
		Vec3i(1212,-1236,3556),
		Vec3i(124,-1141,2453),
		Vec3i(1280,-2409,2479),
		Vec3i(2557,-1262,2305),
		Vec3i(1252,-1184,4724),
		Vec3i(4832,80,-84),
		Vec3i(6041,14,-1191),
		Vec3i(6132,-1177,-1173),
		Vec3i(7324,-63,-1168),
		Vec3i(8571,-45,-1296),
	)
	var max = 0
	for (a in data) {
		for (b in data) {
			val d = a.mDistance(b)
			if (d > max) {
				max = d
			}
		}
	}
	return max.toLong()
}//less than 18026

fun tryTransform(t : (Vec3i) -> Vec3i, offset : Vec3i, a : List<Vec3i>, b : List<Vec3i>) : Boolean {
	val newB = b.map {t(it) + offset}
	return a.count {newB.contains(it)} >= MINOVERLAP
}

open class Vec3i(var x : Int, var y : Int, var z : Int) {
	constructor(s : String) : this(s.trim().split(","))

	constructor(coords : List<String>) : this(coords[0].toInt(), coords[1].toInt(), coords[2].toInt())

	operator fun minus(o : Vec3i) = Vec3i(x - o.x, y - o.y, z - o.z)
	operator fun plus(o : Vec3i) = Vec3i(x + o.x, y + o.y, z + o.z)

	override operator fun equals(o : Any?) : Boolean {
		return when (o) {
			is Vec3i -> x == o.x && y == o.y && z == o.z
			else -> false
		}
	}

	fun mDistance(o : Vec3i) : Int {
		return abs(o.x - x) + abs(o.y - y) + abs(o.z - z)
	}
}

class ScannerPos : Vec3i {
	var transform : (Vec3i) -> Vec3i = {v -> Vec3i(v.x, v.y, v.z)}
	constructor() : super(0, 0, 0)
	constructor(x : Int, y : Int, z : Int, t : (Vec3i) -> Vec3i) : super(x, y, z) {
		transform = t
	}

	operator fun invoke(v : Vec3i) = transform(v) + this

	override fun toString() : String {
		val t = invoke(Vec3i(1, 2, 3)) - this
		return "$x,$y,$z (${t.x},${t.y},${t.z})"
	}
}