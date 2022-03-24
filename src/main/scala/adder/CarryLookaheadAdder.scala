package adder

import chisel3._

class CarryLookaheadAdder(bitwidth: Int) extends Module {
  val io = IO(new Bundle {
    val a = Input(UInt(bitwidth.W))
    val b = Input(UInt(bitwidth.W))
    val cin = Input(UInt(1.W))
    val sum = Output(UInt(bitwidth.W))
    val cout = Output(UInt(1.W))
  })

  // g{i} = a{i} & b{i}, p{i} = a{i} | b{i}
  val g = io.a & io.b
  val p = io.a ^ io.b

  // Generate the carry chain
  private def getC(_depth: Int): UInt = {
    _depth match {
      case 0 => io.cin
      case _ => g(_depth - 1) | p(_depth - 1) & getC(_depth - 1)
    }
  }
  private val cSeq = (0 to bitwidth).map(getC(_).asBool)
  
  // Output
  private val sumVec = VecInit(Seq.fill(bitwidth)(false.B))
  for (i <- 0 until bitwidth) {
    sumVec(i) := p(i) ^ cSeq(i)
  }
  
  io.sum := sumVec.asUInt
  io.cout := cSeq(bitwidth)
}
