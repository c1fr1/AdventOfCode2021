import kotlinx.cinterop.refTo
import platform.posix.fclose
import platform.posix.fopen
import platform.posix.fread

fun loadDay(day : Int) : String {
	val path = "resources/day$day"
	val ret = StringBuilder()
	val buffer = ByteArray(4096)
	val file = fopen(path, "r")
	var bytesRead = fread(buffer.refTo(0), 1.toULong(), buffer.size.toULong(), file)
	while (bytesRead > 0u) {
		val section = buffer.copyOfRange(0, bytesRead.toInt()).map { it.toInt().toChar() }
		ret.append(section.toCharArray().concatToString())
		bytesRead = fread(buffer.refTo(0), 1.toULong(), buffer.size.toULong(), file)
	}
	fclose(file)
	return ret.toString()
}