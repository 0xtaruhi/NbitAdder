package adder

import chisel3._
import scala.util._
import chiseltest._
import org.scalatest.freespec.AnyFreeSpec

class CarrySaveAdderSpec extends AnyFreeSpec with ChiselScalatestTester {
  "CarrySaveAdder should work" in {
    test(new CarrySaveAdder(4)) { dut => 
      val rnd = new Random
      for (i <- 0 until 10) {
        val x = rnd.nextInt(1 << 4)
        val y = rnd.nextInt(1 << 4)
        val z = rnd.nextInt(2)
        dut.io.x.poke(x)
        dut.io.y.poke(y)
        dut.io.z.poke(z)
        dut.io.sum.expect((x + y + z) & ((1 << 4) - 1))
        dut.io.carry.expect((x + y + z) >> 4)
      }
    }
  }
}
