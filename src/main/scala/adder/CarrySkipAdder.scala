package adder

import chisel3._
import chisel3.util._

class CarrySkipAdder(bitwidth: Int) extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(bitwidth.W))
    val b = Input(UInt(bitwidth.W))
    val cin = Input(UInt(1.W))
    val cout = Output(UInt(1.W))
    val sum = Output(UInt(bitwidth.W))
  })

  val oneBitAdders = Seq.fill(bitwidth)(Module(new OneBitAdder()))
  for (i <- 0 until bitwidth) {
    oneBitAdders(i).io.a := io.a(i)
    oneBitAdders(i).io.b := io.b(i)
    oneBitAdders(i).io.cin := { if (i == 0) io.cin else oneBitAdders(i - 1).io.cout }
  }
  io.cout := Mux((VecInit(io.a.asBools) zip VecInit(io.b.asBools))
              .map(x => x._1 ^ x._2).reduce(_ & _), io.cin, oneBitAdders.last.io.cout)
  io.sum := Cat(oneBitAdders.map(_.io.sum).reverse)
}
