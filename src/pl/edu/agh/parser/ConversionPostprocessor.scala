package pl.edu.agh.parser

/**
  * Created by lmarek on 10.05.2016.
  */
class ConversionPostprocessor(cnf: String) {

  lazy val postprocess: String = {
    val formulaBuffer = StringBuilder.newBuilder
    val processedString = cnf.replaceAll("[()]", "")
    for (clause <- processedString.split("&&")) {
      val clauseBuffer = StringBuilder.newBuilder
      val vars = clause.split("\\|\\|")
      clauseBuffer.append("(")
      for (variable <- vars.sorted if !clauseBuffer.iterator.contains(variable))
        clauseBuffer.append(variable).append("||")
      clauseBuffer.append(")")
      val asString = clauseBuffer.toString()
      if (!formulaBuffer.iterator.contains(asString))
        formulaBuffer.append(asString)
    }
    formulaBuffer.toString().replaceAll("\\|\\|\\)", ")").replaceAll("\\)\\(", "\\)&&\\(")
  }

}
