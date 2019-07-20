package compiler;

import java.io.*;
import java.util.*;

public class Parser 
{
	private Stack<RSymbol> Parsed;

	private int Count;
	
	private LexicalAnalyzer LA;
	private SemanticAction SA;
	
	private Token LastSeen;
	private Token TwoTokensAgo;
	
	//if true, prints the stack after each production
	private boolean PRINT = false;
	//if true, prints the semantic stack after each action
	private boolean SEMPRINT = false;
	
	//Gives the index of the lookahead Token based on its position in the ParseTable
	private TokenType[] TokenIndeces = new TokenType[] 
			{
			TokenType.PROGRAM, TokenType.BEGIN, TokenType.END, TokenType.VAR, TokenType.FUNCTION,
			TokenType.PROCEDURE, TokenType.RESULT, TokenType.INTEGER, TokenType.REAL, TokenType.ARRAY, 
			TokenType.OF, TokenType.IF, TokenType.THEN, TokenType.ELSE, TokenType.WHILE, TokenType.DO,
			TokenType.NOT, TokenType.IDENTIFIER, TokenType.INTCONSTANT, TokenType.REALCONSTANT,
			TokenType.RELOP, TokenType.MULOP, TokenType.ADDOP, TokenType.ASSIGNOP, TokenType.COMMA, 
			TokenType.SEMICOLON, TokenType.COLON, TokenType.LEFTPAREN, TokenType.RIGHTPAREN, 
			TokenType.LEFTBRACKET, TokenType.RIGHTBRACKET, TokenType.UNARYMINUS, TokenType.UNARYPLUS, 
			TokenType.DOUBLEDOT, TokenType.ENDMARKER
	};

	//Gives the index of the top of stack RSymbol based on its position in the ParseTable
	private RSymbol[] NonIndeces = new RSymbol[] 
			{
			NonTerminal.program, NonTerminal.identifier_list, NonTerminal.declarations, NonTerminal.sub_declarations,
			NonTerminal.compound_statement, NonTerminal.identifier_list_tail, NonTerminal.declaration_list,
			NonTerminal.type, NonTerminal.declaration_list_tail, NonTerminal.standard_type, NonTerminal.array_type,
			NonTerminal.subprogram_declaration, NonTerminal.subprogram_head, NonTerminal.arguments,
			NonTerminal.parameter_list, NonTerminal.parameter_list_tail, NonTerminal.statement_list, NonTerminal.statement, 
			NonTerminal.statement_list_tail, NonTerminal.elementary_statement, NonTerminal.expression, NonTerminal.else_clause,
			NonTerminal.es_tail, NonTerminal.subscript, NonTerminal.parameters, NonTerminal.expression_list, 
			NonTerminal.expression_list_tail, NonTerminal.simple_expression, NonTerminal.expression_tail, NonTerminal.term,
			NonTerminal.simple_expression_tail, NonTerminal.sign, NonTerminal.factor, NonTerminal.term_tail,
			NonTerminal.factor_tail, NonTerminal.actual_parameters, NonTerminal.Goal, NonTerminal.constant
	};

	//Given valid indeces from both the lookahead Token and top of stack Symbol, returns an error (999), an epsilon production (negative number),
	//	or the index of a valid right hand production from the rightProductions table
	private int[][] ParseTable = new int[][] 
			{
		{1,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,65,999},
		{999,999,-6,-16,25,999,999,999,-9,999,999,999,999,999,999,999,26,29,999,35,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,-28,999,999,33,37,39,41,999,999,999,-47,999,-51,999,999,-54,60,62,999,999},
		{999,999,5,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999},
		{999,999,-6,15,999,999,999,999,-9,999,999,17,18,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999},
		{999,999,-6,15,999,999,999,999,-9,999,999,17,19,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999},
		{999,999,999,999,999,999,999,10,999,12,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999},
		{999,999,999,999,999,999,999,10,999,13,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999},
		{999,999,999,999,999,999,999,11,999,999,14,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,26,30,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,39,999,999,999,999,-47,999,-51,999,999,-54,60,62,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,32,37,39,41,999,999,999,-47,999,-51,999,999,-54,60,62,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,26,31,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,39,999,999,999,999,-47,999,-51,999,999,-54,60,62,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,45,999,999,999,999,42,999,48,999,52,999,999,58,999,999,999,999,999},
		{999,2,999,999,999,999,7,999,8,999,999,999,999,999,22,999,26,29,999,34,45,999,999,999,999,42,999,48,999,52,999,999,55,999,999,999,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,45,999,999,999,999,42,999,48,999,52,999,999,56,999,999,999,999,66},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,45,999,999,999,999,42,999,48,999,52,999,999,56,999,999,999,999,67},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,39,999,999,999,999,46,999,-51,999,999,-54,60,62,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,39,999,999,999,999,999,999,999,999,999,53,60,62,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,39,999,999,999,999,999,999,50,999,999,-54,60,62,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,36,39,999,999,999,999,999,999,999,999,999,999,999,999,999,999},
		{999,999,999,999,999,3,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,39,999,999,43,999,-47,999,-51,999,999,-54,60,62,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,-21,999,23,999,999,27,999,999,33,37,39,41,999,999,999,-47,999,-51,999,999,-54,60,62,999,999},
		{999,999,999,999,999,-4,999,999,999,999,999,999,999,-21,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,20,999,999,999,999,999,999,45,999,37,999,40,42,999,48,999,52,999,999,57,999,59,61,999,999},
		{999,999,999,999,999,-4,999,999,999,999,999,999,999,999,999,-24,999,999,999,999,999,999,999,39,999,999,-44,999,-47,999,-51,999,999,-54,60,62,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,36,38,999,999,999,999,999,999,999,999,999,999,60,999,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,39,999,999,999,999,-47,999,-51,999,999,-54,60,62,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,45,999,999,999,999,42,999,49,999,999,999,64,999,999,999,999,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,45,999,999,999,999,42,999,49,999,999,999,63,999,999,999,999,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999},
		{999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999}
	};

	//Takes the index given by the parseTable and returns an array of the RSymbols making up the right side of that production
	private  RSymbol[][] RightProductions = new RSymbol[][] 
			{
		{},
		/*1*/	{TokenType.PROGRAM, TokenType.IDENTIFIER, Action.thirteen, TokenType.LEFTPAREN, NonTerminal.identifier_list,
			TokenType.RIGHTPAREN, Action.nine, TokenType.SEMICOLON, NonTerminal.declarations, 
			NonTerminal.sub_declarations, Action.fiftysix, NonTerminal.compound_statement, Action.fiftyfive},
		/*2*/	{TokenType.IDENTIFIER, Action.thirteen, NonTerminal.identifier_list_tail},
		/*3*/   {TokenType.COMMA, TokenType.IDENTIFIER, Action.thirteen, NonTerminal.identifier_list_tail},
		/*4*/   {},
		/*5*/   {TokenType.VAR, Action.one, NonTerminal.declaration_list, Action.two},
		/*6*/   {},
		/*7*/   {NonTerminal.identifier_list, TokenType.COLON, NonTerminal.type, Action.three, TokenType.SEMICOLON,
			NonTerminal.declaration_list_tail},
		/*8*/   {NonTerminal.identifier_list, TokenType.COLON, NonTerminal.type, Action.three, TokenType.SEMICOLON, 
				NonTerminal.declaration_list_tail},
		/*9*/   {},
		/*10*/  {NonTerminal.standard_type},
		/*11*/  {NonTerminal.array_type},
		/*12*/  {TokenType.INTEGER, Action.four},
		/*13*/  {TokenType.REAL, Action.four},
		/*14*/  {Action.six, TokenType.ARRAY, TokenType.LEFTBRACKET, TokenType.INTCONSTANT, Action.seven, TokenType.DOUBLEDOT, 
			TokenType.INTCONSTANT, Action.seven, TokenType.RIGHTBRACKET, TokenType.OF, NonTerminal.standard_type},
		/*15*/  {NonTerminal.subprogram_declaration, NonTerminal.sub_declarations},
		/*16*/  {},
		/*17*/  {Action.one, NonTerminal.subprogram_head, NonTerminal.declarations, Action.five, NonTerminal.compound_statement, Action.eleven},
		/*18*/  {TokenType.FUNCTION, TokenType.IDENTIFIER, Action.fifteen, NonTerminal.arguments, TokenType.COLON, 
			TokenType.RESULT, NonTerminal.standard_type, TokenType.SEMICOLON, Action.sixteen},
		/*19*/  {TokenType.PROCEDURE, TokenType.IDENTIFIER, Action.seventeen, NonTerminal.arguments, TokenType.SEMICOLON},
		/*20*/  {TokenType.LEFTPAREN, Action.nineteen, NonTerminal.parameter_list, TokenType.RIGHTPAREN, Action.twenty},
		/*21*/  {},
		/*22*/  {NonTerminal.identifier_list, TokenType.COLON, NonTerminal.type, Action.twentyone, NonTerminal.parameter_list_tail},
		/*23*/  {TokenType.SEMICOLON, NonTerminal.identifier_list, TokenType.COLON, NonTerminal.type, Action.twentyone,
			NonTerminal.parameter_list_tail},
		/*24*/  {},
		/*25*/  {TokenType.BEGIN, NonTerminal.statement_list, TokenType.END},
		/*26*/  {NonTerminal.statement, NonTerminal.statement_list_tail},
		/*27*/  {TokenType.SEMICOLON, NonTerminal.statement, NonTerminal.statement_list_tail},
		/*28*/  {},
		/*29*/  {NonTerminal.elementary_statement},
		/*30*/  {TokenType.IF, NonTerminal.expression, Action.twentytwo, TokenType.THEN, NonTerminal.statement, 
			NonTerminal.else_clause},
		/*31*/  {TokenType.WHILE, Action.twentyfour, NonTerminal.expression, Action.twentyfive, TokenType.DO, NonTerminal.statement, Action.twentysix},
		/*32*/  {TokenType.ELSE, Action.twentyseven, NonTerminal.statement, Action.twentyeight},
		/*33*/  {Action.twentynine},
		/*34*/  {TokenType.IDENTIFIER, Action.thirty, NonTerminal.es_tail},
		/*35*/  {NonTerminal.compound_statement},
		/*36*/  {Action.fiftythree, NonTerminal.subscript, TokenType.ASSIGNOP, NonTerminal.expression, Action.thirtyone},
		/*37*/  {Action.fiftyfour, NonTerminal.parameters},
		/*38*/  {Action.thirtytwo, TokenType.LEFTBRACKET, NonTerminal.expression, TokenType.RIGHTBRACKET, Action.thirtythree},
		/*39*/  {Action.thirtyfour},
		/*40*/  {Action.thirtyfive, TokenType.LEFTPAREN, NonTerminal.expression_list, TokenType.RIGHTPAREN, Action.fiftyone},
		/*41*/  {Action.thirtysix},
		/*42*/  {NonTerminal.expression, Action.thirtyseven, NonTerminal.expression_list_tail},
		/*43*/  {TokenType.COMMA, NonTerminal.expression, Action.thirtyseven, NonTerminal.expression_list_tail},
		/*44*/  {},
		/*45*/  {NonTerminal.simple_expression, NonTerminal.expression_tail},
		/*46*/  {TokenType.RELOP, Action.thirtyeight, NonTerminal.simple_expression, Action.thirtynine},
		/*47*/  {},
		/*48*/  {NonTerminal.term, NonTerminal.simple_expression_tail},
		/*49*/  {NonTerminal.sign, Action.forty, NonTerminal.term, Action.fortyone, NonTerminal.simple_expression_tail},
		/*50*/  {TokenType.ADDOP, Action.fortytwo, NonTerminal.term, Action.fortythree, NonTerminal.simple_expression_tail},
		/*51*/  {},
		/*52*/  {NonTerminal.factor, NonTerminal.term_tail},
		/*53*/  {TokenType.MULOP, Action.fortyfour, NonTerminal.factor, Action.fortyfive, NonTerminal.term_tail},
		/*54*/  {},
		/*55*/  {TokenType.IDENTIFIER, Action.fortysix, NonTerminal.factor_tail},
		/*56*/  {NonTerminal.constant, Action.fortysix},
		/*57*/  {TokenType.LEFTPAREN, NonTerminal.expression, TokenType.RIGHTPAREN},
		/*58*/  {TokenType.NOT, NonTerminal.factor, Action.fortyseven},
		/*59*/  {NonTerminal.actual_parameters},
		/*60*/  {NonTerminal.subscript, Action.fortyeight},
		/*61*/  {Action.fortynine, TokenType.LEFTPAREN, NonTerminal.expression_list, TokenType.RIGHTPAREN, Action.fifty},
		/*62*/  {Action.fiftytwo},
		/*63*/  {TokenType.UNARYPLUS},
		/*64*/  {TokenType.UNARYMINUS},
		/*65*/  {NonTerminal.program, TokenType.ENDMARKER},
		/*66*/  {TokenType.INTCONSTANT}, 
		/*67*/  {TokenType.REALCONSTANT}
	};
	
	/* The Parser constructor takes in the LexicalAnalyzer and SemanticAction instantiated
	 *   in Compiler, sets the count used in stack printing, and initializes the parse stack
	 *   with and ENDOFFILE Token and a Goal NonTerminal
	 */
	public Parser(LexicalAnalyzer la, SemanticAction sa) 
	{
		
		LA = la;
		SA = sa;
		
		Count = 1;
		
		Parsed = new Stack<>();
		Parsed.push(TokenType.ENDOFFILE);		
		Parsed.push(NonTerminal.Goal);
	}
	
	/* Returns the Token index, -999 to signify an endoffile, or an error */ 
	public int GetTokenInd(Token t) throws CompilerError 
	{
		for (int i = 0; i < TokenIndeces.length;i++) 
		{
			if (t.GetType() == TokenIndeces[i]) 
			{
				return i;
			}
			else if (t.GetType() == TokenType.ENDOFFILE) 
			{
				return -999;
			}
		}
		throw new SyntacticError("Token Error: Token not found ( " + t.TokenToString() + " )");
	}

	/* Returns the top of stack index, -999 to signify a Token match, or an error */
	public int GetPeekedInd(RSymbol r) throws CompilerError 
	{
		if (r.IsToken()) 
		{
			if (r == LastSeen.GetType()) 
			{
				return -999;
			}
			else 
			{
				throw new SyntacticError("Token " + "(" +
						LastSeen.GetType() + ") " + "doesn't match stack top " + "(" + 
						r + ")");
			}
		} 
		else if (r.IsAction()) 
		{
			return -888;
		}
		for (int i = 0; i < NonIndeces.length;i++)
		{
			if (r == NonIndeces[i]) 
			{
				return i;
			}
		}
		throw new SyntacticError("Stack Error: Stack Symbol not found ( " + r + " )");
	}

	/* Prints the stack (subject to your formatting approval) */
	public void PrintStack() 
	{
		System.out.println(">>-  " + Count + "  -<<");
		String stack = Parsed.toString();
		System.out.println("Stack ::==> " + stack);
		Count += 1;
	}

	/* Returns a string representation of the production rules for nice printing */
	public String StringProductions(RSymbol[] r) 
	{
		String toPrint = "[" + r[0];

		for (int i = 1; i < r.length; i++) 
		{
			toPrint += ", ";
			toPrint += r[i];
		}
		toPrint += "]";
		return toPrint;
	}
	
	/* used in the Parse method to set the last Token seen and get a new Token from the LexicalAnalyzer */
    private void GetToken() throws IOException, CompilerError 
    {
    	TwoTokensAgo = LastSeen;
		LastSeen = LA.GetNextToken(TwoTokensAgo);
    }
    
    /* The big shebang! */ 
    public void Parse() throws IOException, CompilerError 
    {

		if (PRINT) 
		{
			PrintStack();
		}
		else if (SEMPRINT) 
		{
			SA.SemanticDump();
		}

		GetToken();

		//One large loop until endoffile is reached
		while (!(LastSeen.GetType() == TokenType.ENDOFFILE)) 
		{
			RSymbol peeked = Parsed.peek();

			//if the stack top is not an action then deal with terminals and nonterminals
			if (!(peeked.IsAction())) 
			{

				int peekedInd = GetPeekedInd(peeked);
				int tempInd = GetTokenInd(LastSeen);
				//If the index of the top of stack symbol is -999, it means a match was found with the lookahead token so
				//	consume both
				while ((peekedInd == -999) && (!(LastSeen.GetType() == TokenType.ENDOFFILE))) 
				{
					if (PRINT) 
					{
						System.out.println("Popped " + peeked+ " with token " + LastSeen.GetType() + 
								" -> * MATCH *  {consume tokens}\n");
					}
					Parsed.pop();
					peeked = Parsed.peek();

					GetToken();
					
					tempInd = GetTokenInd(LastSeen);
					peekedInd = GetPeekedInd(peeked);
					if (PRINT) 
					{
						PrintStack();
					}
				}

				//After the loop ends, check that the new Token isn't endoffile
				if (LastSeen.GetType() == TokenType.ENDOFFILE) 
				{
					if (PRINT) 
					{
						System.out.println("Popped " + peeked + " with token " + LastSeen.GetType() + 
								" -> * MATCH *  {consume tokens}\n");
					}
					Parsed.pop();
				
				//If the index of the top of stack symbol is -888, it means it's an action so execute
				}
				else if (peekedInd == -888) 
				{
					Action peekeoad = (Action) peeked;
					SA.Execute(peekeoad, TwoTokensAgo);

					if (SEMPRINT) 
					{
						System.out.println("Executed Action [ " + peeked + " ] with token " + TwoTokensAgo.TokenToString() + "\n");
						SA.SemanticDump();
					}
					else if (PRINT) 
					{
						System.out.println("Popped " + peeked + " with token " + LastSeen.GetType() + 
								" -> # SEMANTIC ACTION #   [ " + peeked + " ] \n");
					}
					Parsed.pop();
					
					if (PRINT) 
					{
						PrintStack();
					}

				//Else go to the parse table
				}
				else 
				{
					//Indol is the index of the production rule (if no Token match or endoffile)
					int indol = ParseTable[tempInd][peekedInd];	
					//If Indol is 999, then there was no match with the lookahead and top of stack so throw an error
					if (indol == 999) 
					{
						throw new SyntacticError("Token " + "(" +
								LastSeen.GetType() + ") " + "doesn't match stack top " + "(" + 
								peeked + ")");
						//If indol is < 0, then it's an epsilon move, pop accordingly
					}
					else if (indol < 0) 
					{
						if (PRINT) 
						{
							System.out.println("Popped " + peeked + " with token " + LastSeen.GetType() + " -> @ EPSILON @   [ " +
									(indol * -1) + " ] " + peeked + " ::= @ EPSILON @\n");
						}
						Parsed.pop();
						
						//Else, indol refers to a production rule in the rightProductions matrix, so pop the top of stack and
						//	push the elements of the rightProductions array one at a time
					} 
					else 
					{
						if (PRINT)
						{
							String toPrint = StringProductions(RightProductions[indol]);
							System.out.println("Popped " + peeked + " with token " + LastSeen.GetType() + " -> $ PUSH $   [ " + 
									indol + " ] " + peeked + "::= " + toPrint + "\n");
						}
						Parsed.pop();
						for (int i = RightProductions[indol].length - 1; i > -1;i--) 
						{
							Parsed.push(RightProductions[indol][i]);
						}		
					}
					if (PRINT) 
					{
						PrintStack();	
					}
				}
			//If the stack top is an action (second check) then execute
			}
			else 
			{
				Action peekoeead = (Action) peeked;
				SA.Execute(peekoeead, TwoTokensAgo);

				if (SEMPRINT) 
				{
					System.out.println("Executed Action [ " + peeked + " ] with token " + TwoTokensAgo.TokenToString() + "\n");
					SA.SemanticDump();
				}
				else if (PRINT) 
				{
					System.out.println("Popped " + peeked + " with token " + LastSeen.GetType() + 
							" -> # SEMANTIC ACTION #   [ " + peeked + " ] \n");
				}
				Parsed.pop();

				if (PRINT) 
				{
					PrintStack();
				}
			}
		} 
		
		//If the loop successfully completed with an endoffile Token, accept!
		System.out.println("[ ACCEPT ]");
    }
    
    /* Parses through the file, printing each stack parse stack change */
    public void TestParser() throws IOException, CompilerError 
    {
    	PRINT = true;
    	Parse();
    }
}
