package day4

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

typealias Minutes = Int

val lineRegex = """\[(.+ \d\d:(\d\d))] (.+)""".toRegex()
val guardRegex = """Guard #(\d+) begins shift""".toRegex()

val group = { regex: Regex -> { line: String -> { x: Int -> regex.find(line)!!.groups[x]!!.value } } }

val dateGroup = { line: String -> group(lineRegex)(line)(1) }
val minuteGroup = { line: String -> group(lineRegex)(line)(2) }
val guardGroup = { line: String -> group(lineRegex)(line)(3) }

data class GuardInfo(val guard: Int, val sumOfMinutes: Int, val minute: Int, val occurrence: Int)

fun dateOf(date: String): LocalDateTime? {
    return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
}

fun guardInfos(input: List<String>): List<GuardInfo> {
    var curGuard = -1

    return input
        .filter { lineRegex.matches(it) }
        .sortedBy { dateOf(dateGroup(it)) }
        .fold(mutableMapOf<Int, MutableList<String>>()) { acc, cur ->
            // begins with guard
            if (guardRegex.matches(guardGroup(cur))) {
                // save current guard
                curGuard = group(guardRegex)(guardGroup(cur))(1).toInt()
                acc.putIfAbsent(curGuard, mutableListOf())
            } else {
                // add to list
                acc[curGuard]!!.add(cur)
            }
            acc
        }.entries
        .map {
            var sum: Minutes = 0
            var mostGroup = mutableMapOf<Int, Int>()
            it.value
                // chunk fall asleep and wakeup
                .chunked(2)
                .forEach { chunk ->
                    val minutes = minuteGroup(chunk[0]).toInt() until minuteGroup(chunk[1]).toInt()
                    sum += minutes.count()
                    minutes.forEach { min ->
                        mostGroup.putIfAbsent(min, 0)
                        mostGroup.compute(min) { _, value -> value?.let { value + 1 } }
                    }
                }
            var most = mostGroup.toSortedMap().maxBy(Map.Entry<Int, Int>::value)
            GuardInfo(
                guard = it.key,
                sumOfMinutes = sum,
                minute = most?.key ?: -1,
                occurrence = most?.value ?: 0
            )
        }
}

fun part1(input: List<String>): Int {
    return guardInfos(input)
        .sortedBy { it.sumOfMinutes }
        .maxBy { it.sumOfMinutes }
        .also { println("${it!!.guard} : ${it.sumOfMinutes} -> ${it.minute}") }
        .let { it!!.guard * it.minute }
}

fun part2(input: List<String>): Int {
    return guardInfos(input)
        .sortedBy { it.occurrence }
        .maxBy { it.occurrence }
        .also { println("${it!!.guard} : ${it.minute} -> ${it.occurrence}") }
        .let { it!!.guard * it.minute }
}

fun main() {
    val input = File("src/main/resources/day4/input").readLines()
    println(part1(input))
    println(part2(input))
}

