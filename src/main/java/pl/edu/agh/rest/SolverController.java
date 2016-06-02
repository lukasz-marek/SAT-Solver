package pl.edu.agh.rest;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.agh.api.*;

/**
 * Created by lmarek on 21.05.2016.
 */
@RestController
public class SolverController {

    @RequestMapping(value = "/sat/full", method = RequestMethod.GET)
    public FullResponseFrame fullSolve(@RequestHeader(name = "formula") String formula) {
        return new SolverFullResponse(formula);
    }

    @RequestMapping(value = "/sat", method = RequestMethod.GET, produces = "application/json")
    public SATResponseFrame satSolve(@RequestHeader(name = "formula") String formula) {
        return new SATResponse(formula);
    }

    @RequestMapping(value = "/sat/dimacs", method = RequestMethod.GET)
    public DIMACSResponseFrame dimacsSolve(@RequestHeader(name = "formula") String formula) {
        return null;
    }

    @RequestMapping(value = "/cnf", method = RequestMethod.GET)
    public CNFConversionResponseFrame convertToCNF(@RequestHeader(name = "formula") String formula) {
        return new CNFConversionResponse(formula);
    }
}
