package adder

import chisel3._
import chisel3.util._

class CarrySaveAdder(bitwidth: Int) extends Module {
  val io = IO(new Bundle {
    val x = Input(UInt(bitwidth.W))
    val y = Input(UInt(bitwidth.W))
    val z = Input(UInt(bitwidth.W))
    val sum = Output(UInt(bitwidth.W))
    val carry = Output(UInt((bitwidth - 1).W))
  })

  val sumSeq = for(i <- 0 until bitwidth) yield { io.x(i) ^ io.y(i) ^ io.z(i) }
  val carrySeq = for(i <- 0 until bitwidth - 1) yield { 
    io.x(i) & io.y(i) | io.x(i) & io.z(i) | io.y(i) & io.z(i) 
  }
  io.sum := VecInit(sumSeq.reverse).asUInt
  io.carry := VecInit(carrySeq.reverse).asUInt
}
