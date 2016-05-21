package pl.edu.agh.parser.transformation

/**
  * Created by lmarek on 10.05.2016.
  */
class ConversionPostprocessor {

  /*def postprocess(cnf: String): String = {
   /* val formulaBuffer = StringBuilder.newBuilder
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
    */
    cnf
  }
  */


  def postprocess(cnf: String): String = {
    val clauses = cnf.split("&&").map(_.replaceAll("[()]", ""))
    val distinct = collection.mutable.Set[String]()
    val globalBuffer = StringBuilder.newBuilder
    val localBuffer = StringBuilder.newBuilder
    for (clause <- clauses) {
      val literals = clause.split("\\|\\|").distinct.sorted
      if (literals.nonEmpty) {
        localBuffer.append("(")
        for (literal <- literals)
          localBuffer.append(literal).append("||")
        localBuffer.append(")")
        val producedClause = localBuffer.toString()
        if (!distinct.contains(producedClause)) {
          distinct.add(producedClause)
          globalBuffer.append(producedClause)
        }
        localBuffer.clear()
      }
    }
    globalBuffer.toString().replaceAll("\\|\\|\\)", ")").replaceAll("\\)\\(", "\\)&&\\(")
  }
}
