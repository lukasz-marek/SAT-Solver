package pl.edu.agh.solver.algorithm

import pl.edu.agh.solver.algorithm.types.{Literal, TripleValueBoolean}
import pl.edu.agh.solver.structures.ImplicationGraph

import scala.collection.mutable

/**
  * Created by lmarek on 30.04.2016.
  */
final class ConflictDrivenClauseLearning
(private val clauses: mutable.Set[List[Literal]],
 private val assignments: Map[String, (Boolean, Long, Option[List[String]])] = Map(),
 private val depth: Long = 0
) {

  lazy val isSatisfiable = satisfiable

  private def satisfiable: Boolean = {
    var toVisit = mutable.Stack[ConflictDrivenClauseLearning]()
    toVisit.push(this)
    val sat = mutable.Set[Map[String, TripleValueBoolean]]()
    while (toVisit.nonEmpty) {
      val current = toVisit.pop()
      if (current.backtrack > 0) {
        toVisit = toVisit.filter(_.depth <= current.backtrack) //Usuwane są stany "młodsze" niż docelowy poziom nawrotu.
      }
      else {
        if (current.value == TripleValueBoolean.True) {
          return true
        } else {
          val left = current.left
          if (left.isDefined)
            toVisit.push(left.get)
          val right = current.right
          if (right.isDefined)
            toVisit.push(right.get)
        }
      }
    }
    false
  }

  private lazy val next: Option[String] = {
    val toGo = (clauses.flatten.map(_.name) -- newAssignments.keys).toList
    val assess = mutable.Map[String, Long]()
    toGo.foreach(variable => {
      clauses.foreach(clause => {
        if (clause.map(_.name).contains(variable)) {
          if (assess.contains(variable)) assess.update(variable, assess.get(variable).get + 1)
          else assess.put(variable, 1)
        }
      })
    })
    if (toGo.isEmpty) None
    else Option(toGo.sortWith((x, y) => assess.get(x).get > assess.get(y).get).head)
  }

  private def left = {
    if (next.isEmpty)
      None
    else {
      val plannedAssignments: Map[String, (Boolean, Long, Option[List[String]])] = Map(newAssignments.toSeq: _*) + (next.get ->(true, depth + 1, None))
      implicit def literalToBoolean(literal: Literal): TripleValueBoolean = {
        if (!plannedAssignments.contains(literal.name)) TripleValueBoolean.Unknown
        else {
          val value = if (plannedAssignments.get(literal.name).get._1) TripleValueBoolean.True else TripleValueBoolean.False
          if (literal.negated) !value else value
        }
      }
      lazy val negated = clauses.map(_.foldLeft(TripleValueBoolean.False)(_ || _)).foldLeft(TripleValueBoolean.True)(_ && _) == TripleValueBoolean.False
      if (backtrack < 0 && !negated) Option(new ConflictDrivenClauseLearning(clauses, plannedAssignments, depth + 1)) else None
    }
  }

  private def right = {
    if (next.isEmpty)
      None
    else {
      val plannedAssignments: Map[String, (Boolean, Long, Option[List[String]])] = Map(newAssignments.toSeq: _*) + (next.get ->(false, depth + 1, None))
      implicit def literalToBoolean(literal: Literal): TripleValueBoolean = {
        if (!plannedAssignments.contains(literal.name)) TripleValueBoolean.Unknown
        else {
          val value = if (plannedAssignments.get(literal.name).get._1) TripleValueBoolean.True else TripleValueBoolean.False
          if (literal.negated) !value else value
        }
      }
      lazy val negated = clauses.map(_.foldLeft(TripleValueBoolean.False)(_ || _)).foldLeft(TripleValueBoolean.True)(_ && _) == TripleValueBoolean.False
      if (backtrack < 0 && !negated) Option(new ConflictDrivenClauseLearning(clauses, plannedAssignments, depth + 1)) else None
    }
  }

  /*
  -1 jeśli backtracking nie następuje, w przeciwnym razie x > 0
  */
  private lazy val backtrack = propagate
  private var newAssignments: Map[String, (Boolean, Long, Option[List[String]])] = null
  private var value: TripleValueBoolean = null

  private def propagate: Long = {
    if (depth == 0) {
      newAssignments = assignments
      value = TripleValueBoolean.False
      return -1
    }
    val mutableAssignments = mutable.Map(assignments.toSeq: _*)
    implicit def literalToBoolean(literal: Literal): TripleValueBoolean = {
      if (!mutableAssignments.contains(literal.name)) TripleValueBoolean.Unknown
      else {
        val value = if (mutableAssignments.get(literal.name).get._1) TripleValueBoolean.True else TripleValueBoolean.False
        if (literal.negated) !value else value
      }
    }
    val tree = new ImplicationGraph
    for (assignment <- mutableAssignments) {
      val label = assignment._1
      if (assignment._2._3.isEmpty)
        tree.addNode(label, assignment._2._1, assignment._2._2)
      else
        tree.addNode(label, assignment._2._1, assignment._2._2, assignment._2._3.get)
    } //Po wyjściu z funkcji gotowy jest graf

    def unit(clause: List[Literal]) = clause.map(_.name).filterNot(mutableAssignments.keys.toSet).length == 1
    var continue = true
    while (clauses.count(unit(_)) > 0 && continue) {
      continue = false
      for (clause <- clauses if unit(clause)) {
        val clauseValue = clause.foldLeft(TripleValueBoolean.False)(_ || _)
        if (clauseValue == TripleValueBoolean.Unknown) {
          val toFill = clause.filterNot(literal => mutableAssignments.contains(literal.name)).head
          val parents = clause.map(_.name).filterNot(Set(toFill.name)).distinct
          val value = if (toFill.negated) false else true
          mutableAssignments.put(toFill.name, (value, depth, Option(parents)))
          tree.addNode(toFill.name, value, depth, parents)
          for (clause <- clauses if clause.foldLeft(TripleValueBoolean.False)(_ || _) == TripleValueBoolean.False) {
            val parents = clause.map(_.name).filterNot(Set(toFill.name))
            tree.addNode(toFill.name, !value, depth, parents)
            val conflict = tree.lastUIPLearntClause
            if (conflict.get._2.nonEmpty)
              clauses.add(conflict.get._2)
            newAssignments = mutableAssignments.toMap
            return conflict.get._1
          }
        }
      }
    }
    value = clauses.map(_.foldLeft(TripleValueBoolean.False)(_ || _)).foldLeft(TripleValueBoolean.True)(_ && _)
    newAssignments = mutableAssignments.toMap
    -1
  }
}
