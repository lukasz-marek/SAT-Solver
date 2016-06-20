package pl.edu.agh.api;

import java.util.Map;

/**
 * Created by lmarek on 21.05.2016.
 */
public interface DIMACSResponseFrame {
    boolean isSatisfiable();
    Map<String,Boolean> satisfyingAssignments();
    boolean parseFailed();
    String getName();
}
