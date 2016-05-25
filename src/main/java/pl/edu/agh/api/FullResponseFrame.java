package pl.edu.agh.api;

import java.util.Map;

/**
 * Created by lmarek on 17.05.2016.
 */
public interface FullResponseFrame {
    boolean isSatisfiable();

    boolean isTautology();

    boolean parseFailed();

    int errorCode();

    char parserGotInstead();

    long parserErrorPosition();

    String givenFormula();

    String fomulaAsCNF();

    Map<String, Boolean> satisfyingAssignments();
}
