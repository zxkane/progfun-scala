package recfun
import common._

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c == 0 || c == r) 1
    else
      pascal(c - 1, r - 1) + pascal(c, r - 1)
  }

  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    def countParentheses(score: Int, chars: List[Char]): Int = {
      if (chars.isEmpty) score
      else if (chars.head == '(')
        countParentheses(score + 1, chars.tail)
      else if (chars.head == ')') {
        if (score > 0) countParentheses(score - 1, chars.tail)
        else -1
      } else countParentheses(score, chars.tail)
    }

    if (countParentheses(0, chars) == 0) true
    else false
  }

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = {
    def countChange2(money: Int, coin: Int, count: Int, coins: List[Int]): Int = {
      if (count == 1) countChange(money - coin, coins)
      else if (money - coin * count == 0)
        1 + countChange2(money, coin, count - 1, coins)
      else
        countChange(money - coin * count, coins) + countChange2(money, coin, count - 1, coins)
    }

    var newcoins = coins.sorted
    if (newcoins.isEmpty) 0
    else {
      var coin = newcoins.head
      if (money < coin) 0
      else if (money == coin) 1
      else {
        // money > coin
        countChange2(money, coin, money / coin, newcoins.tail) + countChange(money, newcoins.tail)
      }
    }
  }
}
