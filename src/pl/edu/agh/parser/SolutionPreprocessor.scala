package pl.edu.agh.parser

import pl.edu.agh.data.reasoning.structures.Literal


/**
  * Created by lmarek on 10.05.2016.
  */
class SolutionPreprocessor {

  def preprocess(clauses: scala.collection.mutable.Set[List[Literal]]): scala.collection.mutable.Set[List[Literal]] = {
    val newClauses = scala.collection.mutable.Set[List[Literal]]()
    for (clause <- clauses) {
      val newClause = clause.distinct
      if (!newClause.exists(literal => newClause.contains(!literal)))
        newClauses.add(newClause)
    }
    newClauses
  }
}
