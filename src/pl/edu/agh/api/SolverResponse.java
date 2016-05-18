package pl.edu.agh.api;

import pl.edu.agh.parser.transformation.CNFConverter;
import pl.edu.agh.parser.validation.FormulaValidator;
import pl.edu.agh.solver.SolverFacade;
import scala.Tuple3;

/**
 * Created by lmarek on 17.05.2016.
 */
public class SolverResponse implements ResponseFrame {
    protected final static FormulaValidator validator = new FormulaValidator();

    protected final String formula;
    protected final String asCNF;
    protected final boolean satisfiable;
    protected final boolean tautology;
    protected final boolean parseFailed;
    protected final int errorCode;
    protected final char parserGotInstead;
    protected final long parserErrorPosition;

    public SolverResponse(String formula) {
        Tuple3<Integer, Object, Object> validation = validator.validate(formula);
        if (validation._1() > 0) {
            parseFailed = true;
            errorCode = validation._1();
            parserGotInstead = (char) validation._2();
            parserErrorPosition = (long) validation._3();
            this.formula = formula;
            asCNF = null;
            satisfiable = false;
            tautology = false;
        } else {
            errorCode = 0;
            parseFailed = false;
            parserGotInstead = '\0';
            parserErrorPosition = 0;
            CNFConverter converter = new CNFConverter(formula);
            this.formula = converter.inputFormula();
            asCNF = converter.asCNF();
            satisfiable = new SolverFacade(asCNF).isSatisfiable();
            if (satisfiable) {
                converter = new CNFConverter("~(" + formula + ")");
                tautology = !new SolverFacade(converter.asCNF()).isSatisfiable();
            } else
                tautology = false;

        }
    }

    @Override
    public boolean isSatisfiable() {
        return satisfiable;
    }

    @Override
    public boolean isTautology() {
        return tautology;
    }

    @Override
    public boolean parseFailed() {
        return parseFailed;
    }

    @Override
    public int errorCode() {
        return errorCode;
    }

    @Override
    public char parserGotInstead() {
        return parserGotInstead;
    }

    @Override
    public long parserErrorPosition() {
        return parserErrorPosition;
    }

    @Override
    public String givenFormula() {
        return formula;
    }

    @Override
    public String fomulaAsCNF() {
        return asCNF;
    }
}
