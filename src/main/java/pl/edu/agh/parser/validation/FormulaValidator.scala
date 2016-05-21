package pl.edu.agh.parser.validation

/**
  * Created by lmarek on 07.05.2016.
  */
class FormulaValidator {

  def validate(formula: String): (Integer, Char, Long) = {
    val alnum = 1
    //val openBracket = 2
    //val closeBracket = 3
    val and = 4
    val or = 5
    //val startImplication = 6
    val endImplication = 7
    //val startIff = 8
    val continueIff = 9
    val endIff = 10
    // val not = 11
    val alnumOrOpenOrNot = 12
    val alnumOrOperatorOrClose = 13
    val closeBracketOrOperator = 14
    val errorClosing = 15
    val notClosed = 16
    var expected = alnumOrOpenOrNot
    var depth = 0l
    var position = 0l
    for (symbol <- formula.toCharArray) {
      if (expected == alnumOrOpenOrNot) {
        if (isAlnumOrOpenOrNot(symbol)) {
          if (isOpenBracket(symbol))
            depth += 1
          else if (isAlnum(symbol))
            expected = alnumOrOperatorOrClose
        }
        else
          return (expected, symbol, position)
      }
      else if (expected == alnumOrOperatorOrClose) {
        if (isAlnumOrOperatorOrClose(symbol)) {
          if (isCloseBracket(symbol)) {
            expected = closeBracketOrOperator
            depth -= 1
            if (depth < 0)
              return (errorClosing, symbol, position)
          }
          else if (isAnd(symbol)) expected = and
          else if (isOr(symbol)) expected = or
          else if (isStartIff(symbol)) expected = continueIff
          else if (isStartImplication(symbol)) expected = endImplication
        }
        else
          return (expected, symbol, position)
      }
      else if (expected == closeBracketOrOperator) {
        if (isCloseBracketOrOperator(symbol)) {
          if (isCloseBracket(symbol)) {
            depth -= 1
            if (depth < 0)
              return (errorClosing, symbol, position)
          }
          else if (isAnd(symbol)) expected = and
          else if (isOr(symbol)) expected = or
          else if (isStartIff(symbol)) expected = continueIff
          else if (isStartImplication(symbol)) expected = endImplication
        }
        else
          return (expected, symbol, position)

      }
      else if (expected == and) {
        if (isAnd(symbol))
          expected = alnumOrOpenOrNot
        else
          return (expected, symbol, position)
      }
      else if (expected == or) {
        if (isOr(symbol))
          expected = alnumOrOpenOrNot
        else
          return (expected, symbol, position)
      }
      else if (expected == endImplication) {
        if (isEndImplication(symbol))
          expected = alnumOrOpenOrNot
        else
          return (expected, symbol, position)
      }
      else if (expected == continueIff) {
        if (isContinueIff(symbol))
          expected = endIff
        else
          return (expected, symbol, position)
      }
      else if (expected == endIff) {
        if (isEndIff(symbol))
          expected = alnumOrOpenOrNot
        else
          return (expected, symbol, position)
      }
      else
        println("Unknown behaviour: " + expected)
      position += 1
    }
    if (expected == alnumOrOpenOrNot)
      return (expected, '\0', position)
    if (depth != 0)
      return (notClosed, '\0', position)
    (0, '\0', 0)
  }

  protected def isAlnum(symbol: Char) = symbol.toString.matches("[a-zA-Z0-9]")

  protected def isOpenBracket(symbol: Char) = symbol == '('

  protected def isCloseBracket(symbol: Char) = symbol == ')'

  protected def isAnd(symbol: Char) = symbol == '&'

  protected def isOr(symbol: Char) = symbol == '|'

  protected def isStartImplication(symbol: Char) = symbol == '='

  protected def isEndImplication(symbol: Char) = symbol == '>'

  protected def isStartIff(symbol: Char) = symbol == '<'

  protected def isContinueIff(symbol: Char) = symbol == '='

  protected def isEndIff(symbol: Char) = symbol == '>'

  protected def isNot(symbol: Char) = symbol == '~'

  protected def isAlnumOrOpenOrNot(symbol: Char) = isNot(symbol) || isOpenBracket(symbol) || isAlnum(symbol)

  protected def isAlnumOrOperatorOrClose(symbol: Char) = isAnd(symbol) || isOr(symbol) || isStartImplication(symbol) || isStartIff(symbol) || isCloseBracket(symbol) || isAlnum(symbol)

  protected def isCloseBracketOrOperator(symbol: Char) = isAnd(symbol) || isOr(symbol) || isStartImplication(symbol) || isStartIff(symbol) || isCloseBracket(symbol)
}
