package pl.edu.agh.parser

/**
  * Created by lmarek on 10.05.2016.
  */
class ConversionPostprocessor {

  def postprocess(cnf: String): String = {
    val formulaBuffer = StringBuilder.newBuilder
    val processedString = cnf.replaceAll("[()]", "")
    for (clause <- processedString.split("&&")) {
      val clauseBuffer = StringBuilder.newBuilder
      val vars = clause.split("\\|\\|")
      clauseBuffer.append("(")
      for (variable <- vars)
        clauseBuffer.append(variable).append("||")
      clauseBuffer.append(")")
      formulaBuffer.append(clauseBuffer.toString())
    }
    formulaBuffer.toString().replaceAll("\\|\\|\\)", ")").replaceAll("\\)\\(", "\\)&&\\(")
  }

}
