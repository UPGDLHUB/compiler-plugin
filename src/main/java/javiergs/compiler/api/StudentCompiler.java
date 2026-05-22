package javiergs.compiler.api;

/**
 * Interface for the student compiler, which defines the method to compile source code.
 *
 * @author javiergs
 * @version 1.0
 */
public interface StudentCompiler {

    CompilerResult compile(String sourceCode) throws Exception;

}
