package day14

import loadDay

fun main() {
	val data = loadDay(14)
	val silverAnswer = silver(data)
	val goldAnswer = gold(data)
	println("SILVER ANSWER : $silverAnswer")
	println("GOLD ANSWER   : $goldAnswer")
}

fun silver(data : String) : Long {
	var ret = data.substringBefore("\n").trim()
	val rules = convertRules(data.substringAfter("\n").trim().split("\n")
		.map { Pair(it.substringBefore("->").trim(), it.substringAfter("->").trim()) })
	repeat(10) { ret = step(ret, rules) }
	return findMaxMinDiff(ret)
}

fun gold(data : String) : Long {
	var ret = data.substringBefore("\n").trim()
	val rules = convertRules(data.substringAfter("\n").trim().split("\n")
		.map { Pair(it.substringBefore("->").trim(), it.substringAfter("->").trim()) })
	return getCounts(ret, rules, 40).findMaxMinDiff()
}

typealias RuleList = ArrayList<Pair<Char, ArrayList<Pair<Char, String>>>>

fun RuleList.getIntermediate(a : Char, b : Char) : String {
	return find {it.first == a}?.second?.find { it.first == b }?.second ?: ""
}

fun convertRules(longRules : List<Pair<String, String>>) : RuleList {
	val rules = RuleList()
	for (rule in longRules) {
		val ruleIndex = rules.find { it.first == rule.first[0] }
		ruleIndex?.second?.add(Pair(rule.first[1], rule.second))
		ruleIndex ?: rules.add(Pair(rule.first[0], arrayListOf(Pair(rule.first[1], rule.second))))
	}
	return rules
}

typealias CounterRuleList = ArrayList<Pair<Char, ArrayList<Pair<Char, CharCounterDict>>>>

fun CounterRuleList.getIntermediate(a : Char, b : Char) : CharCounterDict {
	return find {it.first == a}?.second?.find { it.first == b }?.second ?: CharCounterDict()
}

fun step(current : String, rules : RuleList) : String {
	var next = "${current[0]}"
	for (i in 1 until current.length) {
		next += rules.getIntermediate(current[i - 1], current[i])
		next += current[i]
	}
	return next
}

fun findMaxMinDiff(s : String) : Long {
	val dict = ArrayList<CharCounter>()
	for (c in s) {
		dict.find {it.c == c}?.let { it.counter += 1L } ?: dict.add(CharCounter(c))
	}
	val min = dict.minOf { it.counter }
	val max = dict.maxOf { it.counter }
	return max - min
}

typealias CharCounterDict = ArrayList<CharCounter>

fun CharCounterDict.merge(other : CharCounterDict) : CharCounterDict {
	for (counter in other) {
		addChar(counter.c, counter.counter)
	}
	return this
}

fun CharCounterDict.addChar(c : Char, times : Long = 1) {
	find {it.c == c}?.let { it.counter += times } ?: add(CharCounter(c, times))
}

fun CharCounterDict.findMaxMinDiff() : Long {
	val min = minOf { it.counter }
	val max = maxOf { it.counter }
	return max - min
}

class CharCounter(val c : Char, var counter : Long = 1)


fun improveCRules(rules : RuleList, crules : CounterRuleList) : CounterRuleList {
	val ret = CounterRuleList()
	for (apair in rules) {
		val a = apair.first
		val arr = ArrayList<Pair<Char, CharCounterDict>>()
		for (bpair in apair.second) {
			val b = bpair.first
			val mid = bpair.second[0]
			val new = CharCounterDict()
			new.merge(crules.getIntermediate(a, mid))
			new.merge(crules.getIntermediate(mid, b))
			new.addChar(mid)
			arr.add(Pair(b, new))
		}
		ret.add(Pair(a, arr))
	}
	return ret
}

fun getCRules(rules : RuleList, iterations : Int) : CounterRuleList {
	var ret = CounterRuleList()
	repeat(iterations) {ret = improveCRules(rules, ret)}
	return ret
}

fun getCounts(s : String, rules : RuleList, iterations : Int) : CharCounterDict {
	val dict = CharCounterDict()
	if (dict.size == 0) {
		for (c in s) {
			dict.addChar(c)
		}
	}
	val crules = getCRules(rules, iterations)
	for (i in 1 until s.length) {
		val a = s[i - 1]
		val b = s[i]
		dict.merge(crules.getIntermediate(a, b))
	}
	return dict
}
