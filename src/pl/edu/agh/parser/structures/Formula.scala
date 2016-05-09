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

}

abstract class TwoArgFormula(part1: String, part2: String) extends Formula {
  protected var leftSide = analyze(part1)
  protected var rightSide = analyze(part2)
}

abstract class SingleFormula(formula: String) extends Formula {
  protected var inside = analyze(formula)

  override def literal = inside match {
    case VariableFormula(_) => true
    case _ => false
  }
}

case class OrFormula(part1: String, part2: String) extends TwoArgFormula(part1, part2) {
  def asConjunction = new NegationFormula("(~(" + leftSide + "))&&~(" + rightSide + ")").simplify

  override def toString = "(" + leftSide.toString + ")||(" + rightSide.toString + ")"
}

case class AndFormula(part1: String, part2: String) extends TwoArgFormula(part1, part2) {
  def asAlternative = new NegationFormula("(~(" + leftSide + "))||~(" + rightSide + ")").simplify

  override def toString = "(" + leftSide.toString + ")&&(" + rightSide.toString + ")"
}

case class IffFormula(part1: String, part2: String) extends TwoArgFormula(part1, part2) {
  def asConjunction = new AndFormula("(" + leftSide + ")=>" + part2, "(" + rightSide + ")=>" + part1)

  override def toString = "(" + leftSide.toString + ")<=>(" + rightSide.toString + ")"
}

case class ImplicationFormula(part1: String, part2: String) extends TwoArgFormula(part1, part2) {
  def asAlternative = new OrFormula("~(" + leftSide + ")", rightSide.toString)

  override def toString = "(" + leftSide.toString + ")=>(" + rightSide.toString + ")"
}

case class NegationFormula(formula: String) extends SingleFormula(formula) {
  def simplify: Formula = inside match {
    case NegationFormula(_) => {
      val next = inside.asInstanceOf[NegationFormula].inside
      if (next.isInstanceOf[NegationFormula]) next.asInstanceOf[NegationFormula].simplify
      else next
    }
    case _ => this
  }

  override def toString = "~(" + inside.toString + ")"
}

case class RootFormula(formula: String) extends SingleFormula(formula: String) {
  override def toString = inside.toString
}

case class VariableFormula(formula: String) extends Formula {
  override def literal = true

  override def toString = formula
}



