package pl.edu.agh.solver.algorithm.types

/**
  * Created by lmarek on 21.04.2016.
  */
object Literal extends Enumeration {
  type Sign = Value
  val POSITIVE, NEGATIVE = Value
}

class Literal(val name: String, val sign: Literal.Value) {
  private def opposite = if (sign == Literal.POSITIVE) Literal.NEGATIVE else Literal.POSITIVE

  def unary_! = new Literal(name, opposite)

  override def toString = if (sign == Literal.NEGATIVE) "~" + name else name

  def negated: Boolean = sign == Literal.NEGATIVE


  override def equals(that: Any): Boolean = {
    that match {
      case cmp: Literal =>
        this.name == cmp.name && this.negated == cmp.negated
      case _ => false
    }
  }
}
