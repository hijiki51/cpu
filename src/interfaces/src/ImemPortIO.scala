package interfaces

import chisel3._
import chisel3.util._
import common.Consts._

class ImemPortIO extends Bundle {
  // メモリアドレス用入力ポート
  val addr = Input(UInt(WORD_LENGTH.W))
  // メモリから読み込んだ命令用出力ポート
  val inst = Output(UInt(WORD_LENGTH.W))
}
