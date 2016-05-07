package pl.edu.agh.data.reasoning.structures

/**
  * Created by lmarek on 21.04.2016.
  */
object TripleValueBoolean extends Enumeration {
  private type Values = Value
  private val TRUE, FALSE, UNKNOWN = Value
  val True: TripleValueBoolean = new TripleValueBoolean(TRUE)
  val False: TripleValueBoolean = new TripleValueBoolean(FALSE)
  val Unknown: TripleValueBoolean = new TripleValueBoolean(UNKNOWN)

  private def apply(value: TripleValueBoolean.Value): TripleValueBoolean = value match {
    case TRUE => True
    case FALSE => False
    case UNKNOWN => Unknown
    case _ => null
  }
}

class TripleValueBoolean private(val value: TripleValueBoolean.Value) {
  implicit def enumToObject(arg: TripleValueBoolean.Value): TripleValueBoolean = TripleValueBoolean.apply(value)

  implicit def booleanToTriple(arg: Boolean): TripleValueBoolean = if (arg) TripleValueBoolean.True else TripleValueBoolean.False

  implicit def tripleToBoolean(arg: TripleValueBoolean): Boolean = if (arg.value == TripleValueBoolean.TRUE) true else false

  def &&(that: TripleValueBoolean): TripleValueBoolean =
    if (this == TripleValueBoolean.True && that == TripleValueBoolean.True) true //implicit
    else if (this == TripleValueBoolean.False || that == TripleValueBoolean.False) false //implicit
    else TripleValueBoolean.Unknown

  def ||(that: TripleValueBoolean): TripleValueBoolean = {
    if (this.value == TripleValueBoolean.TRUE || that.value == TripleValueBoolean.TRUE) TripleValueBoolean.True
    else if (this.value == TripleValueBoolean.FALSE && that.value == TripleValueBoolean.FALSE) TripleValueBoolean.False
    else TripleValueBoolean.Unknown
  }

  def ==>(that: TripleValueBoolean): TripleValueBoolean = (!this) || that


  def <==(that: TripleValueBoolean): TripleValueBoolean = that ==> this

  def unary_! : TripleValueBoolean = {
    if (this) false
    else if (this.value == TripleValueBoolean.FALSE) true
    else TripleValueBoolean.Unknown
  }

  def <==>(that: TripleValueBoolean) = (this ==> that) && (that <== this)

  override def toString = {
    if (this.value == TripleValueBoolean.TRUE) "TRUE"
    else if (this.value == TripleValueBoolean.FALSE) "FALSE"
    else "UNKNOWN"
  }

  override def equals(that: Any): Boolean = {
    that match {
      case cmp: TripleValueBoolean =>
        this.value == cmp.value
      case _ => false
    }
  }
}
