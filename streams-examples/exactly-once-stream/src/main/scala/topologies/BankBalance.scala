package topologies

import java.time.LocalDateTime

case class BankBalance(name: String, count: Int, balance: Long, lastTransaction: LocalDateTime)
object BankBalance {
  def Empty: BankBalance = BankBalance("undefined", 0, 0, LocalDateTime.MIN)
}
