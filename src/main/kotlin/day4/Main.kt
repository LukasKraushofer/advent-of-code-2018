package day4

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun part1(input: List<String>): Int {
//    [1518-04-12 00:36] falls asleep
    val lineRegex = """\[(.+ \d\d:(\d\d))] (.+)""".toRegex()
    val guardRegex = """Guard #(\d+) begins shift""".toRegex()
    var curGuard = -1

    val group = { regex: Regex -> { line: String -> { x: Int -> regex.find(line)!!.groups[x]!!.value } } }

    val dateGroup = { line: String -> group(lineRegex)(line)(1) }
    val minuteGroup = { line: String -> group(lineRegex)(line)(2) }
    val guardGroup = { line: String -> group(lineRegex)(line)(3) }

    return input
//        .take(25)
        .filter { lineRegex.matches(it) }
        .sortedBy { dateOf(dateGroup(it)) }
//        .also { it.map{ line -> dateGroup(line)}.forEach { date -> println(date) }}
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
//        .take(2)
        .map {
            var sum: Minutes = 0
            var mostGroup = mutableMapOf<Int, Int>()

//            println("" + it.key + it.value.map { match -> guardGroup(match) })

            it.value
                .chunked(2)
                .forEach { chunk ->
                    val minutes = minuteGroup(chunk[0]).toInt() until minuteGroup(chunk[1]).toInt()
                    sum += minutes.count()
                    minutes.forEach { min ->
                        mostGroup.putIfAbsent(min, 0)
                        mostGroup.compute(min) { key, value -> value?.let { value + 1 } }
                    }
//                    println("${minutes.count()} ${minuteGroup(chunk[0]).toInt()} ${minuteGroup(chunk[1]).toInt()}")
                }
            var most = mostGroup.toSortedMap().maxBy ( Map.Entry<Int, Int>::value )

//            println("${it.key} : $sum -> $most")

            object {
                val guard = it.key
                val sum = sum
                val most = most?.key
            }
        }
        .sortedBy { it.sum }
        .also { it.forEach {println("${it!!.guard} : ${it!!.sum} -> ${it!!.most}") } }
        .maxBy { it.sum }
        .also { println("${it!!.guard} : ${it!!.sum} -> ${it!!.most}") }
        .let{ it!!.guard * it!!.most!!}
}


fun dateOf(date: String): LocalDateTime? {
    return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
}

typealias Minutes = Int

fun main() {
    val input = File("src/main/resources/day4/input").readLines()
    println(part1(input))
//    println(part2(input))
}

