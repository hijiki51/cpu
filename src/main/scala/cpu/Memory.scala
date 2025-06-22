package cpu

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFile
import common.Consts._
import interfaces.ImemPortIO

class Memory extends Module {
  val io = IO(new Bundle {
    val imem = new ImemPortIO()
  })

  val mem = Mem(32768, UInt(8.W))

  loadMemoryFromFile(mem, "src/main/resources/mem.hex")

  io.imem.inst := Cat(
    mem(io.imem.addr + 3.U),
    mem(io.imem.addr + 2.U),
    mem(io.imem.addr + 1.U),
    mem(io.imem.addr)
  )

}
