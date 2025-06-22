package common

import chisel3._
import chisel3.util._

object Consts {
  val WORD_LENGTH = 64
  val START_ADDR = 0.U(WORD_LENGTH.W)

  val END_OF_PROGRAM = 0x34333231.U(WORD_LENGTH.W)
}
