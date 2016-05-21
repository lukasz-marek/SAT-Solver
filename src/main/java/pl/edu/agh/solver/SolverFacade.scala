package pl.edu.agh.solver

import pl.edu.agh.parser.transformation.ConjunctiveNormalFormParser
import pl.edu.agh.solver.algorithm.ConflictDrivenClauseLearning

import scala.collection.JavaConversions._

/**
  * Created by lmarek on 17.05.2016.
  */
class SolverFacade(cnf: String) extends Facade {
  private lazy val internalState = new ConflictDrivenClauseLearning(new ConjunctiveNormalFormParser().parse(cnf))
  lazy val isSatisfiable = internalState.isSatisfiable
  lazy val assignments = mapAsJavaMap(internalState.satisfyingAssignments).asInstanceOf[java.util.Map[java.lang.String, java.lang.Boolean]]


}
