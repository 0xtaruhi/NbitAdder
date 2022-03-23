package adder

import chisel3._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

class OneBitAdderSpec extends AnyFreeSpec with ChiselScalatestTester {
  "OneBitAdder should add two numbers" in {
    test(new OneBitAdder) { dut => 
      for (i <- 0 until 2; j <- 0 until 2; k <- 0 until 2) {
        dut.io.a.poke(i)
        dut.io.b.poke(j)
        dut.io.cin.poke(k)
        dut.io.sum.expect((i + j + k) % 2)
        dut.io.cout.expect(((i + j + k) / 2) % 2)
      }
    }
  }
}
