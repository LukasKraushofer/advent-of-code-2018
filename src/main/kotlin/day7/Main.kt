package day7

import java.io.File

enum class TreeNodeState {
    NEW, WORKING, DONE
}

class TreeNode(val value: String) {
    var parents: MutableSet<TreeNode> = mutableSetOf()
    var nodes: MutableSet<TreeNode> = mutableSetOf()
    var state: TreeNodeState = TreeNodeState.NEW
    override fun toString(): String {
        return "TreeNode(value='$value')"
    }
}

fun makeSortedTree(input: List<String>, fac: (String) -> TreeNode = factoryFun()): MutableList<TreeNode> {
    val pairs = input
        .map { it.split(" ") }
        .map { Pair( fac(it[1]), fac(it[7]) ) }

    val nodes = pairs
        .map { p ->
            p.first.nodes.add(p.second)
            p.second.parents.add(p.first)
            p.first
        }
        .distinct()

    return nodes.filter { it.parents.isEmpty() }.sortedBy { it.value }.toMutableList()
}

val factoryFun = {
    val pool = mutableMapOf<String, TreeNode>();
    {value: String ->
        pool.computeIfAbsent(value){ TreeNode(it) }
    }
}

fun part1(lines: List<String>): String {
    var sorted = makeSortedTree(lines)

    var code = ""
    var i = 0
    while (!sorted.isEmpty()) {
        // lese 1. element von sortierter liste
        var element = sorted.get(i)
        // hat 1. element nur parents die done=true sind ?
        if (element.parents.all { it.state == TreeNodeState.DONE }) {
            // JA -> 1. element zu code hinzufuegen
            code += element.value
            //       done=true setzen
            element.state = TreeNodeState.DONE
            //       dessen nodes in die liste aufnehmen
            sorted.removeAt(i)
            val mut = mutableSetOf<TreeNode>()
            mut.addAll(sorted)
            mut.addAll(element.nodes)
            sorted = mut.sortedBy { it.value }.toMutableList()
            //       wieder von vorne beginnen
            i = 0
        } else {
            // NEIN -> naechstes element
            i++
        }
    }

    return code;
}

fun part2(lines: List<String>): Int {
    val fac = factoryFun()
    var sorted = makeSortedTree(lines, fac)
    val workers = Array(5) { Pair(".", emptySequence<Int>().iterator()) }

    var code = ""

    var count = -1
    do {
        count++
        for ((index, worker) in workers.withIndex()) {
            if (worker.second.hasNext()) {
                worker.second.next()
            } else {
                // worker is done
                // set letter to done
                if (worker.first != ".") {
                    val cur = fac(worker.first)
                    //       done=true setzen
                    cur.state = TreeNodeState.DONE
                    code += cur.value
                    sorted.remove(cur)
                    val mut = mutableSetOf<TreeNode>()
                    mut.addAll(sorted)
                    //       dessen nodes in die liste aufnehmen
                    mut.addAll(cur.nodes)
                    sorted = mut.sortedBy { it.value }.toMutableList()
                }
                // find new TreeNode
                var i = 0
                while (true) {
                    if(i >= sorted.size) {
                        // kein passendes element...
                        workers[index] = Pair(".", emptySequence<Int>().iterator())
                        break;
                    }
                    // lese 1. element von sortierter liste
                    var element = sorted.get(i)
                    // hat 1. element nur parents die done=true sind ?
                    if (element.parents.all { it.state == TreeNodeState.DONE }
                        && element.state == TreeNodeState.NEW) {
                        // JA -> 1. element zu code hinzufuegen
                        element.state = TreeNodeState.WORKING
                        val seq = workerSequenceItr(element.value)
                        workers[index] = Pair(element.value, seq)
                        if (seq.hasNext()) {
                            seq.next()
                        }
                        break;
                    } else {
                        // NEIN -> naechstes element
                        i++
                    }
                }
            }
        }
    } while ( workers.any { it.first != "." })

    return count
}

fun workerSequenceItr(letter: String): Iterator<Int> {
    val aValue = "A".chars().findFirst().asInt
    val letterValue = (letter.chars().findFirst().asInt - aValue) + 1
    return generateSequence(1) { if (it < (60 + letterValue)) it + 1 else null }.iterator()
}

fun main(){
    val readLines = File("src/main/resources/day7/input").readLines()
    println(part1(readLines))
    println(part2(readLines))

}

