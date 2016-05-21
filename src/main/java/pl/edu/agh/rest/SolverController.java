package pl.edu.agh.rest;

import org.springframework.web.bind.annotation.*;
import pl.edu.agh.api.*;
import pl.edu.agh.parser.transformation.CNFConverter;
import pl.edu.agh.parser.validation.FormulaValidator;

/**
 * Created by lmarek on 21.05.2016.
 */
@RestController
public class SolverController {

    @RequestMapping(value = "/sat/full", method = RequestMethod.GET)
    public
    @ResponseBody
    FullResponseFrame fullSolve(@RequestHeader(name = "formula") String formula) {
        return new SolverFullResponse(formula);
    }

    @RequestMapping(value = "/sat", method = RequestMethod.GET)
    public
    @ResponseBody
    SATResponseFrame satSolve(@RequestHeader(name = "formula") String formula) {
        return new SATResponse(formula);
    }

    @RequestMapping(value = "/sat/dimacs", method = RequestMethod.GET)
    public DIMACSResponseFrame dimacsSolve(@RequestHeader(name = "formula") String formula) {
        return null;
    }

    @RequestMapping(value = "/cnf", method = RequestMethod.GET)
    public String convertToCNF(@RequestHeader(name = "formula") String formula) {
        FormulaValidator validator = new FormulaValidator();
        if (validator.validate(formula)._1() > 0)
            return null;
        return new CNFConverter(formula).asCNF();
    }
}
