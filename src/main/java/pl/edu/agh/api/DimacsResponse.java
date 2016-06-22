package pl.edu.agh.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.sun.org.apache.xpath.internal.operations.Bool;
import pl.edu.agh.dimacs.DimacsReader;
import pl.edu.agh.solver.algorithm.types.Literal;
import pl.edu.agh.solver.facades.DimacsFacade;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lmarek on 20.06.2016.
 */
@JsonRootName("response")
public class DimacsResponse implements DIMACSResponseFrame {
    @JsonProperty
    protected final boolean satisfiable;
    @JsonProperty
    protected final Map<String,Boolean> assignments;
    @JsonProperty
    protected final boolean parseFailed;
    @JsonProperty
    protected final String name;
    public DimacsResponse(){this(null,null);};
    public DimacsResponse(File cnf, String name){
        DimacsFacade facade = new DimacsFacade(cnf);
        this.name = name;
        if(facade.failed()){
            parseFailed = true;
            assignments = null;
            satisfiable = false;
        }else{
            parseFailed = false;
            assignments = facade.assignments();
            satisfiable = facade.isSatisfiable();
        }
    }

    @Override
    public boolean isSatisfiable() {
        return satisfiable;
    }

    @Override
    public Map<String, Boolean> satisfyingAssignments() {
        return assignments;
    }

    @Override
    public boolean parseFailed() {
        return parseFailed;
    }

    @Override
    public String getName() {
        return name;
    }
}
