package pl.edu.agh.rest;

import org.springframework.web.bind.annotation.*;
import pl.edu.agh.api.DIMACSResponseFrame;
import pl.edu.agh.api.FullResponseFrame;
import pl.edu.agh.api.SATResponseFrame;
import pl.edu.agh.api.SolverFullResponse;
import pl.edu.agh.parser.transformation.CNFConverter;

/**
 * Created by lmarek on 21.05.2016.
 */
@RestController
public class SolverController {

    @RequestMapping(value = "/sat/full", method = RequestMethod.GET)
    public
    @ResponseBody
    FullResponseFrame fullSolve(@RequestHeader(name = "formula") String formula) {
        if (formula == null)
            return null;
        return new SolverFullResponse(formula);
    }

    @RequestMapping(value = "/sat", method = RequestMethod.GET)
    public SATResponseFrame satSolve(@RequestHeader(name = "formula") String formula) {
        return null;
    }

    @RequestMapping(value = "/sat/dimacs", method = RequestMethod.GET)
    public DIMACSResponseFrame dimacsSolve(@RequestHeader(name = "formula") String formula) {
        return null;
    }

    @RequestMapping(value = "/cnf", method = RequestMethod.GET)
    public String convertToCNF(@RequestHeader(name = "formula") String formula) {
        if (formula == null)
            return null;
        return new CNFConverter(formula).asCNF();
    }
}
