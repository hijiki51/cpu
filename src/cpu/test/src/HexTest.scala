package cpu

import chisel3._
import org.scalatest.flatspec.AnyFlatSpec
import chisel3.simulator.EphemeralSimulator._

class HexTest extends AnyFlatSpec {
  behavior of "Instruction Fetch"
  it should "work through hex" in {

    simulate(new Top) { c =>
      // 1クロックの遅延があるので先に進めないとメモリの読み出しができない
      c.reset.poke(true.B)
      c.clock.step(1)
      c.reset.poke(false.B)
      while (!c.io.exit.peek().litToBoolean) {
        c.clock.step(1)
      }
      // Print文を見るために1クロック進める
      c.clock.step(1)
    }
  }
}
