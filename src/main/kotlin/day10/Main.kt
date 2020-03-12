package day10

import java.io.File

data class PosVel(var x: Long, var y: Long, var dx: Int, var dy: Int)

fun part1(inputs: List<String>) {
    val parseInput = parseInput(inputs)

    val (number, area) = generateSequence(0) { it + 1 }
        .map { i ->
            val minx = parseInput.map { it.x + (it.dx * i) }.min()!!
            val miny = parseInput.map { it.y + (it.dy * i) }.min()!!
            val maxx = parseInput.map { it.x + (it.dx * i) }.max()!!
            val maxy = parseInput.map { it.y + (it.dy * i) }.max()!!

            Pair(i, (maxx - minx) * (maxy - miny))
        }
        .take(15000)
        .minBy { it.second }!!


    val curPoints = parseInput
        .map { Pair(it.x + it.dx * number, it.y + it.dy * number)}

    println(curPoints)

    val minx = curPoints.map { it.first }.min()!!
    val miny = curPoints.map { it.second }.min()!!
    val maxx = curPoints.map { it.first }.max()!!
    val maxy = curPoints.map { it.second }.max()!!

    var matrix = arrayOf<Array<Char>>()
    for (i in 0..(maxy-miny)) {
        var array = arrayOf<Char>()
        for (j in 0..(maxx-minx)) {
            array += ' '
        }
        matrix += array
    }
    curPoints.forEach { matrix[(it.second - miny).toInt()][(it.first - minx).toInt()] = '#' }

    println("$number: $area")
    printMatrix(matrix)

}

private fun parseInput(inputs: List<String>): List<PosVel> {
    val regex = "position=< ?(-?\\d+),  ?(-?\\d+)> velocity=< ?(-?\\d+),  ?(-?\\d+)>".toRegex()
    return inputs.map {
        val (x,y, dx, dy) = regex.find(it)!!.destructured
        PosVel(x.toLong(), y.toLong(), dx.toInt(), dy.toInt())
    }
}

private fun printMatrix(matrix: Array<Array<Char>>) {
    for (array in matrix) {
        for (value in array) {
            print("$value")
        }
        println()
    }
}

fun main() {
    val readLines = File("src/main/resources/day10/input").readLines()
    part1(readLines)
}

