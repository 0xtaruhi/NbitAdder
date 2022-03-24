package adder

import chisel3._
import scala.util._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

class MixedAdderSpec extends AnyFreeSpec with ChiselScalatestTester {
  "MixedAdder should work" in {
    test(new MixedAdder(16, 4)) { dut =>
      val rnd = new Random
      for (i <- 0 until 10) {
        val a = rnd.nextInt(1 << 16)
        val b = rnd.nextInt(1 << 16)
        val cin = rnd.nextInt(2)
        dut.io.a.poke(a)
        dut.io.b.poke(b)
        dut.io.cin.poke(cin)
        dut.io.sum.expect((a + b + cin) & ((1 << 16) - 1))
        dut.io.cout.expect((a + b + cin) >> 16)
      }
    }
  }
}

