package day16

import loadDay

fun main() {
	val data = loadDay(16)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val bits = convertToBits(data)
	val packet = Packet(bits.asList())
	return packet.getVersionSum().toLong()
}

fun gold(data : String) : Long {
	val bits = convertToBits(data)
	val packet = Packet(bits.asList())
	return packet.calculate()
}

fun convertToBits(s : String) : Array<Boolean> {
	val bits = Array(s.trim().length * 4) {false}
	val bitArrays = s.trim().map { getBits(it) }
	for (bitArrI in bitArrays.indices) {
		val bitArr = bitArrays[bitArrI]
		for (bitI in bitArr.indices) {
			bits[bitArrI * bitArr.size + bitI] = bitArr[bitI]
		}
	}
	return bits
}

fun getBits(hexChar : Char) : Array<Boolean> {
	val dec = if (hexChar in '0'..'9') hexChar.digitToInt() else ((hexChar - 'A') + 10)
	return Array(4) { dec and (1 shl (3 - it)) != 0 }
}