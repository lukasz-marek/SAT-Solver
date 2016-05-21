package pl.edu.agh.api;

import pl.edu.agh.parser.transformation.CNFConverter;
import pl.edu.agh.parser.validation.FormulaValidator;
import pl.edu.agh.solver.SolverFacade;
import scala.Tuple3;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by lmarek on 17.05.2016.
 */
@XmlRootElement(name = "response")
public class SolverFullResponse implements FullResponseFrame {
    @XmlTransient
    protected final static FormulaValidator validator = new FormulaValidator();
    @XmlElement(name = "input")
    protected final String formula;
    @XmlElement(name = "cnf")
    protected final String asCNF;
    @XmlElement(name = "satisfiable")
    protected final boolean satisfiable;
    @XmlElement(name = "tautology")
    protected final boolean tautology;
    @XmlElement(name = "parse_failed")
    protected final boolean parseFailed;
    @XmlElement(name = "error_code")
    protected final int errorCode;
    @XmlElement(name = "misplaced_symbol")
    protected final char parserGotInstead;
    @XmlElement(name = "error_position")
    protected final long parserErrorPosition;

    public SolverFullResponse(String formula) {
        Tuple3<Integer, Object, Object> validation = validator.validate(formula);
        if (validation._1() > 0) {
            parseFailed = true;
            errorCode = validation._1();
            parserGotInstead = (Character) validation._2();
            parserErrorPosition = (Long) validation._3();
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

    public SolverFullResponse() {
        this(null);
    }

    public boolean isSatisfiable() {
        return satisfiable;
    }

    public boolean isTautology() {
        return tautology;
    }

    public boolean parseFailed() {
        return parseFailed;
    }

    public int errorCode() {
        return errorCode;
    }

    public char parserGotInstead() {
        return parserGotInstead;
    }

    public long parserErrorPosition() {
        return parserErrorPosition;
    }

    public String givenFormula() {
        return formula;
    }

    public String fomulaAsCNF() {
        return asCNF;
    }
}
