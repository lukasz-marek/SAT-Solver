import pl.edu.agh.api.SolverResponse

/**
  * Created by lmarek on 21.04.2016.
  */
object Main {

  def main(args: Array[String]): Unit = {
    val formula = "(((((a<=>b)<=>c)=>d)=>e)<=>f)"
    val frame = new SolverResponse(formula)
    if (frame.parseFailed()) {
      println("Error: " + frame.errorCode())
    } else {
      println("CNF: " + frame.fomulaAsCNF())
      println("Satisfiable: " + frame.isSatisfiable)
      println("Tautology: " + frame.isTautology)
    }

  }
}
