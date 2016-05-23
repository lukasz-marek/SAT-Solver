package pl.edu.agh.dimacs;

import pl.edu.agh.solver.algorithm.types.Literal;
import scala.Enumeration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lmarek on 22.05.2016.
 */
public class DimacsReader {
    protected final File file;

    public DimacsReader(File f) {
        file = f;
    }

    public DimacsReader(String path) {
        file = new File(path);
    }

    public Set<List<Literal>> read() {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Set<List<Literal>> clauses = new HashSet<>();
            String line;
            long nbvars = 0, nbclauses = 0;
            boolean found = false;
            while (!found && (line = reader.readLine()) != null) {
                line = line.toLowerCase().trim();
                if (line.matches("p cnf [0-9]+ [0-9]+")) {
                    String[] params = line.split("\\s+");
                    nbvars = Long.parseLong(params[2]);
                    nbclauses = Long.parseLong(params[3]);
                    found = true;
                }
            }
            if (!found)
                return null;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("c"))
                    continue;
                while (!line.endsWith(" 0")) {
                    String secondLine = reader.readLine();
                    if (secondLine != null)
                        line += " " + secondLine.trim();
                    else
                        break;
                }
                List<Literal> clause = parseClause(line, nbvars);
                if (!clause.isEmpty())
                    clauses.add(clause);
            }
            // if(clauses.size() == nbclauses)
            return clauses;
            //else
            //  return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected List<Literal> parseClause(String clause, long nbvars) throws Exception {
        List<Literal> literals = new ArrayList<Literal>();
        for (String variable : clause.split("\\s+")) {
            variable = variable.trim();
            if (!(variable.matches("[0-9]+") || variable.matches("-[0-9]+")))
                continue;
            long numValue = Long.parseLong(variable);
            if (numValue == 0)
                break;
            if (Math.abs(numValue) > nbvars)
                throw new Exception();
            else {
                Enumeration.Value value;
                if (numValue < 0)
                    value = Literal.NEGATIVE();
                else
                    value = Literal.POSITIVE();
                literals.add(new Literal(Long.toString(Math.abs(numValue)), value));
            }
        }
        return literals;
    }
}
