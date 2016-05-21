package pl.edu.agh.api;

/**
 * Created by lmarek on 21.05.2016.
 */
public interface SATResponseFrame {
    boolean isSatisfiable();

    boolean parseFailed();

    int errorCode();

    char parserGotInstead();

    long parserErrorPosition();
}
