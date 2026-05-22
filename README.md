
This repository contains the IntelliJ plugin and API required to connect your compiler to the UP Compiler Plugin.
The repository does **not** include the instructor compiler source code.
Students are expected to build and integrate their own:

- Lexer
- Parser
- Semantic Analyzer
- Code Generator

The plugin only requires a small compatibility layer through the provided API.


# What Is Included

The repository contains:

```text
javiergs/compiler/api/
```

with:

```text
CompilerError.java
CompilerResult.java
StudentCompiler.java
```

These classes define the communication contract between the IntelliJ plugin and your compiler.


# What Students Must Build

Students must implement their own compiler project independently.

Your compiler may have any internal structure you want.

Example:

```text
my-compiler/
├── lexer/
├── parser/
├── semantic/
├── codegen/
└── MyCompiler.java
```


# Plugin Compatibility Requirement

Your compiler must provide one public class implementing:

```java
javiergs.compiler.api.StudentCompiler
```

Example:

```java
package team1.compiler;

import javiergs.compiler.api.CompilerResult;
import javiergs.compiler.api.StudentCompiler;

public class TeamCompiler implements StudentCompiler {

    @Override
    public CompilerResult compile(String sourceCode) throws Exception {

        CompilerResult result = new CompilerResult();

        // Run lexer
        // Run parser
        // Run semantic analysis
        // Run code generation

        return result;
    }
}
```

This class acts as the adapter between your compiler and the IntelliJ plugin.


# Step 1 – Copy the API Package

Copy:

```text
javiergs/compiler/api/
```

into your compiler project:

```text
src/main/java/javiergs/compiler/api/
```

Do not rename the package.

It must remain:

```java
package javiergs.compiler.api;
```


# Step 2 – Implement StudentCompiler

Your compiler adapter class must:

```text
1. Implement StudentCompiler
2. Provide a public no-argument constructor
3. Return CompilerResult
```

Example:

```java
public class TeamCompiler implements StudentCompiler
```

---

# Step 3 – Return Tokens

If your lexer generates tokens, return them:

```java
result.setTokens(tokens);
```

Each token should contain at least:

```text
lexeme
token type
line number
```

Example:

```text
x      IDENTIFIER    line 2
=      OPERATOR      line 2
10     INTEGER       line 2
```

---

# Step 4 – Return Syntax Errors

Syntax errors must be stored as `CompilerError` objects.

Example:

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


# Step 5 – Return Semantic Errors

Semantic errors must also be stored as `CompilerError`.

Example:

```java
result.getSemanticErrors().add(
    new CompilerError(
        "Semantic",
        "Line 4:[Semantic] variable <x> not found",
        4,
        0
    )
);
```

Examples of semantic errors:

```text
variable not found
variable already defined
type mismatch
expected boolean
```


# Step 6 – Return a Syntax Tree

The plugin supports syntax trees using:

```java
javax.swing.tree.DefaultMutableTreeNode
```

Example:

```java
DefaultMutableTreeNode root =
    new DefaultMutableTreeNode("PROGRAM");

result.setParserTree(root);
```


# Step 7 – Return Intermediate Code

If compilation succeeds:

```java
result.setIntermediateCode(
    "@\n" +
    "LIT 10, 0\n" +
    "STO x, 0\n" +
    "OPR 0, 0\n"
);
```

If compilation fails:

```java
result.setIntermediateCode("");
```

The plugin opens generated code automatically when no syntax or semantic errors exist.


# Step 8 – Build Your Compiler JAR

For Maven projects:

```bash
mvn clean package
```

The generated JAR is usually located in:

```text
target/
```

Example:

```text
target/team1-compiler-1.0.0.jar
```


# Step 9 – Configure the Plugin

Inside IntelliJ IDEA:

```text
Settings → Tools → UP Compiler
```

Configure:

## Compiler JAR

Example:

```text
/Users/student/Documents/team1-compiler/target/team1-compiler-1.0.0.jar
```

## Compiler Class

Use the full class name.

Example:

```text
team1.compiler.TeamCompiler
```


# Step 10 – Run the Compiler

Open a source file.

Example:

```text
{
    int x;
    x = 10;
}
```

Run:

```text
Tools → Run Student Compiler
```

The plugin displays:

- Errors
- Tokens
- Syntax Tree

If compilation succeeds, the generated intermediate code opens automatically in a new editor tab.


# Minimal Compatible Compiler

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
my.compiler.MyCompiler
```


# Troubleshooting

## Class Not Found

Incorrect:

```text
TeamCompiler
```

Correct:

```text
team1.compiler.TeamCompiler
```

---

## Does Not Implement StudentCompiler

Your class must include:

```java
implements StudentCompiler
```

and import:

```java
import javiergs.compiler.api.StudentCompiler;
```

---

## Changes Not Appearing

Rebuild your JAR:

```bash
mvn clean package
```

Then run the plugin again.

---

## Generated Code Does Not Open

Check the Errors tab.

Generated code only opens when there are no syntax or semantic errors.

---

# Final Reminder

The plugin only requires this:

```java
public class YourCompiler implements StudentCompiler {
    public CompilerResult compile(String sourceCode) throws Exception {
        ...
    }
}
```

Everything else is your compiler design.
