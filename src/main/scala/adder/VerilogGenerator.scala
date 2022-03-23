package adder

import chisel3.stage.{ChiselGeneratorAnnotation, ChiselStage}
import firrtl.options.TargetDirAnnotation

object generateOneBitAdder extends App {
  (new chisel3.stage.ChiselStage).execute(
     Array("-X", "verilog"),
      Seq(
          ChiselGeneratorAnnotation(() => new OneBitAdder),
          TargetDirAnnotation("./target/Verilog/adder/")
      )
  )
}

object generateCarryLookaheadAdder extends App {
  (new chisel3.stage.ChiselStage).execute(
     Array("-X", "verilog"),
      Seq(
          ChiselGeneratorAnnotation(() => new CarryLookaheadAdder(4)),
          TargetDirAnnotation("./target/Verilog/adder/")
      )
  )
}
