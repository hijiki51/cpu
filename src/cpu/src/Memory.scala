package cpu

import chisel3._
import chisel3.util._
import chisel3.util.experimental.loadMemoryFromFileInline
import common.Consts._
import interfaces.ImemPortIO
import scala.util.Using

class Memory extends Module {

  // readFileToSequenceをより安全な形に修正
  def readFileToSequence(filename: String): Seq[BigInt] = {
    // getResourceAsStreamはOptionでラップしてnull安全にする
    val streamOption = Option(getClass.getResourceAsStream(filename))

    streamOption
      .map { inputStream =>
        // Usingを使うと、ブロックを抜け出すときに自動でcloseしてくれる
        Using(scala.io.Source.fromInputStream(inputStream)) { source =>
          source.getLines().map(line => BigInt(line, 16)).toSeq
        }.getOrElse(Seq.empty) // Usingが失敗した場合（稀）は空のSeqを返す
      }
      .getOrElse {
        // streamOptionがNoneだった場合（ファイルが見つからない場合）
        println(s"エラー: クラスパスリソース '$filename' が見つかりませんでした。")
        println("cpu/test/resources/ にファイルが正しく配置されているか確認してください。")
        Seq.empty // ファイルが見つからない場合は空のSeqを返す
      }
  }

  def load(memory: Mem[UInt], path: String): Unit = {
    // Set the memory
    val memContent = readFileToSequence(path)

    if (memContent.nonEmpty) {
      // Read the file and initialize it
      val s =
        if (memContent.length < MEMORY_DEPTH) memContent.length
        else MEMORY_DEPTH
      for (i <- 0 until s) {
        memory(i) := memContent(i).asUInt
      }
    }
  }
  val io = IO(new Bundle {
    val imem = new ImemPortIO()
  })

  val mem = Mem(MEMORY_DEPTH, UInt(8.W))

  load(mem, "/mem.hex")

  io.imem.inst := Cat(
    mem(io.imem.addr + 7.U),
    mem(io.imem.addr + 6.U),
    mem(io.imem.addr + 5.U),
    mem(io.imem.addr + 4.U),
    mem(io.imem.addr + 3.U),
    mem(io.imem.addr + 2.U),
    mem(io.imem.addr + 1.U),
    mem(io.imem.addr)
  )
}
