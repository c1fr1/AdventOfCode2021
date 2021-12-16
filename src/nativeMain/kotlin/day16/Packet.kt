package day16

interface Packet {
	val version : Int
	val type : Int
	val length : Int

	companion object {
		operator fun invoke(bits : List<Boolean>) : Packet {
			return if (bitsToLong(bits.slice(3..5)) == 4L) LiteralPacket(bits) else OperatorPacket(bits)
		}
	}

	fun getVersionSum() : Int

	fun calculate() : Long

	override fun toString() : String
}

class LiteralPacket : Packet {
	override val version : Int
	override val type : Int
	override val length : Int

	val data : ArrayList<Boolean>
	constructor(bits : List<Boolean>) {
		version = bitsToLong(bits.slice(0..2)).toInt()
		type = bitsToLong(bits.slice(3..5)).toInt()
		var currentI = 6
		data = ArrayList()
		while (true) {
			data.addAll(bits.slice((currentI + 1)..(currentI + 4)))
			currentI += 5
			if (!bits[currentI - 5]) {
				break
			}
		}
		length = currentI
	}

	override fun getVersionSum() : Int {
		return version
	}

	override fun calculate() : Long {
		return bitsToLong(data)
	}

	override fun toString() : String {
		return bitsToLong(data).toString()
	}
}

class OperatorPacket : Packet {
	override val version : Int
	override val type : Int
	override val length : Int

	val subPackets = ArrayList<Packet>()

	constructor(bits : List<Boolean>) {
		version = bitsToLong(bits.slice(0..2)).toInt()
		type = bitsToLong(bits.slice(3..5)).toInt()
		if (bits[6]) {
			//11 bit num telling number of packets
			val numPackets = bitsToLong(bits.slice(7..17))
			var currentI = 18
			repeat(numPackets.toInt()) {
				val newPacket = Packet(bits.slice(currentI until bits.size))
				currentI += newPacket.length
				subPackets.add(newPacket)
			}
			length = currentI
		} else {
			//15 bit num telling number of bits
			val numBits = bitsToLong(bits.slice(7..21))
			length = numBits.toInt() + 22
			var currentI = 22
			while (currentI < length) {
				val newPacket = Packet(bits.slice(currentI..length))
				currentI += newPacket.length
				subPackets.add(newPacket)
			}
		}
	}

	override fun getVersionSum() : Int {
		return version + subPackets.sumOf { it.getVersionSum() }
	}

	override fun calculate() : Long {
		return when (type) {
			0 -> subPackets.sumOf { it.calculate() }
			1 -> subPackets.fold(1L) {acc, packet -> acc * packet.calculate()}
			2 -> subPackets.minOf { it.calculate() }
			3 -> subPackets.maxOf { it.calculate() }
			5 -> if (subPackets[0].calculate() > subPackets[1].calculate()) 1L else 0L
			6 -> if (subPackets[0].calculate() < subPackets[1].calculate()) 1L else 0L
			7 -> if (subPackets[0].calculate() == subPackets[1].calculate()) 1L else 0L
			else -> -1
		}
	}

	override fun toString() : String {
		return when (type) {
			0 -> "SUM(${subPackets.joinToString { it.toString() }})"
			1 -> "PROD(${subPackets.joinToString { it.toString() }})"
			2 -> "MIN(${subPackets.joinToString { it.toString() }})"
			3 -> "MAX(${subPackets.joinToString { it.toString() }})"
			5 -> "${subPackets[0]} > ${subPackets[1]}"
			6 -> "${subPackets[0]} < ${subPackets[1]}"
			7 -> "${subPackets[0]} == ${subPackets[1]}"
			else -> "ERROR"
		}
	}
}

fun bitsToLong(list : List<Boolean>) : Long {
	var ret = 0L
	for (biti in list.indices) {
		if (list[biti]) ret += 1L shl list.size - biti - 1
	}
	return ret
}