package javiergs.compiler.api;

import javiergs.compiler.lexer.Token;
import javiergs.compiler.semantic.SymbolTableItem;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.List;

/**
  * Represents the result of the compilation process,
 * including errors, tokens, symbol table, parser tree, and intermediate code.
 *
 * @author javiergs
 * @version 1.0
 */
public class CompilerResult {

  private List<CompilerError> lexicalErrors = new ArrayList<>();
  private List<CompilerError> syntaxErrors = new ArrayList<>();
  private List<CompilerError> semanticErrors = new ArrayList<>();

  private List<Token> tokens = new ArrayList<>();
  private DefaultMutableTreeNode parserTree;
  private List<SymbolTableItem> symbolTable = new ArrayList<>();
  private String intermediateCode = "";

  public List<Token> getTokens() {
    return tokens;
  }

  public void setTokens(List<Token> tokens) {
    this.tokens = tokens;
  }

  public List<CompilerError> getSyntaxErrors() {
    return syntaxErrors;
  }

  public void setSyntaxErrors(List<CompilerError> syntaxErrors) {
    this.syntaxErrors = syntaxErrors;
  }

  public List<CompilerError> getSemanticErrors() {
    return semanticErrors;
  }

  public void setSemanticErrors(List<CompilerError> semanticErrors) {
    this.semanticErrors = semanticErrors;
  }

  public List<SymbolTableItem> getSymbolTable() {
    return symbolTable;
  }

  public void setSymbolTable(List<SymbolTableItem> symbolTable) {
    this.symbolTable = symbolTable;
  }

  public DefaultMutableTreeNode getParserTree() {
    return parserTree;
  }

  public void setParserTree(DefaultMutableTreeNode parserTree) {
    this.parserTree = parserTree;
  }

  public String getIntermediateCode() {
    return intermediateCode;
  }

  public void setIntermediateCode(String intermediateCode) {
    this.intermediateCode = intermediateCode;
  }

}