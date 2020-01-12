package day8

import java.io.File


class Element(
    val metadata: MutableList<Int> = mutableListOf(),
    val children: MutableList<Element> = mutableListOf(),
    var sum: Int = 0
)

fun part1Calculation(input: Iterator<Int>): Int {
    if (input.hasNext()) {
        var sum = 0
        val childNodes = input.next()
        val metadata = input.next()

        if (childNodes != 0) {
            for (i in 1..childNodes) {
                sum += part1Calculation(input)
            }
        }

        for (i in 1..metadata) {
            sum += input.next()
        }

        return sum
    } else {
        return 0
    }
}

fun part2Calculation(input: Iterator<Int>): Int {
    val element = parseInput2(input, null)

    return element?.sum ?: 0
}

fun parseInput2(input: Iterator<Int>, element: Element?): Element? {
    if (input.hasNext()) {
        val newElement = Element().also {
            element?.children?.add(it)
        }

        val childNodes = input.next()
        val metadata = input.next()

        if (childNodes != 0) {
            for (i in 1..childNodes) {
                parseInput2(input, newElement)
            }
        }

        for (i in 1..metadata) {
            newElement.metadata.add(input.next())
        }

        newElement.sum =
            if (childNodes == 0) {
                newElement.metadata.sum()
            } else {
                newElement.metadata.fold(0) { sum, cur ->
                    sum + when {
                        cur <= newElement.children.size -> newElement.children[cur-1].sum
                        else -> 0
                    }
                }
            }

        return newElement;

    } else {
        return null
    }
}

fun part1(input: String): Int {
    return part1Calculation(parseInput(input))
}

fun part2(input: String): Int {
    return part2Calculation(parseInput(input))
}

fun parseInput(input: String): Iterator<Int> {
    return input.split(" ").map { it.toInt() }.iterator()
}

fun main(): Unit {
    val readLine = File("src/main/resources/day8/input").readLines().first()
    println(part1(readLine))
    println(part2(readLine))
}

