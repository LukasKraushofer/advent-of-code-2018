package day11

const val serialNumber: Int = 1133

data class FuelCell(val x: Int, val y: Int, val powerLevel: Int, val size: Int)

typealias ValueAccFun = (x:Int, y:Int) -> Int

val fuelCellValue = { x:Int, y:Int ->
    val rackId = x + 10
    val powerLevel = (rackId * y + serialNumber) * rackId
    val digit = powerLevel.toString().let { if (it.length > 2) it[it.length - 3].toString().toInt() else 0 }
    digit - 5
}

fun calcFuelCellPower(matrix: Array<Array<Int>>, x:Int, y:Int, size: Int): Int {
    var result = 0
    for (s1 in 0 until size) {
        for (s2 in 0 until size) {
            result += matrix[y + s1][x + s2]
        }
    }
    return result
}

fun findPowerLevel(matrix: Array<Array<Int>>, fuelCellSize: IntRange): FuelCell {
    var fuelCell: FuelCell = FuelCell(-1, -1, -1, -1)

    for (size in fuelCellSize) {
        for (y in 0..matrix.size-size) {
            for (x in 0..matrix.size-size) {
                val result = calcFuelCellPower(matrix, x, y, size)

                if (fuelCell.powerLevel < result) {
                    fuelCell = FuelCell(x, y, result, size)
                }
            }
        }
    }

    return fuelCell
}

private fun defaultValueAcc(): ValueAccFun {
    val sequence = generateSequence(1) { it + 1 }.iterator()
    return { _, _ -> sequence.next() }
}

private fun createMatrix(xy: Int, valueAcc: ValueAccFun = defaultValueAcc() ): Array<Array<Int>> {
    var matrix = arrayOf<Array<Int>>()
    for (y in 0 until xy) {
        var array = arrayOf<Int>()
        for (x in 0 until xy) {
            array += valueAcc(x, y)
        }
        matrix += array
    }
    return matrix
}

private fun printMatrix(matrix: Array<Array<Int>>) {
    for (array in matrix) {
        for (value in array) {
            print(String.format("%3d", value))
        }
        println()
    }
}

fun main() {
    val matrixSize = 300
    val matrix = createMatrix(matrixSize, fuelCellValue)

    println(findPowerLevel(matrix, 3..3)) // 235,14
    println(findPowerLevel(matrix, 1 until matrixSize)) // 237,227,14
}