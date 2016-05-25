package pl.edu.agh.api;

/**
 * Created by lmarek on 22.05.2016.
 */
public interface CNFConversionResponseFrame {

    boolean parseFailed();

    int errorCode();

    char parserGotInstead();

    long parserErrorPosition();

    String givenFormula();

    String fomulaAsCNF();

}
