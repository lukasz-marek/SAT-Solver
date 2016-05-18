package pl.edu.agh.parser.transformation

import pl.edu.agh.solver.algorithm.types.Literal

/**
  * Created by lmarek on 22.04.2016.
  */
class ConjunctiveNormalFormParser {

  def parse(cnf: String): scala.collection.mutable.Set[List[Literal]] = {
    val clauses = scala.collection.mutable.Set[List[Literal]]()
    val buffer = scala.collection.mutable.ArrayBuffer[Literal]()
    for (clause <- cnf.split("&&").toList) {
      val part = clause.replaceAll("[()]", "")
      for (literal <- part.split("\\|\\|")) {
        if (literal.startsWith("~")) buffer += new Literal(literal.replaceFirst("~", ""), Literal.NEGATIVE)
        else buffer += new Literal(literal, Literal.POSITIVE)
      }
      clauses.add(buffer.toList)
      buffer.clear()
    }
    clauses
  }
}
