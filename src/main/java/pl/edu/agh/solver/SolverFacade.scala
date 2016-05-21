package pl.edu.agh.solver

import pl.edu.agh.parser.transformation.ConjunctiveNormalFormParser
import pl.edu.agh.solver.algorithm.ConflictDrivenClauseLearning

/**
  * Created by lmarek on 17.05.2016.
  */
class SolverFacade(cnf: String) extends Facade {
  lazy val isSatisfiable = new ConflictDrivenClauseLearning(new ConjunctiveNormalFormParser().parse(cnf)).isSatisfiable


}
