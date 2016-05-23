package pl.edu.agh.solver.structures

import pl.edu.agh.solver.algorithm.types.Literal

/**
  * Created by lmarek on 03.05.2016.
  */
class ImplicationGraph {
  protected val nodes = scala.collection.mutable.Stack[Node]()

  /*
  last-uip
   */
  def this(graph: ImplicationGraph) = {
    this()
    nodes.pushAll(graph.nodes)
  }

  def learntClause: Option[List[Literal]] = {
    val deduced = nodes.filter(x => x match {
      case ImplicationNode(_, _, _, _) => true
      case _ => false
    })
    for (first <- deduced) {
      for (second <- deduced if first.label == second.label && first.assignment != second.assignment) {
        val clauseLiterals = (antecedent(first) ++ antecedent(second)).distinct //deduced.filter(_.depth < depth) ++ assigned.filter(_.depth <= depth)
        implicit def booleanToSign(boolean: Boolean): Literal.Value = {
          if (boolean) Literal.NEGATIVE
          else Literal.POSITIVE
        }
        val newClause = clauseLiterals.map(node => new Literal(node.label, node.assignment)).sorted
        return Option(newClause)
      }
    }
    None
  }

  private def antecedent(conflict: Node): List[Node] = conflict match {
    case x: AssignmentNode => List(x)
    case x: ImplicationNode => {
      val queue = scala.collection.mutable.Queue[List[Node]]()
      for (node <- nodes.filter(y => x.parents.contains(y.label)))
        queue.enqueue(antecedent(node))
      queue.flatten.toList.distinct
    }
  }

  /*
  Zawsze zwraca TRUE.
   */
  def addNode(label: String, assignment: Boolean, depth: Long): Boolean = {
    val node = AssignmentNode(label, assignment, depth)
    nodes.push(node)
    true
  }

  /*
  False, jeśli istnieje node o tej samej etykiecie, ale innej wartości.
   */
  def addNode(label: String, assignment: Boolean, depth: Long, parents: List[String]): Boolean = {
    val node = ImplicationNode(label, assignment, depth, parents)
    nodes.push(node)
    if (nodes.exists(oldNode => oldNode.label == node.label && oldNode.assignment != node.assignment)) false
    else true
  }

}
