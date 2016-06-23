package pl.edu.agh.rest;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.agh.api.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

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
    public
    @ResponseBody
    SATResponseFrame satSolve(@RequestHeader(name = "formula") String formula) {
        SATResponse response = new SATResponse(formula);
        return response;
    }

    public DIMACSResponseFrame dimacsSolve(@RequestHeader(name = "formula") String formula) {
        return null;
    }

    @RequestMapping(value = "/cnf", method = RequestMethod.GET)
    public CNFConversionResponseFrame convertToCNF(@RequestHeader(name = "formula") String formula) {
        return new CNFConversionResponse(formula);
    }

    @RequestMapping(value = "/sat/dimacs", method = RequestMethod.POST)
    public DIMACSResponseFrame handleFileUpload(@RequestParam("file") MultipartFile file) {
        File source;
        try {
            source = File.createTempFile("dimacs", ".cnf");
            BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(source));
            FileCopyUtils.copy(file.getInputStream(), stream);
            stream.close();
            DimacsResponse response = new DimacsResponse(source,source.getName());
            source.delete();
            return response;

        } catch (Exception e) {
            return null;
        }
    }
}
