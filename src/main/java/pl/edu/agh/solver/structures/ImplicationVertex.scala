package pl.edu.agh.solver.structures

/**
  * Created by lmarek on 03.05.2016.
  */
abstract class Node(val label: String, val assignment: Boolean, val depth: Long) {}

case class AssignmentNode(override val label: String, override val assignment: Boolean, override val depth: Long) extends Node(label, assignment, depth) {}

case class ImplicationNode(override val label: String, override val assignment: Boolean, override val depth: Long, val parents: List[String]) extends Node(label, assignment, depth) {}
