import pl.edu.agh.solver.facades.DimacsFacade;
import pl.edu.agh.solver.facades.Facade;

import java.io.File;

/**
 * Created by lmarek on 22.05.2016.
 */
public class Main {
    public static void main(String[] args) {
        File f = new File("C:\\Users\\lmarek\\Desktop\\CNF\\zebra_v155_c1135.cnf");
        //System.out.println(f.exists());
        Facade ff = new DimacsFacade(f);
        System.out.println("SAT:");
        System.out.println(ff.isSatisfiable());
        if (ff.isSatisfiable()) {
            System.out.println("Assignments:");
            ff.assignments().forEach((x, y) -> System.out.println(x + ": " + y));
        }
    }
}
