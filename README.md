
This guide explains how to make your compiler compatible with the **UP Compiler IntelliJ Plugin**.

The plugin does **not** require you to use the same internal compiler structure as the example compiler. Your compiler may have its own lexer, parser, semantic analyzer, code generator, classes, packages, and algorithms.

The only requirement is that your project provides one public adapter class that implements the shared API interface:

```java
javiergs.compiler.api.StudentCompiler
```

## Files Provided by the Instructor

Share these files with students:

```text
UPCompilerPlugin.zip
```

The IntelliJ plugin package. Students install this in IntelliJ IDEA.

```text
javiergs/compiler/api/
```

The API package that student compilers must use. It contains:

```text
StudentCompiler.java
CompilerResult.java
CompilerError.java
```

You may also share it as a small API JAR.

```text
simple-compiler-1.0.0-obfuscated.jar
```

An example compatible compiler. This is only for testing the plugin. Students do not need to copy its internal structure.

## What Your Compiler Must Do

Your compiler must expose one class that implements:

```java
package javiergs.compiler.api;

public interface StudentCompiler {
    CompilerResult compile(String sourceCode) throws Exception;
}
```

The plugin calls:

```java
compile(sourceCode)
```

and your compiler returns a `CompilerResult`.

## Step 1: Add the API Package

Copy this package into your compiler project:

```text
src/main/java/javiergs/compiler/api/
```

Do not change the package name. It must remain:

```java
package javiergs.compiler.api;
```

## Step 2: Create Your Compiler Adapter Class

Your compiler may have any internal structure, but it needs one adapter class.

Example:

```java
package team1.compiler;

import javiergs.compiler.api.CompilerResult;
import javiergs.compiler.api.StudentCompiler;

public class TeamCompiler implements StudentCompiler {

    @Override
    public CompilerResult compile(String sourceCode) throws Exception {

        CompilerResult result = new CompilerResult();

        // 1. Run your lexer
        // 2. Run your parser
        // 3. Run semantic analysis
        // 4. Run code generation
        // 5. Store all outputs in CompilerResult

        return result;
    }
}
```

This class is the class name students will enter in the plugin settings.

## Step 3: Return Tokens

If your lexer produces tokens, store them in the result:

```java
result.setTokens(tokens);
```

Each token should provide at least:

```text
lexeme / word
token type
line number
```

Example token display:

```text
x      IDENTIFIER    line 2
=      OPERATOR      line 2
10     INTEGER       line 2
;      DELIMITER     line 2
```

## Step 4: Return Syntax Errors

Syntax errors should be added as `CompilerError` objects:

```java
result.getSyntaxErrors().add(
    new CompilerError(
        "Syntax",
        "Line 3:[Syntactic] expected ;",
        3,
        0
    )
);
```

Use column `0` if your compiler does not track columns.

## Step 5: Return Semantic Errors

Semantic errors should also be added as `CompilerError` objects:

```java
result.getSemanticErrors().add(
    new CompilerError(
        "Semantic",
        "Line 4:[Semantic] variable <y> not found",
        4,
        0
    )
);
```

Examples:

```text
variable already defined
variable not found
type mismatch
expected boolean
invalid assignment
```

## Step 6: Return a Syntax Tree

The plugin supports syntax trees using:

```java
javax.swing.tree.DefaultMutableTreeNode
```

Example:

```java
DefaultMutableTreeNode root = new DefaultMutableTreeNode("PROGRAM");
DefaultMutableTreeNode body = new DefaultMutableTreeNode("BODY");
root.add(body);

result.setParserTree(root);
```

If your parser does not generate a tree yet, return:

```java
result.setParserTree(
    new DefaultMutableTreeNode("No syntax tree available")
);
```

## Step 7: Return Generated Code

If compilation succeeds, store your intermediate code or P-Code:

```java
result.setIntermediateCode(
    "x, int, global, 0\n" +
    "@\n" +
    "LIT 10, 0\n" +
    "STO x, 0\n" +
    "OPR 0, 0\n"
);
```

If there are errors, return an empty string:

```java
result.setIntermediateCode("");
```

The plugin opens a new editor tab with generated code only when there are no errors.

## Step 8: Build Your Compiler JAR

For Maven projects:

```bash
mvn clean package
```

The JAR will usually be created here:

```text
target/
```

Example:

```text
target/team1-compiler-1.0.0.jar
```

## Step 9: Configure the IntelliJ Plugin

In IntelliJ IDEA:

```text
Settings → Tools → UP Compiler
```

Set:

```text
Compiler JAR
```

to your generated JAR path.

Example:

```text
/Users/student/Documents/team1-compiler/target/team1-compiler-1.0.0.jar
```

Set:

```text
Compiler Class
```

to the full adapter class name.

Example:

```text
team1.compiler.TeamCompiler
```

## Step 10: Run Your Compiler

Open a source file, for example:

```text
test.up
```

Example input:

```text
{
    int x;
    x = 10;
}
```

Then run:

```text
Tools → Run Student Compiler
```

The plugin will show:

```text
Errors tab
Tokens tab
Syntax Tree tab
```

If compilation succeeds, generated P-Code opens automatically in a new editor tab.

## Important Rules

Your compiler must:

```text
1. Include the API package.
2. Provide one public class implementing StudentCompiler.
3. Have a public no-argument constructor.
4. Return a CompilerResult.
5. Add errors as CompilerError objects.
6. Build a JAR file.
7. Use the correct full class name in the plugin settings.
```

Your compiler does not need to:

```text
1. Use the same lexer as the example.
2. Use the same parser as the example.
3. Use the same semantic analyzer.
4. Use the same code generator.
5. Use the same package structure internally.
```

Only the adapter class must follow the plugin API.

## Minimal Compatible Compiler Example

```java
package my.compiler;

import javiergs.compiler.api.CompilerResult;
import javiergs.compiler.api.StudentCompiler;

import javax.swing.tree.DefaultMutableTreeNode;

public class MyCompiler implements StudentCompiler {

    @Override
    public CompilerResult compile(String sourceCode) {

        CompilerResult result = new CompilerResult();

        result.setParserTree(
            new DefaultMutableTreeNode("PROGRAM")
        );

        result.setIntermediateCode(
            "@\nOPR 0, 0\n"
        );

        return result;
    }
}
```

Plugin setting:

```text
Compiler Class:
my.compiler.MyCompiler
```

## Recommended Project Structure

```text
team-compiler/
├── pom.xml
└── src/main/java/
    ├── javiergs/compiler/api/
    │   ├── StudentCompiler.java
    │   ├── CompilerResult.java
    │   └── CompilerError.java
    │
    └── team1/compiler/
        ├── TeamCompiler.java
        ├── lexer/
        ├── parser/
        ├── semantic/
        └── codegen/
```

## Troubleshooting

### The plugin says: class not found

Use the full class name.

Incorrect:

```text
TeamCompiler
```

Correct:

```text
team1.compiler.TeamCompiler
```

### The plugin says: does not implement StudentCompiler

Your adapter class must include:

```java
implements StudentCompiler
```

and must import:

```java
import javiergs.compiler.api.StudentCompiler;
```

### The plugin does not show new changes

Rebuild your compiler JAR:

```bash
mvn clean package
```

Then run the plugin again. Usually you do not need to restart IntelliJ.

### The generated code does not open

Check the Errors tab. The plugin opens generated code only when there are no syntax or semantic errors.

### The line number is -1

Create errors with the real line number:

```java
new CompilerError("Syntax", message, lineNumber, 0)
```

not:

```java
new CompilerError("Syntax", message, -1, -1)
```

## Final Reminder

Your compiler may be designed in any way you want.

The plugin only needs this:

```java
public class YourCompiler implements StudentCompiler {
    public CompilerResult compile(String sourceCode) throws Exception {
        ...
    }
}
```

Everything else is your compiler design.
