package day5

import java.io.File

fun part1(input: String): Int {
    return react(input).length
}

fun part2(input: String): Int {
    return ('a'..'z')
        .map { input.replace("$it|${it.toUpperCase()}".toRegex(), "") }
        .map { react(it) }
        .map(String::length)
        .min()!!
}

fun react(polymer: String): String {
    var testStr = polymer
    var tmp = ""
    while(!testStr.equals(tmp)) {
        tmp = testStr
        for (c in 'a'..'z') {
            // replce cC or Cc with empty string
            testStr = testStr.replace("($c${c.toUpperCase()})|(${c.toUpperCase()}$c)".toRegex(), "")
        }
    }
    return testStr
}

fun main() {
    val input = File("src/main/resources/day5/input").readLines().first()
    println(part1(input))
    println(part2(input))
}
