package day9

import java.io.File

class Marble(val number: Int) {
    override fun toString(): String {
        return number.toString()
    }

    var before: Marble = this
        private set(value) {
            field = value
        }

    var after: Marble = this
        private set(value) {
            field = value
        }

    fun take(): Int {
        before.after = after
        after.before = before
        return number
    }

    companion object {
        fun setBeforeFor(cur: Marble, before: Marble) {
            val self = cur.after == cur && cur.before == cur
            if (self) {
                cur.before = before
                cur.after = before
                before.before = cur
                before.after = cur
            } else {
                val curBefor = cur.before
                cur.before = before
                before.before = curBefor
                before.after = cur
                curBefor.after = before
            }
        }
        fun setAfterFor(cur: Marble, after: Marble) {
            val self = cur.after == cur && cur.before == cur
            if (self) {
                cur.after = after
                cur.before = after
                after.after = cur
                after.before = cur
            } else {
                val curAfter = cur.after
                cur.after = after
                after.after = curAfter
                after.before = cur
                curAfter.before = after
            }
        }
    }
}

data class Player(val number: Int, var score: Long = 0)

fun part2(input: String): Long {
    val (playerCount, marbles) = parseInput(input)
    return calculation(initializePlayers(playerCount), marbles * 100).map {it.score}.max() ?: 0
//    val playerWhoWonFirst = calculation(initializePlayers(playerCount), marbles).maxBy { it.score }!!
//    return calculation(initializePlayers(playerCount), marbles * 100)
//        .first { it.number == playerWhoWonFirst.number }
//        .score
}

fun part1(input: String): Long {
    val (player, marbles) = parseInput(input)

    val allPlayers = initializePlayers(player)

    return calculation(allPlayers, marbles).map { it.score }.max() ?: 0
}

private fun printMarbles(m0: Marble) {
    var pm = m0
    do {
        print("${pm.number} ")
        pm = pm.after
    } while (pm != m0)
    println()
}

private fun parseInput(input: String): Pair<Int, Int> {
    val regex = "(\\d+) players; last marble is worth (\\d+) points".toRegex()
    val (players, marbles) = regex.find(input)!!.destructured
    return Pair(players.toInt(), marbles.toInt())
}

private fun calculation(allPlayers: List<Player>, marbles: Int): List<Player> {
    val m0 = Marble(0)
    var curMarble = m0
    val players = playerSeq(allPlayers).iterator()

    for (m in 1..marbles) {
        val curPlayer = players.next()
//        print("[${curPlayer.number}] ")
        if (m % 23 == 0) {
            curPlayer.score += m
            // 7 times counter-clockwise
            val marbleToRemove = curMarble.before.before.before.before.before.before.before
            curMarble = marbleToRemove.after
            curPlayer.score += marbleToRemove.take()
        } else {
            val newMarble = Marble(m)
            Marble.setAfterFor(curMarble.after, newMarble)
            curMarble = newMarble
        }
//        printMarbles(m0)
    }

//    allPlayers.forEach {println("[${it.number}]: ${it.score}")}
    return allPlayers
}

private fun initializePlayers(players: Int): List<Player> =
    generateSequence(1) { if (it < players) it + 1 else null }
        .map { Player(it) }
        .toList()


private fun playerSeq(players: List<Player>): Sequence<Player> = sequence {
    while(true) {
        for (p in players) {
            yield(p)
        }
    }
}

fun main(): Unit {
    val readLine = File("src/main/resources/day9/input").readLines().first()
    println(part1(readLine))
    println(part2(readLine))
}
