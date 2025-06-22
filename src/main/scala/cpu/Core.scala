package cpu

import chisel3._
import chisel3.util._
import common.Consts._
import interfaces.ImemPortIO

class Core extends Module {

  val io = IO(new Bundle {
    val imem = Flipped(new ImemPortIO)

    val exit = Output(Bool())
  })

  // 64bit * 64
  val reg = Mem(64, UInt(WORD_LENGTH.W))

  val pc_reg = RegInit(START_ADDR)

  // クロックごとにPCを8ずつインクリメント(64bit / 8bit = 8)
  pc_reg := pc_reg + 8.U(WORD_LENGTH.W)

  // メモリのアドレス指定ポートにPCを入力
  io.imem.addr := pc_reg

  val inst = io.imem.inst

  io.exit := (inst === END_OF_PROGRAM)
}
