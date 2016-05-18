package pl.edu.agh.api;

/**
 * Created by lmarek on 17.05.2016.
 */
public interface ResponseFrame {
    boolean isSatisfiable();

    boolean isTautology();

    boolean parseFailed();

    int errorCode();

    char parserGotInstead();

    long parserErrorPosition();

    String givenFormula();

    String fomulaAsCNF();
}
