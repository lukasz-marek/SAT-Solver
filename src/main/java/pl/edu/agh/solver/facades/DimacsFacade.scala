package pl.edu.agh.solver.facades

import java.lang.Boolean
import java.util

import pl.edu.agh.dimacs.DimacsReader
import pl.edu.agh.solver.algorithm.ConflictDrivenClauseLearning

import scala.collection.JavaConversions
import scala.collection.JavaConversions._


/**
  * Created by lmarek on 22.05.2016.
  */
class DimacsFacade(dimacs: java.io.File) extends Facade {
  override lazy val isSatisfiable: scala.Boolean = if (inner == null) false else inner.isSatisfiable
  override lazy val assignments: util.Map[String, Boolean] = if (inner == null) null else mapAsJavaMap(inner.satisfyingAssignments).asInstanceOf[java.util.Map[java.lang.String, java.lang.Boolean]]
  private lazy val inner = solve()

  private def solve(): ConflictDrivenClauseLearning = {
    val reader = new DimacsReader(dimacs)
    val clauses = reader.read()
    if (clauses == null)
      return null
    val asScala = JavaConversions.asScalaSet(clauses).map(_.toList) //.filterNot(_.size == 0)
    new ConflictDrivenClauseLearning(asScala)
  }

}
