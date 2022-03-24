package adder

import chisel3._
import chisel3.util._
import chisel3.experimental._

class MixedAdder(bitwidth: Int, claMaxBitwidth: Int = 4) extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(bitwidth.W))
    val b = Input(UInt(bitwidth.W))
    val cin = Input(UInt(1.W))
    val sum = Output(UInt(bitwidth.W))
    val cout = Output(UInt(1.W))
  })

  val claBitwidthSeq = {
    if (bitwidth <= claMaxBitwidth) {
      Seq(bitwidth)
    } else {
      val numCla = bitwidth / claMaxBitwidth
      val lastClaBitwidth = bitwidth % claMaxBitwidth
      if (lastClaBitwidth == 0) {
        (0 until numCla).map(i => claMaxBitwidth)
      } else {
        (0 until numCla).map(i => claMaxBitwidth) :+ lastClaBitwidth
      }
    }
  }

  val claModules = for (i <- 0 until claBitwidthSeq.length) yield {
    val startBit = if (i == 0) 0 else { claBitwidthSeq.take(i).sum }
    val endBit = startBit + claBitwidthSeq(i) - 1
    val cla = Module(new CarryLookaheadAdder(claBitwidthSeq(i)))
    cla.io.a := io.a(endBit, startBit)
    cla.io.b := io.b(endBit, startBit)
    cla
  }

  for (i <- 1 until claModules.length) { 
    claModules(i).io.cin := claModules(i - 1).io.cout 
  }
  claModules(0).io.cin := io.cin

  io.sum := Cat(claModules.map(_.io.sum).reverse)
  io.cout := claModules.last.io.cout
}
