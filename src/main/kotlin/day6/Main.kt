package day6

import java.io.File
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

val euclidean = {x1:Int, y1:Int, x2:Int, y2:Int ->
    // sqrt( (x1 - x2)^2 + (y1 - y2)^2 )
    sqrt( (x1 - x2).toDouble().pow(2.0) + (y1 - y2).toDouble().pow(2.0))
}

val taxicab = {x1:Int, y1:Int, x2:Int, y2:Int ->
    // |x1 - x2| + |y1 - y2|
     abs(x1 - x2) + abs(y1 - y2)
}

fun printrix(matrix: Array<Array<Pair<Int, Int>>>, pairs:List<Pair<Int, Int>> = emptyList()) {
    var counter = 1
    matrix.forEachIndexed { y, i ->
        print("$y:")
        i.forEachIndexed { x, j ->
            if (pairs.contains(Pair(x, y))) {
                print(" ${counter++} ")
            } else {
                when (j) {
                    Pair(-1, -1) -> print("   ")
                    Pair(-2, -1) -> print(" . ")
                    else -> print(" ${j.first} ")
                }
            }
        }
        println()
    }
}

fun part1(readLines: List<String>): Int {
    val pairs = readLines
        .map { it.split(", ") }
        .map { Pair(it[0].toInt(), it[1].toInt()) }
    val x = (pairs.map{it.first}.max() ?: 0) + 1
    val y = (pairs.map{it.second}.max() ?: 0) + 1
    val isBoarder = {xx:Int, yy:Int -> xx == 0 || xx == x || yy == 0 || yy == y}
    println("$x, $y")
    var matrix = Array(y+1) { Array(x+1) {Pair(-1, -1)} }
//    printrix(matrix, pairs)
    var areas = mutableMapOf<Int, Int>()

    for (row in 0..y) {
        for (column in 0..x) {
            matrix = pairs.foldIndexed(matrix){ index, m, pair ->
                val diff = taxicab(column, row, pair.first, pair.second)
                if (m[row][column].first == -1 || diff < m[row][column].second) {
                    m[row][column] = Pair(index, diff)
                } else if (m[row][column].second == diff) {
                    m[row][column] = Pair(-2, diff)
                }
                m
            }
            if (isBoarder(column, row)) {
                areas[matrix[row][column].first] = -1
            } else {
                areas.computeIfPresent(matrix[row][column].first) {_, v -> if (v == -1) -1 else v.plus(1) }
                areas.computeIfAbsent(matrix[row][column].first) {_ -> 1 }
            }
        }
//        println("$row: ${matrix[row].contentDeepToString()}")
    }

    println(areas)

    return areas.values.max() ?: 0
}

fun part2(readLines: List<String>): Int {
    val pairs = readLines
        .map { it.split(", ") }
        .map { Pair(it[0].toInt(), it[1].toInt()) }
    val x = (pairs.map{it.first}.max() ?: 0) + 1
    val y = (pairs.map{it.second}.max() ?: 0) + 1

    var areas = 0

    for (row in 0..y) {
        for (column in 0..x) {
            val sum = pairs
                .map { taxicab(column, row, it.first, it.second) }
                .sum()
            if (sum < 10000) {
                areas++
            }
        }
    }

    return areas
}

fun main(){
    val readLines = File("src/main/resources/day6/input").readLines()
//    val readLines = File("src/main/resources/day6/test").readLines()
    println(part1(readLines))
    println(part2(readLines))
}