package pl.edu.agh.parser.structures

/**
  * Created by lmarek on 07.05.2016.
  */
abstract class Formula {
  def literal = false
  final def analyze(formula: String): Formula = {
    if (formula.matches("[a-zA-Z0-9]*"))
      return new VariableFormula(formula)
    var depth = 0l
    val symbols = formula.toCharArray
    val buffer = StringBuilder.newBuilder
    var operator: Symbol = null
    for (symbol <- symbols) {
      if (symbol == '~' && buffer.isEmpty)
        return new NegationFormula(formula.substring(1)).simplify
      else if (symbol == '=' && depth == 0 && operator == null)
        operator = 'implication
      else if (symbol == '<' && depth == 0 && operator == null)
        operator = 'iff
      else if (symbol == '&' && depth == 0 && operator == null)
        operator = 'and
      else if (symbol == '|' && depth == 0 && operator == null)
        operator = 'or
      else if (symbol == '(') {
        depth = depth + 1
        buffer.append(symbol)
      }
      else if (symbol == ')') {
        depth = depth - 1
        buffer.append(symbol)
      }
      else buffer.append(symbol)
      if (depth == 0 && !(operator == null)) {
        if (operator == 'implication)
          return new ImplicationFormula(buffer.toString(), formula.substring(buffer.size + 2))
        else if (operator == 'iff)
          return new IffFormula(buffer.toString(), formula.substring(buffer.size + 3))
        else if (operator == 'and)
          return new AndFormula(buffer.toString(), formula.substring(buffer.size + 2))
        else
          return new OrFormula(buffer.toString(), formula.substring(buffer.size + 2))
      }
    }
    analyze(formula.substring(1, formula.length - 1))
  }

  def convertToCNF(): Unit = {}
}

abstract class TwoArgFormula(part1: String, part2: String) extends Formula {
  var leftSide = analyze(part1)
  var rightSide = analyze(part2)

  override def convertToCNF() {
    leftSide match {
      case x: NegationFormula => {
        val current = x.simplify
        current match {
          case y: NegationFormula => leftSide = y.diveInNegation; leftSide.convertToCNF()
          case _ => leftSide = current; leftSide.convertToCNF()
        }
      }
      case x: AndFormula => /*leftSide = x.deMorgan*/ ; leftSide.convertToCNF()
      case x: OrFormula => leftSide = x.distribution; leftSide.convertToCNF()
      case x: IffFormula => leftSide = x.asConjunction; leftSide.convertToCNF()
      case x: ImplicationFormula => leftSide = x.asAlternative; leftSide.convertToCNF()
      case _ => leftSide.convertToCNF()
    }
    leftSide.convertToCNF()
    rightSide match {
      case x: NegationFormula => {
        val current = x.simplify
        current match {
          case y: NegationFormula => rightSide = y.diveInNegation; rightSide.convertToCNF()
          case _ => rightSide = current; rightSide.convertToCNF()
        }
      }
      case x: AndFormula => /*rightSide = x.deMorgan*/ ; rightSide.convertToCNF()
      case x: OrFormula => rightSide = x.distribution; rightSide.convertToCNF()
      case x: IffFormula => rightSide = x.asConjunction; rightSide.convertToCNF()
      case x: ImplicationFormula => rightSide = x.asAlternative; rightSide.convertToCNF()
      case _ => rightSide.convertToCNF()
    }
  }
}

abstract class SingleFormula(formula: String) extends Formula {
  var inside = analyze(formula)

  override def literal = inside match {
    case VariableFormula(_) => true
    case _ => false
  }

  override def convertToCNF(): Unit = inside match {
    case x: NegationFormula => {
      val current = x.simplify
      current match {
        case y: NegationFormula => inside = y.diveInNegation; inside.convertToCNF()
        case _ => inside = current; inside.convertToCNF()
      }
    }
    case x: OrFormula => inside = x.distribution; inside.convertToCNF()
    case x: ImplicationFormula => inside = x.asAlternative; inside.convertToCNF()
    case x: IffFormula => inside = x.asConjunction; inside.convertToCNF()
    case _ => inside.convertToCNF()
  }
}

case class OrFormula(part1: String, part2: String) extends TwoArgFormula(part1, part2) {
  def deMorgan = new NegationFormula("(~(" + leftSide + "))&&~(" + rightSide + ")").simplify

  override def toString = "(" + leftSide.toString + ")||(" + rightSide.toString + ")"

  def distribution = {
    if (leftSide.isInstanceOf[AndFormula]) {
      val left = leftSide.asInstanceOf[AndFormula]
      new AndFormula("(" + rightSide + ")||(" + left.leftSide + ")", "(" + rightSide + ")||(" + left.rightSide + ")")
    } else if (rightSide.isInstanceOf[AndFormula]) {
      val right = rightSide.asInstanceOf[AndFormula]
      new AndFormula("(" + leftSide + ")||(" + right.leftSide + ")", "(" + leftSide + ")||(" + right.rightSide + ")")
    }
    else this
  }

}

case class AndFormula(part1: String, part2: String) extends TwoArgFormula(part1, part2) {
  def deMorgan = new NegationFormula("(~(" + leftSide + "))||~(" + rightSide + ")").simplify

  override def toString = "(" + leftSide + ")&&(" + rightSide + ")"

}

case class IffFormula(part1: String, part2: String) extends TwoArgFormula(part1, part2) {
  def asConjunction = new AndFormula("(" + leftSide + ")=>" + part2, "(" + rightSide + ")=>" + part1)

  override def toString = "(" + leftSide + ")<=>(" + rightSide + ")"

}

case class ImplicationFormula(part1: String, part2: String) extends TwoArgFormula(part1, part2) {
  def asAlternative = new OrFormula("~(" + leftSide + ")", rightSide.toString)

  override def toString = "(" + leftSide + ")=>(" + rightSide + ")"

}

case class NegationFormula(formula: String) extends SingleFormula(formula) {
  def simplify: Formula = inside match {
    case x: NegationFormula => x.inside
    case _ => this
  }

  def diveInNegation = inside match {
    case inner: OrFormula => new NegationFormula(inner.deMorgan.toString).simplify
    case inner: AndFormula => new NegationFormula(inner.deMorgan.toString).simplify

    case _ => this
  }

  override def toString = "~(" + inside.toString + ")"


}

case class RootFormula(formula: String) extends SingleFormula(formula: String) {
  override def toString = inside.toString

  override def convertToCNF(): Unit = {
    var first = toString
    super.convertToCNF()
    var second = toString
    while (first != second) {
      second = first
      super.convertToCNF()
      first = toString
    }
  }

}

case class VariableFormula(formula: String) extends Formula {
  override def literal = true
  override def toString = formula
}



