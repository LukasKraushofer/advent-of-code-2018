package day3

import java.io.File
import java.util.regex.Pattern


fun part1(input: List<String>): Int {
    val pattern = Pattern.compile("#\\d+ @ (\\d+),(\\d+): (\\d+)x(\\d+)")!!
    return input
        .map { pattern.matcher(it) }
        .filter { it.matches() }
        .map {
            Rect(
                XY(it.group(1).toInt(), it.group(2).toInt()),
                XY(it.group(3).toInt(), it.group(4).toInt())
            )
        }
        .flatMap {
            IntArray(it.size.first) { i -> i + it.start.first }
                .flatMap { x ->
                    IntArray(it.size.second) { i -> i + it.start.second }
                        .map { y -> XY(x, y) }
                }
                .asIterable()
        }
        .fold(mutableMapOf<XY, Int>()) { map, pair -> map.apply { merge(pair, 1, Int::plus) } }
        .filter { it.value > 1 }
        .count()
}

data class Rect(val start: Pair<Int, Int>, val size: Pair<Int, Int>)
typealias XY = Pair<Int, Int>

fun part2(input: List<String>): Int {
    val pattern = Pattern.compile("#(\\d+) @ (\\d+),(\\d+): (\\d+)x(\\d+)")!!
    val groups = input
        .map { pattern.matcher(it) }
        .filter { it.matches() }
        .map {
            val id = it.group(1).toInt()
            val sizeX = it.group(2).toInt()
            val sizeY = it.group(3).toInt()
            val startX = it.group(4).toInt()
            val startY = it.group(5).toInt()

            Group(
                id,
                IntArray(startX) { i -> i + sizeX }
                    .flatMap { x ->
                        IntArray(startY) { i -> i + sizeY }
                            .map { y -> XY(x, y) }
                    }
            )
        }

    val checkedGroups = mutableListOf<Int>()
    return groups
        .apply { println("all size: $size") }
        .parallelStream()
        .filter { group ->
            !checkedGroups.contains(group.id)
                && groups
                    .filter { it.id != group.id }
                    .all { otherGroup ->
                        group.pairs.none { otherGroup.pairs.contains(it) }
                            .apply {
                                if (!this) {
                                    println("group ${group.id} and ${otherGroup.id} have same pairs")
                                    checkedGroups.add(group.id)
                                    checkedGroups.add(otherGroup.id)
                                }
                            }
                    }
        }
        .map(Group::id)
        .findAny().orElse(0)
}

data class Group(val id: Int, val pairs: List<XY>)

fun main() {
    val input = File("src/main/resources/day3/input").readLines()
    println(part1(input))
    println(part2(input))
}
