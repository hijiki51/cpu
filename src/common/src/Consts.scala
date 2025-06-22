package common

import chisel3._
import chisel3.util._

object Consts {
  val WORD_LENGTH = 64
  val START_ADDR = 0.U(WORD_LENGTH.W)

  val END_OF_PROGRAM = BigInt("3837363534333231", 16).U(WORD_LENGTH.W)

  val MEMORY_DEPTH = 32768

}
