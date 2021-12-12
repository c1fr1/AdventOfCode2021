package day12

import loadDay
import loadExample

fun main() {
	val data = loadDay(12)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	val paths = data.trim().split("\n").map { it.trim().split("-") }
	return countRoutes(paths)
}

fun gold(data : String) : Long {
	val paths = data.trim().split("\n").map { it.trim().split("-") }
	return countRoutesSmallTwice(paths)
}

fun countRoutes(paths : List<List<String>>, currentNode : String = "start",
                currentPath : ArrayList<String> = arrayListOf("start")) : Long {
	var possibleNodes = paths.filter { it.contains(currentNode) }.map { it.filter { n -> n != currentNode }[0] }
	possibleNodes = possibleNodes.filter { it == "end" || !(it[0] in 'a'..'z' && currentPath.contains(it)) }
	return possibleNodes.sumOf { if (it == "end") 1 else {
		currentPath.add(it)
		val ret = countRoutes(paths, it, currentPath)
		currentPath.removeLast()
		ret
	} }
}


fun countRoutesSmallTwice(paths : List<List<String>>, currentNode : String = "start",
                currentPath : ArrayList<String> = arrayListOf("start")) : Long {
	var possibleNodes = paths.filter { it.contains(currentNode) }.map { it.filter { n -> n != currentNode }[0] }
	possibleNodes = possibleNodes.filter { it != "start" }
	return possibleNodes.sumOf { if (it == "end") 1 else {
		val ret = if (it[0] in 'a'..'z' && currentPath.contains(it)) {
			currentPath.add(it)
			countRoutes(paths, it, currentPath)
		} else {
			currentPath.add(it)
			countRoutesSmallTwice(paths, it, currentPath)
		}
		currentPath.removeLast()
		ret
	} }
}
//9098
//143170