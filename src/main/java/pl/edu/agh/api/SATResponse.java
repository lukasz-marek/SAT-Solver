package pl.edu.agh.api;

import pl.edu.agh.parser.transformation.CNFConverter;
import pl.edu.agh.parser.validation.FormulaValidator;
import pl.edu.agh.solver.facades.SolverFacade;
import scala.Tuple3;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Map;

/**
 * Created by lmarek on 21.05.2016.
 */
@XmlRootElement(name = "response")
public class SATResponse implements SATResponseFrame {
    @XmlTransient
    protected final static FormulaValidator validator = new FormulaValidator();
    @XmlElement(name = "input")
    protected final String formula;
    @XmlElement(name = "cnf")
    protected final String asCNF;
    @XmlElement(name = "satisfiable")
    protected final boolean satisfiable;
    @XmlElement(name = "parse_failed")
    protected final boolean parseFailed;
    @XmlElement(name = "error_code")
    protected final int errorCode;
    @XmlElement(name = "misplaced_symbol")
    protected final char parserGotInstead;
    @XmlElement(name = "error_position")
    protected final long parserErrorPosition;
    @XmlElement(name = "satisfying_assignments")
    protected final Map<String, Boolean> assignments;

    public boolean isSatisfiable() {
        return satisfiable;
    }

    public Map<String, Boolean> assignments() {
        return assignments;
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

    public SATResponse(String formula) {
        Tuple3<Integer, Object, Object> validation = validator.validate(formula);
        if (validation._1() > 0) {
            parseFailed = true;
            errorCode = validation._1();
            parserGotInstead = (Character) validation._2();
            parserErrorPosition = (Long) validation._3();
            this.formula = formula;
            asCNF = null;
            satisfiable = false;
            assignments = null;
        } else {
            errorCode = 0;
            parseFailed = false;
            parserGotInstead = '\0';
            parserErrorPosition = 0;
            CNFConverter converter = new CNFConverter(formula);
            this.formula = converter.inputFormula();
            asCNF = converter.asCNF();
            SolverFacade facade = new SolverFacade(asCNF);
            satisfiable = facade.isSatisfiable();
            if (satisfiable) {
                assignments = facade.assignments();
            } else
                assignments = null;

        }
    }

    public SATResponse() {
        this(null);
    }
}
