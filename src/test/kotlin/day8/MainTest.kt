package day8

import io.kotlintest.matchers.numerics.shouldBeLessThanOrEqual
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.kotlintest.specs.WordSpec

class MainTest : WordSpec({
    val testInput = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2"

    "part1" should {
        "test input should sum up to 138" {
            day8.part1(testInput) shouldBe 138
        }

        "test with zero child nodes" {
            day8.part1("0 2 3 5") shouldBe 8
        }

        "test with one child nodes" {
            day8.part1("1 2 0 1 1 3 5") shouldBe 9
        }
    }

    "part2" should {
        "test input should sum up to 66" {
            day8.part2(testInput) shouldBe 66
        }
        "node without sub nodes should sum up" {
            day8.part2("0 3 10 11 12")
        }
    }
})
