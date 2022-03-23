package adder

import chisel3._
import scala.util._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

class CarryLookaheadAdderSpec extends AnyFreeSpec with ChiselScalatestTester {
  "CarryLookaheadAdder should work" in {
    test(new CarryLookaheadAdder(4)) { dut => 
      val rnd = new Random
      for (i <- 0 until 10) {
        val a = rnd.nextInt(1 << 4)
        val b = rnd.nextInt(1 << 4)
        val cin = rnd.nextInt(2)
        dut.io.a.poke(a)
        dut.io.b.poke(b)
        dut.io.cin.poke(cin)
        dut.io.sum.expect((a + b + cin) & ((1 << 4) - 1))
        dut.io.cout.expect((a + b + cin) >> 4)
      }
    }
  }
}
