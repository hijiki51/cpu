package cpu

import chisel3._
import chisel3.util._
import common.Consts._
import common.Instructions._
import interfaces._

class Core extends Module {

  val io = IO(new Bundle {
    val imem = Flipped(new ImemPortIO)
    val dmem = Flipped(new DmemPortIO)
    val exit = Output(Bool())
  })

  // 64bit * 64
  val reg = Mem(64, UInt(WORD_LENGTH.W))

  /* ------- fetch ------- */
  val pc_reg = RegInit(START_ADDR)

  // クロックごとにPCを8ずつインクリメント(64bit / 8bit = 8)
  pc_reg := pc_reg + 8.U(WORD_LENGTH.W)

  // メモリのアドレス指定ポートにPCを入力
  io.imem.addr := pc_reg

  /* ------- decode ------- */
  val inst = io.imem.inst

  val rs1_addr = inst(19, 15)
  val rs2_addr = inst(24, 20)
  val rd_addr = inst(11, 7)

  // アドレスが0の場合は値も0になるようにする
  val rs1_data = Mux(rs1_addr =/= 0.U, reg(rs1_addr), 0.U)
  val rs2_data = Mux(rs2_addr =/= 0.U, reg(rs2_addr), 0.U)

  val imm_i = Cat(Fill(20, inst(31)), inst(31, 20))

  // NOTE: ALU(Arithmetic Logic Unit)
  val alu_out = MuxCase(
    0.U(WORD_LENGTH.W),
    Seq(
      (inst === LOAD_WORD) -> (rs1_data + imm_i),
      (inst === LOAD_DWORD) -> (rs1_data + imm_i)
    )
  )
  // NOTE: 読みだしたデータをどうするかはこっちが決めればよい
  // したがってLOAD命令に限る必要はなく、LOAD命令以外でも読み出す（LOAD命令以外では使わなければよい）
  io.dmem.addr := alu_out

  val wb_data = io.dmem.rdata
  when(inst === LOAD_WORD || inst === LOAD_DWORD) {
    reg(rd_addr) := wb_data
  }
  io.exit := (inst === END_OF_PROGRAM)

  /* ------- debug ------- */
  printf(p"inst: 0x${Hexadecimal(inst)}\n")
  printf(p"pc_reg: 0x${Hexadecimal(pc_reg)}\n")
  printf(p"exit: ${io.exit}\n")
  printf(p"rs1_addr: 0x${Hexadecimal(rs1_addr)}\n")
  printf(p"rs2_addr: 0x${Hexadecimal(rs2_addr)}\n")
  printf(p"rd_addr: 0x${Hexadecimal(rd_addr)}\n")
  printf(p"wb_data: 0x${Hexadecimal(wb_data)}\n")
  printf(p"io.dmem.addr: 0x${Hexadecimal(io.dmem.addr)}\n")
  printf(p"rs1_data: 0x${Hexadecimal(rs1_data)}\n")
  printf(p"rs2_data: 0x${Hexadecimal(rs2_data)}\n")
  printf(p"--------------------------------\n")

}
