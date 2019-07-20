. . . . . . . . . . . .
    VASCAL COMPILER
. . . . . . . . . . . .
   BY JOHN MACDOUGALL
. . . . . . . . . . . .

0) RUNNER
___________________
The runner class is the main class of the Compiler, instantiating a Compiler object with a filename, which in turn 
instantiates the CharStream, LexicalAnalyzer, Parser, and SemanticAction objects to parse the file.

*** To pass in any input file, simply change the FILENAME variable to the file path of your choice. ***
*** Sample test files are included in Lex_Tests and Misc_Tests ***

1) LEXICAL ANALYZER
___________________
The Lexer returns one token at a time via the GetNextToken() function.

VALID CHARS: 
"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890" +
".,;:<>/*[]+-=()}{\t ";

ERROR TYPES:
-Identifier too long or Constant (int or real) too long
-Ill-formed comment (anytime you see a ‘}’ that doesn’t end a comment
-Invalid char (if the char passed by getChar() doesn’t match any of the cases defined by readLetter, readNumber, and readSymbol


2) PARSER
___________________
The parser instantiates the Lexical Analyzer and calls getNextToken() until the end of file is reached.

*** The PRINT boolean at the top of the Parser class can be changed to true to print the contents of the stack ***
*** The SEMPRINT boolean can likewise be used to print the contents of the semantic stack ***

ERROR TYPES:
-The lookahead token and top of stack symbol don’t match any production rules (999)
-Invalid token 
-Invalid top of stack symbol


3) SEMANTIC ACTIONS
___________________
The Semantic Actions work alongside the NonTerminals and Tokens in order to specify the behavior of specific data types
including arrays, constants, functions and their parameters, IO devices, and variables.

ERROR TYPES:
-Illegal operation
-Type mismatch
-EType mismatch (relational/arithmetic inconsistency)
-Undefined variable
-Incorrect parameters, type or number
