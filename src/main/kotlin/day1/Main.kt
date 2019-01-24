package day1

import java.io.File

fun part1(input: List<String>): Int {
    return input
        .map { line -> line.toInt() }
        .reduce { acc, i -> acc + i }
}

fun part2(input: List<String>): Int {
    val seen = mutableSetOf<Int>()
    var value = 0
    while(true) {
        input
            .map { line -> line.toInt() }
            .forEach {
                value += it
                if(seen.contains(value)) return value
                else seen.add(value)
            }
    }
}

fun main(args: Array<String>) {
    val input = File("src/main/resources/day1/input").readLines()
    println(part1(input))
    println(part2(input))
}