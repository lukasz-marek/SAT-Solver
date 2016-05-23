package pl.edu.agh.solver.facades

/**
  * Created by lmarek on 30.04.2016.
  */
trait Facade {

  def isSatisfiable: Boolean

  def assignments: java.util.Map[java.lang.String, java.lang.Boolean]

}
