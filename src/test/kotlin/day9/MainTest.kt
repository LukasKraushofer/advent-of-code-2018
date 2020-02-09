package day9

import io.kotlintest.should
import io.kotlintest.shouldBe
import io.kotlintest.specs.WordSpec

class MainTest : WordSpec ({
    "Marble" should {
        var m = Marble(0)
        var m1 = Marble(1)
        var m2 = Marble(2)
        var m3 = Marble(3)
        fun prep() {
            m = Marble(0)
            m1 = Marble(1)
            m2 = Marble(2)
            m3 = Marble(3)
        }
        "has number" {
            m.number shouldBe 0
        }
        "have itself as before" {
            m.after shouldBe m
        }
        "have itself as after" {
            m.after shouldBe m
        }
        "set after and before" {
            prep()
            Marble.setAfterFor(m, m1)
            m1.before shouldBe m
        }
        "set before and after" {
            prep()
            Marble.setBeforeFor(m, m1)
            m1.after shouldBe m
        }
        "make a small circle after" {
            prep()
            Marble.setAfterFor(m, m1)
            m1.after shouldBe m
        }
        "make a large circle after" {
            prep()
            Marble.setAfterFor(m, m1)
            Marble.setAfterFor(m1, m2)
            m2.after shouldBe m
        }
        "make a very large circle after" {
            prep()
            Marble.setAfterFor(m, m1)
            Marble.setAfterFor(m1, m2)
            Marble.setAfterFor(m2, m3)
            m3.after shouldBe m
        }
        "make a small circle before" {
            prep()
            Marble.setBeforeFor(m, m1)
            m.before shouldBe m1
        }
        "make a large circle before" {
            prep()
            Marble.setAfterFor(m, m1)
            Marble.setAfterFor(m1, m2)
            m.before shouldBe m2
        }
        "make a very large circle before" {
            prep()
            Marble.setAfterFor(m, m1)
            Marble.setAfterFor(m1, m2)
            Marble.setAfterFor(m2, m3)
            m.before shouldBe m3
        }
        "take get number" {
            prep()
            Marble.setBeforeFor(m, m1)
            Marble.setAfterFor(m, m2)
            m.take() shouldBe 0
        }
        "take check before and after" {
            prep()
            Marble.setBeforeFor(m, m1)
            Marble.setAfterFor(m, m2)
            m.take()
            m1.after shouldBe m2
            m2.before shouldBe m1
        }
    }

    "part1" should {
        val testInput = "405 players; last marble is worth 70953 points"
        val testInput1 = "10 players; last marble is worth 1618 points"
        val testInput2 = "13 players; last marble is worth 7999 points"
        val testInput3 = "17 players; last marble is worth 1104 points"
        val testInput4 = "21 players; last marble is worth 6111 points"
        val testInput5 = "30 players; last marble is worth 5807 points"
        "with real input" {
            part1(testInput) shouldBe 422980
        }
        "zero try" { part1("9 players; last marble is worth 25 points") shouldBe 32 }
        "first try" { part1(testInput1) shouldBe 8317 }
        "second try" { part1(testInput2) shouldBe 146373 }
        "third try" { part1(testInput3) shouldBe 2764 }
        "fourth try" { part1(testInput4) shouldBe 54718 }
        "fifth try" { part1(testInput5) shouldBe 37305 }
    }

    "part2" should {
        val testInput = "405 players; last marble is worth 70953 points"
        "with real input" {
            part2(testInput) shouldBe 3552041936
        }
    }
})