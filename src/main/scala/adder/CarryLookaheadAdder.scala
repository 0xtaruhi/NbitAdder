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
  
  val aVec = VecInit(io.a.asBools)
  val bVec = VecInit(io.b.asBools)

  // g{i} = a{i} & b{i}, p{i} = a{i} | b{i}
  val gVec = (aVec zip bVec).map(x => x._1 & x._2)
  val pVec = (aVec zip bVec).map(x => x._1 ^ x._2)

  //Generate the carry chain
  val cVec = VecInit(Seq.fill(bitwidth + 1)(0.U(1.W)))
  cVec(0) := io.cin
  // c{i+1} = g{i} | (c{i} & p{i}) 
  val cVecTail = (pVec zip cVec.init).map(x => x._1 & x._2)
                  .zip(gVec).map(x => x._1 | x._2)
  for (i <- 1 until bitwidth + 1) { cVec(i) := cVecTail(i - 1) }
  
  // Output
  io.sum := VecInit(pVec.zip(cVec.init).map(x => x._1 ^ x._2)).asUInt
  io.cout := cVec(bitwidth)
}
