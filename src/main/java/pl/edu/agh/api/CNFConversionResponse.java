package pl.edu.agh.api;

import pl.edu.agh.parser.transformation.CNFConverter;
import pl.edu.agh.parser.validation.FormulaValidator;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by lmarek on 24.05.2016.
 */
@XmlRootElement(name = "response")
public class CNFConversionResponse implements CNFConversionResponseFrame {
    @XmlElement(name = "input")
    protected final String input;
    @XmlElement(name = "cnf")
    protected final String cnf;
    @XmlElement(name = "parse_failed")
    protected final boolean failed;
    @XmlElement(name = "error position")
    protected final long errorPosition;
    @XmlElement(name = "error_code")
    protected final int errorCode;
    @XmlElement(name = "parser_got_instead")
    protected final char gotInstead;

    public CNFConversionResponse(String formula) {
        FormulaValidator validator = new FormulaValidator();
        scala.Tuple3<Integer, Object, Object> validationResult = validator.validate(formula);
        if (validationResult._1().intValue() > 0) {
            failed = false;
            input = formula;
            cnf = null;
            errorPosition = (Long) validationResult._3();
            errorCode = validationResult._1();
            gotInstead = (Character) validationResult._2();
        } else {
            CNFConverter converter = new CNFConverter(formula);
            input = converter.inputFormula();
            cnf = converter.asCNF();
            failed = false;
            errorPosition = 0;
            gotInstead = 0;
            errorCode = 0;
        }
    }

    public CNFConversionResponse() {
        this("");
    }

    public boolean parseFailed() {
        return failed;
    }

    public int errorCode() {
        return errorCode;
    }

    public char parserGotInstead() {
        return gotInstead;
    }

    public long parserErrorPosition() {
        return errorPosition;
    }

    public String givenFormula() {
        return input;
    }

    public String fomulaAsCNF() {
        return cnf;
    }
}
