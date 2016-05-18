package pl.edu.agh.parser.transformation

import pl.edu.agh.parser.transformation.tree.RootFormula

/**
  * Created by lmarek on 18.05.2016.
  */

class CNFConverter(private val formula: String) {
  lazy val inputFormula = new RootFormula(formula).toString
  lazy val asCNF = {
    val root = new RootFormula(formula)
    root.convertToCNF()
    new ConversionPostprocessor().postprocess(root.toString)
  }
}
