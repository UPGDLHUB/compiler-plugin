package javiergs.compiler;

import javiergs.compiler.api.CompilerError;
import javiergs.compiler.api.CompilerResult;
import javiergs.compiler.api.StudentCompiler;
import javiergs.compiler.coder.CodeGenerator;
import javiergs.compiler.lexer.Lexer;
import javiergs.compiler.parser.Parser;
import javiergs.compiler.semantic.SemanticAnalyzer;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * CompilerClient is responsible for orchestrating the compilation process,
 * which includes lexing, parsing, semantic analysis, and code generation.
 *
 * @author javiergs
 * @version 1.0
 */
public class CompilerClient implements StudentCompiler {

  @Override
  public CompilerResult compile(String sourceCode) throws Exception {

    CompilerResult result = new CompilerResult();
    // Step 1: Lexer
    Lexer lexer = new Lexer(sourceCode);
    lexer.run();
    result.setTokens(lexer.getTokens());
    SemanticAnalyzer.clearAll();
    CodeGenerator.clearAll();
    // Step 2: Parser
    DefaultMutableTreeNode parserTree = Parser.run(lexer.getTokens());
    result.setParserTree(parserTree);
    // Step 3: Results
    for (int i = 0; i < Parser.getSyntaxErrors().size(); i++) {

      String message = Parser.getSyntaxErrors().get(i);
      int line = Parser.getSyntaxErrorLines().get(i);

      result.getSyntaxErrors().add(
          new CompilerError(
              "Syntax",
              message,
              line,
              0
          )
      );
    }
    for (String message : SemanticAnalyzer.getSemanticErrors()) {
      result.getSemanticErrors().add(
          new CompilerError(
              "Semantic",
              message,
              -1,
              -1
          )
      );
    }
    if (result.getSyntaxErrors().isEmpty()
        && result.getSemanticErrors().isEmpty()) {
      result.setIntermediateCode(
          CodeGenerator.getCode().toString()
      );
    } else {
      result.setIntermediateCode("");
    }
    return result;
  }

}