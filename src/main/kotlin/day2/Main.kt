package day2

import java.io.File

fun part1(input: List<String>): Int {
    return input
        .map { it.split("") }
        .map {
            it
                .filter { it != "" }
                .fold(
                    mapOf<String, Int>()
                ) { map, cur -> map.plus(Pair(cur, map.getOrDefault(cur, 0) + 1)) }
        }
        .map { Pair(boolToInt(it.containsValue(2)), boolToInt(it.containsValue(3))) }
        .fold(Pair(0, 0)) { acc, pair -> Pair(acc.first + pair.first, acc.second + pair.second) }
        .let { it.first * it.second }
}

fun boolToInt(bool: Boolean): Int {
    return if (bool) 1 else 0
}

fun part2(input: List<String>): String {
    var result = ""
    for (i in 0 until input.size) {
        for (j in i until input.size) {
            var count = 0
            val first = input[i].split("")
            val second = input[j].split("")
            for(k in 0 until first.size) {
                if(first[k] != second[k]) count++
            }
            if(count == 1) {
                for(k in 0 until first.size) {
                    if(first[k] == second[k]) result += first[k]
                }
            }
        }
    }
    return result
}


fun main(args: Array<String>) {
    val input = File("src/main/resources/day2/input").readLines()
    println(part1(input))
    println(part2(input)) // efmyhuckqldtwjyvisipargno
}