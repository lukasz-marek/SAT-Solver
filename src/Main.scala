import pl.edu.agh.parser.structures.ImplicationFormula

/**
  * Created by lmarek on 21.04.2016.
  */
object Main {

  def main(args: Array[String]): Unit = {
    /*lazy val query = StringBuilder.newBuilder
    var limit = 20000
    query.append("-1")
    while (limit >= 0) {
      query.append("&&" + limit)
      limit = limit - 1
    }
    val parser = new ConjunctiveNormalFormParser()
    val test = "(x1||x4)&&(x1||~x3||~x8)&&(x1||x8||x12)&&(x2||x11)&&(~x7||~x3||x9)&&(~x7||x8||~x9)&&(x7||x8||~x10)&&(x7||x10||~x12)"
    val clauses = parser.parse(test)
    val solver = new ConflictDrivenClauseLearning(clauses)
    println("Results:")
    println(solver.satisfiable)*/

    val test = new ImplicationFormula("a", "b")
    println(test.asAlternative.asConjunction)
  }
}
