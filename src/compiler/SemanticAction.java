package compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SemanticAction 
{
	private boolean Insert;
	private boolean Global;
	private boolean Array;
	private int GlobalMemory;
	private int LocalMemory;

	private int TempVars;

	private int GlobalStore;
	private int LocalStore;

	private Stack<Object> SemStack;

	private static int SemCount;

	private SymbolTable GlobalTable;
	private SymbolTable LocalTable;
	private SymbolTable ConstantTable;

	private Quadruples Quadyos;
	
	private SymbolTableEntry CurrentFunction;
	
	private Stack<List<SymbolTableEntry>> ParamStack;
	private Stack<Integer> ParamCount;
	
	private int NextParam;

	public SemanticAction() throws CompilerError {

		Insert = true;
		Global = true;
		Array = false;
		GlobalMemory = 0;
		LocalMemory = 0;

		TempVars = -1;

		GlobalStore = 0;
		LocalStore = 0;

		SemStack = new Stack<>();

		SemCount = 1;

		GlobalTable = new SymbolTable();
		LocalTable = new SymbolTable();
		ConstantTable = new SymbolTable();

		Quadyos = new Quadruples();

		ProcedureEntry ready = new ProcedureEntry("READ");
		ready.SetReserved();
		ProcedureEntry writey = new ProcedureEntry("WRITE");
		writey.SetReserved();
		ProcedureEntry mainy = new ProcedureEntry("MAIN");
		mainy.SetReserved();

		GlobalTable.insert("READ", ready);
		GlobalTable.insert("WRITE", writey);
		GlobalTable.insert("MAIN", mainy);
		
		ParamStack = new Stack<>();
		ParamCount = new Stack<>();
	}

	/* Takes as input an Action number and the lastseen token, executing that 
	 *   Action, passing in the token if necessary
	 */
	public void Execute(Action number, Token toky) throws CompilerError {

		switch (number) {
		case one:
			ActionOne();
			break;
		case two:
			ActionTwo();
			break;
		case three:
			ActionThree();
			break;
		case four:
			ActionFour(toky);
			break;
		case five:
			ActionFive();
			break;
		case six:
			ActionSix();
			break;
		case seven:
			ActionSeven(toky);
			break;
		case nine:
			ActionNine();
			break;
		case eleven:
			ActionEleven();
			break;
		case thirteen:
			ActionThirteen(toky);
			break;
		case fifteen:
			ActionFifteen(toky);
			break;
		case sixteen:
			ActionSixteen();
			break;
		case seventeen:
			ActionSeventeen(toky);
			break;
		case nineteen:
			ActionNineteen();
			break;
		case twenty:
			ActionTwenty();
			break;
		case twentyone:
			ActionTwentyOne();
			break;
		case twentytwo:
			ActionTwentyTwo();
			break;
		case twentyfour:
			ActionTwentyFour();
			break;
		case twentyfive:
			ActionTwentyFive();
			break;
		case twentysix:
			ActionTwentySix();
			break;
		case twentyseven:
			ActionTwentySeven();
			break;
		case twentyeight:
			ActionTwentyEight();
			break;
		case twentynine:
			ActionTwentyNine();
			break;
		case thirty:
			ActionThirty(toky);
			break;
		case thirtyone:
			ActionThirtyOne();
			break;
		case thirtytwo:
			ActionThirtyTwo();
			break;
		case thirtythree:
			ActionThirtyThree();
			break;
		case thirtyfour:
			ActionThirtyFour(toky);
			break;
		case thirtyfive:
			ActionThirtyFive();
			break;
		case thirtysix:
			ActionThirtySix();
			break;
		case thirtyseven:
			ActionThirtySeven();
			break;
		case thirtyeight:
			ActionThirtyEight(toky);
			break;
		case thirtynine:
			ActionThirtyNine();
			break;
		case forty:
			ActionForty(toky);
			break;
		case fortyone:
			ActionFortyOne();
			break;
		case fortytwo:
			ActionFortyTwo(toky);
			break;
		case fortythree:
			ActionFortyThree();
			break;
		case fortyfour:
			ActionFortyFour(toky);
			break;
		case fortyfive:
			ActionFortyFive();
			break;
		case fortysix:
			ActionFortySix(toky);
			break;
		case fortyseven:
			ActionFortySeven();
			break;
		case fortyeight:
			ActionFortyEight(toky);
			break;
		case fortynine:
			ActionFortyNine();
			break;
		case fifty:
			ActionFifty();
			break;
		case fiftyone:
			ActionFiftyOne(toky);
			break;
		case fiftyoneread:
			ActionFiftyOneRead();
			break;
		case fiftyonewrite:
			ActionFiftyOneWrite();
			break;
		case fiftytwo:
			ActionFiftyTwo();
			break;
		case fiftythree:
			ActionFiftyThree();
			break;
		case fiftyfour:
			ActionFiftyFour();
			break;
		case fiftyfive:
			ActionFiftyFive();
			break;
		case fiftysix:
			ActionFiftySix();
			break;
		}
	}

	/* Sets the Insert flag as true */
	private void ActionOne() 
	{
		Insert = true;
	}

	/* Sets the Insert flag as false */
	private void ActionTwo() 
	{
		Insert = false;
	}

	/* Creates an ArrayEntry or a VariableEntry */ 
	private void ActionThree() throws CompilerError 
	{
		TokenType type = ((Token) SemStack.pop()).GetType();
		if (Array) 
		{
			int upperbound = ((Token) SemStack.pop()).TokenToInt();
			int lowerbound = ((Token) SemStack.pop()).TokenToInt();
			int memorysize = (upperbound - lowerbound) + 1;

			while ((!(SemStack.isEmpty())) && (((Token) SemStack.peek()).GetType() == TokenType.IDENTIFIER)) 
			{
				Token tok = (Token) SemStack.pop();
				ArrayEntry a = new ArrayEntry(tok.GetValue());
				a.SetType(type);
				a.SetUpperBound(upperbound);
				a.SetLowerBound(lowerbound);

				if (Global) 
				{
					a.SetAddress(GlobalMemory);
					GlobalTable.insert(tok.GetValue(), a);
					GlobalMemory += memorysize;
				}
				else 
				{
					a.SetAddress(LocalMemory);
					LocalTable.insert(tok.GetValue(), a);
					LocalMemory += memorysize;
				}
			}
		}
		else 
		{
			while ((!(SemStack.isEmpty())) && (SemStack.peek() instanceof Token) && 
					(((Token) SemStack.peek()).GetType() == TokenType.IDENTIFIER)) 
			{
				Token tok = (Token) SemStack.pop();
				VariableEntry v = new VariableEntry(tok.GetValue());
				v.SetType(type);

				if (Global) 
				{
					v.SetAddress(GlobalMemory);
					GlobalTable.insert(tok.GetValue(), v);
					GlobalMemory++;
				}
				else 
				{
					v.SetAddress(LocalMemory);
					LocalTable.insert(tok.GetValue(), v);
					LocalMemory++;
				}
			}
		}
		Array = false;
	}

	/* Pushes a Token on the stack whose type is used in action three */
	private void ActionFour(Token toky) 
	{
		SemStack.push(toky);
	}
	
	/* Generates the quads to begin a new process */
	private void ActionFive() throws CompilerError 
	{
		Insert = false;
		SymbolTableEntry id = (SymbolTableEntry)SemStack.pop();
		Gen("PROCBEGIN", "@"+id.GetName());
		LocalStore = Quadyos.GetNextQuad();
		Gen("alloc", "_");
	}

	/* Sets the Array flag as true */
	private void ActionSix() 
	{
		Array = true;
	}

	/* Pushes an integer identifier onto the stack, to be used as the upper and lower
	 *   bounds of the ArrayEntry in action three
	 */
	private void ActionSeven(Token toky) 
	{
		SemStack.push(toky);
	}

	/* Insert the first three ids into the GlobalTable (input, output, the function name) 
	 *   also initialize the call to main and exit quads */
	private void ActionNine() throws CompilerError 
	{
		Token id1 = (Token) SemStack.pop();
		Token id2 = (Token) SemStack.pop();
		Token id3 = (Token) SemStack.pop();

		IODeviceEntry id1y = new IODeviceEntry(id1.GetValue());
		id1y.SetReserved();
		IODeviceEntry id2y = new IODeviceEntry(id2.GetValue());
		id2y.SetReserved();
		ProcedureEntry id3y = new ProcedureEntry(id3.GetValue(), 0, null);
		id3y.SetReserved();

		GlobalTable.insert(id1.GetValue(), id1y);
		GlobalTable.insert(id2.GetValue(), id2y);
		GlobalTable.insert(id3.GetValue(), id3y);

		Gen("call", "@main", "@0");
		Gen("exit");
	}
	
	/* Cleans up the CurrentFunction, ending the going process and freeing its memory */
	private void ActionEleven() throws CompilerError 
	{
		Global = true;
		LocalTable = new SymbolTable();
		CurrentFunction = null;
		Backpatch(LocalStore, LocalMemory);
		Gen("free", "@" + Integer.toString(LocalMemory));
		Gen("PROCEND");
	}

	/* Push an identifier token onto the semantic stack */
	private void ActionThirteen(Token toky) 
	{
		SemStack.push(toky);
	}
	
	/* Insert a FunctionEntry into the GlobalTable and create a VariableEntry of its results */
	private void ActionFifteen(Token toky) throws CompilerError 
	{
		VariableEntry result = Create(toky.GetValue() + "_RESULT", TokenType.INTEGER);
		
		SymbolTableEntry id = new FunctionEntry(toky.GetValue(), result);
		GlobalTable.insert(id.GetName(), id);
		Global = false;
		LocalMemory = 0;
		CurrentFunction = id;
		SemStack.push(id);
	}
	
	/* Sets the type of a function and its result */
	private void ActionSixteen() 
	{
		Token type = (Token)SemStack.pop();
		FunctionEntry id = (FunctionEntry)SemStack.peek();
		id.SetType(type.GetType());
		id.SetResultType(type.GetType());
		CurrentFunction = id;
	}
	
	/* Creates a new ProcedureEntry with the name of the token from the parser */
	private void ActionSeventeen(Token toky) throws CompilerError 
	{
		SymbolTableEntry id = new ProcedureEntry(toky.GetValue());
		GlobalTable.insert(id.GetName(), id);
		Global = false;
		LocalMemory = 0;
		CurrentFunction = id;
		SemStack.push(id);
	}
	
	/* Initializes a ParamCount stack and pushes 0 on */
	private void ActionNineteen() 
	{
		ParamCount = new Stack<>();
		ParamCount.push(0);
	}
	
	/* Set the number of parameters for some Function or Procedure entry */
	private void ActionTwenty() 
	{
		SymbolTableEntry id = (SymbolTableEntry)SemStack.peek();
		int numParams = ParamCount.pop();
		id.SetNumberOfParameters(numParams);
	}
	
	/* Pop a function's parameters off of the semantic stack and create an ArrayEntry or a 
	 *   VariableEntry for each
	 */
	private void ActionTwentyOne() throws CompilerError 
	{
		Token type = (Token)SemStack.pop();
		
		int upperBound = -1;
		int lowerBound = -1;
		
		if (Array) 
		{
			upperBound = ((Token)SemStack.pop()).TokenToInt();
			lowerBound = ((Token)SemStack.pop()).TokenToInt();
		}
		Stack<Token> parameters = new Stack<>();
		
		while ((SemStack.peek() instanceof Token) && (((Token)SemStack.peek()).GetType() == TokenType.IDENTIFIER)) 
		{
			parameters.push((Token)SemStack.pop());
		}
		
		while (!parameters.empty()) 
		{
			Token param = parameters.pop();
			
			SymbolTableEntry var;
			if (Array) 
			{
				var = new ArrayEntry(param.GetValue(), LocalMemory, type.GetType(), upperBound, lowerBound);
				
			} 
			else 
			{
				var = new VariableEntry(param.GetValue(), LocalMemory, type.GetType());
			}
			var.SetParameter();
			LocalTable.insert(var.GetName(), var);
			
			CurrentFunction.AddParameter(var);
			LocalMemory++;
			ParamCount.push(ParamCount.pop() + 1);
		}
		Array = false;
	}

	/* Backpatches the true case of a relational operator */
	private void ActionTwentyTwo() throws CompilerError 
	{
		EType etype = (EType) SemStack.pop();
		if (etype != EType.RELATIONAL) 
		{
			throw new SemanticError("Illegal operation");
		}
		List<Integer> EFalse = (List<Integer>) SemStack.pop();
		List<Integer> ETrue = (List<Integer>) SemStack.pop();

		Backpatch(ETrue, Quadyos.GetNextQuad());
		SemStack.push(ETrue);
		SemStack.push(EFalse);
	}

	/* Pushes the quad number corresponding to the beginning of a loop onto the semantic stack */
	private void ActionTwentyFour() 
	{
		int beginLoop = Quadyos.GetNextQuad();
		SemStack.push(beginLoop);
	}

	/* Backpatches the true jump for < */ 
	private void ActionTwentyFive() throws CompilerError 
	{
		EType etype = (EType) SemStack.pop();
		if (etype != EType.RELATIONAL) 
		{
			throw new SemanticError("EType mismatch");
		}
		List<Integer> EFalse = (List<Integer>) SemStack.pop();
		List<Integer> ETrue = (List<Integer>) SemStack.pop();

		Backpatch(ETrue, (Quadyos.GetNextQuad()));

		SemStack.push(ETrue);
		SemStack.push(EFalse);
	}

	/* Generates the goto for returning to the beginning of a loop and backpatches the false case 
	 *   of some relational operator
	 */
	private void ActionTwentySix() throws CompilerError 
	{
		List<Integer> EFalse = (List<Integer>) SemStack.pop();
		List<Integer> ETrue = (List<Integer>) SemStack.pop();

		int beginLoop = (int) SemStack.pop();

		Gen("goto", "@" + Integer.toString(beginLoop));
		Backpatch(EFalse, Quadyos.GetNextQuad());

	}

	/* Generates a blank goto and backpatches the false case of some relational operator */
	private void ActionTwentySeven() throws CompilerError 
	{
		List<Integer> skipElse = MakeList(Quadyos.GetNextQuad());
		Gen("goto", "@" + "_");
		List<Integer> EFalse = (List<Integer>) SemStack.pop();
		List<Integer> ETrue = (List<Integer>) SemStack.pop();
		Backpatch(EFalse, Quadyos.GetNextQuad());
		SemStack.push(skipElse);
		SemStack.push(ETrue);
		SemStack.push(EFalse);
	}

	/* Backpatches the skipelse case of some relational operator */
	private void ActionTwentyEight() 
	{
		List<Integer> EFalse = (List<Integer>) SemStack.pop();
		List<Integer> ETrue = (List<Integer>) SemStack.pop();
		List<Integer> skipElse = (List<Integer>) SemStack.pop();
		Backpatch(skipElse, Quadyos.GetNextQuad());
	}

	/* Backpatches the false case of some relational operator */
	private void ActionTwentyNine() 
	{
		List<Integer> EFalse = (List<Integer>) SemStack.pop();
		List<Integer> ETrue = (List<Integer>) SemStack.pop();
		Backpatch(EFalse, Quadyos.GetNextQuad());
	}

	/* Push a variable's SymbolTableEntry and the arithmetic EType on the semantic stack */
	private void ActionThirty(Token toky) throws CompilerError 
	{
		SymbolTableEntry id = LookupID(toky.GetValue());
		if (id == null) 
		{
			throw new SemanticError("Variable " + toky.GetValue() + " is undefined");
		}
		SemStack.push(id);
		SemStack.push(EType.ARITHMETIC);
	}

	/* Generate the move, ltof, and stor quads used in assigning a variable */
	private void ActionThirtyOne() throws CompilerError 
	{

		EType etype = (EType) SemStack.pop();
		if (etype != EType.ARITHMETIC) 
		{
			throw new SemanticError("Illegal operation");
		}

		SymbolTableEntry id2 = (SymbolTableEntry) SemStack.pop();
		SymbolTableEntry offset = (SymbolTableEntry) SemStack.pop();
		SymbolTableEntry id1 = (SymbolTableEntry) SemStack.pop();
		
		if (TypeCheck(id1, id2) == 3) 
		{
			throw new SemanticError("Type Mismatch: Cannot assign real value to integer variable");
		}
		else if (TypeCheck(id1, id2) == 2) 
		{
			VariableEntry temp = Create(GetTempVar(), TokenType.REAL);
			Gen("ltof", id2.GetName(), temp.GetName());
			
			if (offset == null) 
			{
				Gen("move", temp.GetName(), id1.GetName());
			}
			else 
			{
				Gen("stor", temp.GetName(), offset.GetName(), id1.GetName());
			}
		}
		else 
		{
			if (offset == null) 
			{
				Gen("move", id2.GetName(), id1.GetName());
			}
			else 
			{
				Gen("stor", id2.GetName(), offset.GetName(), id1.GetName());
			}
		}
	}

	/* Checks that the top of the semantic stack is an ArrayEntry */
	private void ActionThirtyTwo() throws CompilerError 
	{
		EType etype = (EType) SemStack.pop();
		SymbolTableEntry id = (SymbolTableEntry) SemStack.peek();
		if (etype != EType.ARITHMETIC) 
		{
			throw new SemanticError("EType mismatch");
		}
		if (!(id.IsArray())) 
		{
			throw new SemanticError("ID is not an ArrayEntry  " + id.StringEntry());
		}
	}

	/* Generates the move and sub quads associated with an integer entry following an ArrayEntry */
	private void ActionThirtyThree() throws CompilerError 
	{
		EType etype = (EType) SemStack.pop();
		
		if (etype != EType.ARITHMETIC) 
		{
			throw new SemanticError("EType mismatch");		
		}
		SymbolTableEntry id = (SymbolTableEntry) SemStack.pop();
		if (id.GetType() != TokenType.INTEGER) 
		{
			throw new SemanticError("EType mismatch");
		}
		
		ArrayEntry array = (ArrayEntry) SemStack.peek();
		VariableEntry temp1 = Create(GetTempVar(), TokenType.INTEGER);
		VariableEntry temp2 = Create(GetTempVar(), TokenType.INTEGER);
		Gen("move", "@" + Integer.toString(array.GetLowerBound()), temp1.GetName());
		Gen("sub", id.GetName(), temp1.GetName(), temp2.GetName());
		SemStack.push(temp2);
	}

	/* Executes action fifty two in the case of a function */
	private void ActionThirtyFour(Token toky) throws CompilerError 
	{
		EType etype = (EType) SemStack.pop();
		SymbolTableEntry id = (SymbolTableEntry) SemStack.peek();
		
		if (id.IsFunction()) 
		{
			SemStack.push(etype);
			Execute(Action.fiftytwo, toky);
		} 
		else 
		{
			SemStack.push(null);
		}
	}
	
	/* Pushes the ParameterInfo of a ProcedureEntry */
	private void ActionThirtyFive() 
	{
		EType etype = (EType)SemStack.pop();
		ProcedureEntry id = (ProcedureEntry)SemStack.peek();
		
		SemStack.push(etype);
		ParamCount.push(0);
		ParamStack.push(id.GetParameterInfo());
	}

	/* Generates the call quad for a ProcedureEntry */
	private void ActionThirtySix() throws CompilerError 
	{
		EType etype = (EType)SemStack.pop();
		ProcedureEntry id = (ProcedureEntry)SemStack.pop();
		
		if (id.GetNumberOfParameters() != 0) 
		{
			throw new SemanticError("Wrong number of parameters");
		}
		
		Gen("call", "@"+id.GetName(), "@"+Integer.toString(0));
	}
	
	/* Pop the name of a procedure of function on the bottom of the semantic stack and check its
	 *   number and type of parameters 
	 */
	private void ActionThirtySeven() throws SemanticError 
	{
		EType etype = (EType)SemStack.pop();
		
		if (etype != EType.ARITHMETIC) 
		{
			throw new SemanticError("EType Mismatch");
		}
		
		SymbolTableEntry id = (SymbolTableEntry)SemStack.peek();
		
		if (!((id.IsVariable()) || (id.IsConstant()) || (id.IsArray()))) 
		{
			throw new SemanticError("Wrong param type");
		}
		
		ParamCount.push(ParamCount.pop() + 1);
		
		Stack<Object> parameters = new Stack<>();
		while ((SemStack.peek() instanceof SymbolTableEntry) || (SemStack.peek() instanceof EType))
		{
			if (SemStack.peek() instanceof SymbolTableEntry) 
			{
				if (!((((SymbolTableEntry)SemStack.peek()).IsProcedure()) || (((SymbolTableEntry)SemStack.peek()).IsFunction()))) 
				{ 
					parameters.push(SemStack.pop());
				}
				else 
				{
					break;
				}
			} 
			else 
			{
				parameters.push(SemStack.pop());
			}
		}
		
		SymbolTableEntry funcId = (SymbolTableEntry)SemStack.peek();
		
		while (!parameters.empty()) 
		{
			SemStack.push(parameters.pop());
		}
		
		if (!((funcId.GetName().equals("READ")) || (funcId.GetName().equals("WRITE")))) 
		{
			if (ParamCount.peek() > funcId.GetNumberOfParameters()) 
			{
				throw new SemanticError("Wrong number of params");
			}
			
			SymbolTableEntry param = ParamStack.peek().get(NextParam);
			if (id.GetType() != param.GetType()) 
			{
				throw new SemanticError("Wrong param type");
			}
			if (param.IsArray()) 
			{
				if ((id.GetLowerBound() != param.GetLowerBound()) ||
						(id.GetUpperBound() != param.GetUpperBound())) 
				{
					throw new SemanticError("Bad param");
				}
			}
			NextParam++;
		}
	}
	
	/* Pushes an operator token onto the semantic stack */
	private void ActionThirtyEight(Token toky) throws CompilerError 
	{
		EType etype = (EType) SemStack.pop();
		if (etype != EType.ARITHMETIC) 
		{
			throw new SemanticError("EType mismatch");		
		}
		SemStack.push(toky);
	}

	/* Generates the quads for a relational operator */
	private void ActionThirtyNine() throws CompilerError 
	{
		EType etype = (EType) SemStack.pop();
		
		if (etype != EType.ARITHMETIC) 
		{
			throw new SemanticError("EType mismatch");		
		}
		
		SymbolTableEntry id2 = (SymbolTableEntry) SemStack.pop();
		Token operator = (Token) SemStack.pop();
		String opcode = operator.GetOpCode();
		SymbolTableEntry id1 = (SymbolTableEntry) SemStack.pop();
		
		if (TypeCheck(id1, id2) == 2) 
		{
			VariableEntry temp = Create(GetTempVar(), TokenType.REAL);
			Gen("ltof", id2.GetName(), temp.GetName());
			Gen(opcode, id1.GetName(), temp.GetName(), "@" + "_");
		} 
		else if (TypeCheck(id1, id2) == 3) 
		{
			VariableEntry temp = Create(GetTempVar(), TokenType.REAL);
			Gen("ltof", id1.GetName(), temp.GetName());
			Gen(opcode, temp.GetName(), id2.GetName(), "@" + "_");
		} 
		else 
		{
			Gen(opcode, id1.GetName(), id2.GetName(), "@" + "_");
		}
		Gen("goto", "@" + "_");

		List<Integer> ETrue = MakeList(Quadyos.GetNextQuad() - 2);
		List<Integer> EFalse = MakeList(Quadyos.GetNextQuad() - 1);

		SemStack.push(ETrue);
		SemStack.push(EFalse);
		SemStack.push(EType.RELATIONAL);
	}

	/* Push a unaryplus or unaryminus on the semantic stack */
	private void ActionForty(Token toky) 
	{
		SemStack.push(toky);
	}

	/* Generate uminus and fuminus quads */
	private void ActionFortyOne() throws CompilerError 
	{
		EType etype = (EType) SemStack.pop();
		
		if (etype != EType.ARITHMETIC) 
		{
			throw new SemanticError("EType mismatch");
		}

		SymbolTableEntry id = (SymbolTableEntry) SemStack.pop();
		Token sign = (Token) SemStack.pop();

		if (sign.GetType() == TokenType.UNARYMINUS) 
		{
			VariableEntry temp = Create(GetTempVar(), id.GetType());
			if (id.GetType() == TokenType.INTEGER) 
			{
				Gen("uminus", id.GetName(), temp.GetName());
			} 
			else 
			{
				Gen("fuminus", id.GetName(), temp.GetName());
			}
			SemStack.push(temp);
		} 
		else 
		{
			SemStack.push(id);
		}
		SemStack.push(EType.ARITHMETIC);
	}

	/* Backpatches the 'or' case */ 
	private void ActionFortyTwo(Token toky) throws CompilerError 
	{
		EType etype = (EType) SemStack.pop();

		if ((toky.GetType() == TokenType.ADDOP) && (toky.GetValue().equals("3"))) 
		{
			if (etype != EType.RELATIONAL) 
			{
				throw new SemanticError("EType mismatch");
			}

			List<Integer> EFalse = (List<Integer>) SemStack.peek();
			Backpatch(EFalse, Quadyos.GetNextQuad());

		} else {
			if (etype != EType.ARITHMETIC) 
			{
				throw new SemanticError("EType mismatch");
			}
		}
		SemStack.push(toky);
	}

	/* Generates relational and arithmetic operations */
	private void ActionFortyThree() throws CompilerError 
	{
		EType etype = (EType) SemStack.pop();
		if (etype == EType.RELATIONAL) 
		{
			List<Integer> E2False = (List<Integer>) SemStack.pop();
			List<Integer> E2True = (List<Integer>) SemStack.pop();
			Token operator = (Token) SemStack.pop();
			List<Integer> E1False = (List<Integer>) SemStack.pop();
			List<Integer> E1True = (List<Integer>) SemStack.pop();

			List<Integer> ETrue = Merge(E1True, E2True);
			List<Integer> EFalse = E2False;

			SemStack.push(ETrue);
			SemStack.push(EFalse);
			SemStack.push(EType.RELATIONAL);
		} 
		else 
		{
			SymbolTableEntry id2 = (SymbolTableEntry) SemStack.pop();
			Token operator = (Token) SemStack.pop();

			String opcode = operator.GetOpCode();
			SymbolTableEntry id1 = (SymbolTableEntry) SemStack.pop();

			if (TypeCheck(id1, id2) == 0) 
			{
				VariableEntry temp = Create(GetTempVar(), TokenType.INTEGER);
				Gen(opcode, id1.GetName(), id2.GetName(), temp.GetName());
				SemStack.push(temp);
			}
			else if (TypeCheck(id1, id2) == 1) 
			{
				VariableEntry temp = Create(GetTempVar(), TokenType.REAL);
				Gen("f" + opcode, id1.GetName(), id2.GetName(), temp.GetName());
				SemStack.push(temp);
			} 
			else if (TypeCheck(id1, id2) == 2) 
			{
				VariableEntry temp1 = Create(GetTempVar(), TokenType.REAL);
				VariableEntry temp2 = Create(GetTempVar(), TokenType.REAL);
				Gen("ltof", id2.GetName(), temp1.GetName());
				Gen("f" + opcode, id1.GetName(), temp1.GetName(), temp2.GetName());
				SemStack.push(temp2);
			} 
			else if (TypeCheck(id1, id2) == 3) 
			{
				VariableEntry temp1 = Create(GetTempVar(), TokenType.REAL);
				VariableEntry temp2 = Create(GetTempVar(), TokenType.REAL);
				Gen("ltof", id1.GetName(), temp1.GetName());
				Gen("f" + opcode, temp1.GetName(), id2.GetName(), temp2.GetName());
				SemStack.push(temp2);
			}
			SemStack.push(EType.ARITHMETIC);
		}

	}

	/* Sets the jump on true for > */
	private void ActionFortyFour(Token toky) 
	{
		EType etype = (EType) SemStack.pop();
		
		if (etype == EType.RELATIONAL) 
		{
			List<Integer> EFalse = (List<Integer>) SemStack.pop();
			List<Integer> ETrue = (List<Integer>) SemStack.pop();

			if ((toky.GetType() == TokenType.MULOP) && (toky.GetValue().equals("5"))) 
			{
				Backpatch(ETrue, Quadyos.GetNextQuad());

			}
			SemStack.push(ETrue);
			SemStack.push(EFalse);
		}

		SemStack.push(toky);
	}

	/* Generates quads required for mod and div operators */
	private void ActionFortyFive() throws CompilerError 
	{
		EType etype = (EType) SemStack.pop();
		if (etype == EType.RELATIONAL) {
			List<Integer> E2False = (List<Integer>) SemStack.pop();
			List<Integer> E2True = (List<Integer>) SemStack.pop();
			Token operator = (Token) SemStack.pop();

			if ((operator.GetType() == TokenType.MULOP) && (operator.GetValue().equals("5"))) 
			{
				List<Integer> E1False = (List<Integer>) SemStack.pop();
				List<Integer> E1True = (List<Integer>) SemStack.pop();

				List<Integer> ETrue = E2True;
				List<Integer> EFalse = Merge(E1False, E2False);

				SemStack.push(ETrue);
				SemStack.push(EFalse);
				SemStack.push(EType.RELATIONAL);
			}
		} 
		else 
		{
			SymbolTableEntry id2 = (SymbolTableEntry) SemStack.pop();
			Token operator = (Token) SemStack.pop();
			String opcode = operator.GetOpCode();
			SymbolTableEntry id1 = (SymbolTableEntry) SemStack.pop();

			// MOD and DIV require integer operands
			if ((TypeCheck(id1, id2) != 0) && (opcode.equals("mod"))) 
			{
				throw new SemanticError("Operands of the MOD operator must both be of type integer");
			} else if ((TypeCheck(id1, id2) != 0) && opcode.equals("DIV")) 
			{
				throw new SemanticError("Operands of the DIV operator must both be of type integer");
			}

			if (TypeCheck(id1, id2) == 0) 
			{
				if (opcode.equals("mod")) 
				{
					VariableEntry temp1 = Create(GetTempVar(), TokenType.INTEGER);
					VariableEntry temp2 = Create(GetTempVar(), TokenType.INTEGER);
					VariableEntry temp3 = Create(GetTempVar(), TokenType.INTEGER);
					Gen("div", id1.GetName(), id2.GetName(), temp1.GetName());
					Gen("mul", id2.GetName(), temp1.GetName(), temp2.GetName());
					Gen("sub", id1.GetName(), temp2.GetName(), temp3.GetName());
					SemStack.push(temp3);
				} 
				else if (opcode.equals("div")) 
				{
					VariableEntry temp1 = Create(GetTempVar(), TokenType.REAL);
					VariableEntry temp2 = Create(GetTempVar(), TokenType.REAL);
					VariableEntry temp3 = Create(GetTempVar(), TokenType.REAL);
					Gen("ltof", id1.GetName(), temp1.GetName());
					Gen("ltof", id2.GetName(), temp2.GetName());
					Gen("fdiv", temp1.GetName(), temp2.GetName(), temp3.GetName());
					SemStack.push(temp3);
				} 
				else
				{
					VariableEntry temp = Create(GetTempVar(), TokenType.INTEGER);
					Gen(opcode.toLowerCase(), id1.GetName(), id2.GetName(), temp.GetName());
					SemStack.push(temp);
				}
			} 
			else if (TypeCheck(id1, id2) == 1) 
			{
				VariableEntry temp = Create(GetTempVar(), TokenType.REAL);
				Gen("f" + opcode, id1.GetName(), id2.GetName(), temp.GetName());
				SemStack.push(temp);
			}
			else if (TypeCheck(id1, id2) == 2) 
			{
				VariableEntry temp1 = Create(GetTempVar(), TokenType.REAL);
				VariableEntry temp2 = Create(GetTempVar(), TokenType.REAL);
				Gen("ltof", id2.GetName(), temp1.GetName());
				Gen("f" + opcode, id1.GetName(), temp1.GetName(), temp2.GetName());
				SemStack.push(temp2);
			} 
			else if (TypeCheck(id1, id2) == 3) 
			{
				VariableEntry temp1 = Create(GetTempVar(), TokenType.REAL);
				VariableEntry temp2 = Create(GetTempVar(), TokenType.REAL);
				Gen("ltof", id1.GetName(), temp1.GetName());
				Gen("f" + opcode, temp1.GetName(), id2.GetName(), temp2.GetName());
				SemStack.push(temp2);
			}
			SemStack.push(EType.ARITHMETIC);
		}

	}

	/* Inserts an intconstant or a realconstant into the ConstantTable */
	private void ActionFortySix(Token toky) throws CompilerError 
	{
		if (toky.GetType() == TokenType.IDENTIFIER) 
		{
			SymbolTableEntry id = LookupID(toky.GetValue());

			if (id == null) 
			{
				throw new SemanticError("Variable " + toky.GetValue() + " is undefined");
			}
			SemStack.push(id);
		} 
		else if ((toky.GetType() == TokenType.INTCONSTANT) || (toky.GetType() == TokenType.REALCONSTANT)) 
		{
			SymbolTableEntry id = LookupID(toky.GetValue());

			if (id == null) 
			{
				if (toky.GetType() == TokenType.INTCONSTANT) 
				{
					id = new ConstantEntry(toky.GetValue(), TokenType.INTEGER);
				} else if (toky.GetType() == TokenType.REALCONSTANT) 
				{
					id = new ConstantEntry(toky.GetValue(), TokenType.REAL);
				}
				ConstantTable.insert(toky.GetValue(), id);
			}
			SemStack.push(id);
		}
		SemStack.push(EType.ARITHMETIC);
	}

	/* Swap ETrue and EFalse on the semantic stack */
	private void ActionFortySeven() throws SemanticError 
	{
		EType etype = (EType) SemStack.pop();
		
		if (etype != EType.RELATIONAL) 
		{
			throw new SemanticError("EType mismatch");
		}
		List<Integer> EFalse = (List<Integer>) SemStack.pop();
		List<Integer> ETrue = (List<Integer>) SemStack.pop();
		SemStack.push(EFalse);
		SemStack.push(ETrue);
		SemStack.push(EType.RELATIONAL);
	}

	/* Generate the load of a temporary VariableEntry */
	private void ActionFortyEight(Token toky) throws CompilerError 
	{
		SymbolTableEntry offset = (SymbolTableEntry) SemStack.pop();
		if (offset != null) 
		{
			if (offset.IsFunction()) 
			{
				Execute(Action.fiftytwo, toky);
			} 
			else 
			{
				SymbolTableEntry id = (SymbolTableEntry) SemStack.pop();
				VariableEntry temp = Create(GetTempVar(), id.GetType());
				Gen("load", id.GetName(), offset.GetName(), temp.GetName());
				SemStack.push(temp);
			}
		}
		SemStack.push(EType.ARITHMETIC);
	}
	
	/* Push the ParameterInfo of a FunctionEntry onto the semantic stack */
	private void ActionFortyNine() throws SemanticError 
	{
		EType etype = (EType)SemStack.pop();
		
		SymbolTableEntry id = (SymbolTableEntry) SemStack.peek();
		SemStack.push(etype);
		
		if (etype != EType.ARITHMETIC) 
		{
			throw new SemanticError("EType mismatch");
		}
		if (!id.IsFunction()) 
		{
			throw new SemanticError("ID is not a function");
		}
		ParamCount.push(0);
		ParamStack.push(id.GetParameterInfo());
	}
	
	/* For each parameter on the semantic stack, generate its param quads, then generate its call and move quads */
	private void ActionFifty() throws CompilerError 
	{
		Stack<SymbolTableEntry> parameters = new Stack<>();
		
		while((SemStack.peek() instanceof SymbolTableEntry) && (((((SymbolTableEntry)SemStack.peek()).IsArray()) ||
				(((SymbolTableEntry)SemStack.peek()).IsConstant()) || (((SymbolTableEntry)SemStack.peek()).IsVariable())))) 
		{
			parameters.push((SymbolTableEntry)SemStack.pop());
		}
		
		while (!parameters.empty()) 
		{
			Gen("param", "~"+parameters.pop().GetName());
			LocalMemory++;
		}
		
		EType etype = (EType)SemStack.pop();
		FunctionEntry id = (FunctionEntry)SemStack.pop();
		
		int numparams = ParamCount.pop();
		
		if (numparams > id.GetNumberOfParameters()) 
		{
			throw new SemanticError("Wrong number of parameters");
		}
		
		Gen("call", "@"+id.GetName(), "@"+Integer.toString(numparams));
		ParamStack.pop();
		NextParam = 0;
		
		VariableEntry temp = Create(GetTempVar(), id.GetResult().GetType());
		Gen("move", id.GetResult().GetName(), temp.GetName());
		
		SemStack.push(temp);
		SemStack.push(EType.ARITHMETIC);
	}
	
	/* Generate param quads or call actions fifty one read or fifty one write */
	private void ActionFiftyOne(Token toky) throws CompilerError {
		Stack<SymbolTableEntry> parameters = new Stack<>();
		
		while((SemStack.peek() instanceof SymbolTableEntry) && ((((SymbolTableEntry)SemStack.peek()).IsArray()) ||
				(((SymbolTableEntry)SemStack.peek()).IsConstant()) || (((SymbolTableEntry)SemStack.peek()).IsVariable()))) 
		{
				parameters.push((SymbolTableEntry)SemStack.pop());
		}
		
		EType etype = (EType)SemStack.pop();
		ProcedureEntry id = (ProcedureEntry)SemStack.pop();
		
		String idn = id.GetName();
		if ((idn.equals("READ")) || (idn.equals("WRITE"))) 
		{
			SemStack.push(id);
			SemStack.push(etype);
			
			while (!parameters.empty()) 
			{
				SemStack.push(parameters.pop());
			}
			if (idn == "READ") 
			{
				Execute(Action.fiftyoneread, toky);
			}
			else 
			{
				Execute(Action.fiftyonewrite, toky);
			}
		}
		else 
		{
			int numParams = ParamCount.pop();
			if (numParams != id.GetNumberOfParameters()) 
			{
				throw new SemanticError("Wrong number of parameters");
			}

			while(!parameters.empty()) 
			{
				Gen("param", "~"+parameters.pop().GetName());
				LocalMemory++;
			}
			Gen("call", "@"+id.GetName(), "@"+Integer.toString(numParams));
			ParamStack.pop();
			NextParam = 0;
		}
	}
	
	/* Generates the quads for an output statement of read */
	private void ActionFiftyOneRead() throws CompilerError 
	{
		Stack<SymbolTableEntry> parameters = new Stack<>();
		
        while ((SemStack.peek() instanceof SymbolTableEntry) && (((SymbolTableEntry)SemStack.peek()).IsVariable())) 
        {
            parameters.push((SymbolTableEntry)SemStack.pop());
        }

        while (!parameters.empty()) 
        {
            SymbolTableEntry id = parameters.pop();
            if (id.GetType() == TokenType.REAL) 
            {
                Gen("finp", id.GetName());
            }
            else 
            {
                Gen("inp", id.GetName());
            }
        }
        EType etype = (EType)SemStack.pop();
        SymbolTableEntry id = (SymbolTableEntry)SemStack.pop();
        ParamCount.pop();
	}
	
	/* Generates the quads for an input statement of write */
	private void ActionFiftyOneWrite() throws CompilerError 
	{
		Stack<SymbolTableEntry> parameters = new Stack<>();
		
        while ((SemStack.peek() instanceof SymbolTableEntry) && ((((SymbolTableEntry)SemStack.peek()).IsConstant()) ||
              (((SymbolTableEntry)SemStack.peek()).IsVariable()))) 
        {
            parameters.push((SymbolTableEntry)SemStack.pop());
        }

        while (!parameters.empty()) 
        {
            SymbolTableEntry id = parameters.pop();
            if (id.IsConstant()) 
            {
                if (id.GetType() == TokenType.REAL) 
                {
                    Gen("foutp", "@"+id.GetName());
                }
                else 
                { 
                    Gen("outp", "@"+id.GetName());
                }
            }
            else 
            { 
                Gen("print", "@"+"\"" +id.GetName() +" = \"");
                if (id.GetType() == TokenType.REAL) 
                {
                    Gen("foutp", id.GetName());
                }
                else 
                { 
                    Gen("outp", id.GetName());
                }
            }
            Gen("newl");
        }
        EType etype = (EType)SemStack.pop();
        SymbolTableEntry id = (SymbolTableEntry)SemStack.pop();
        ParamCount.pop();
	}
	
	/* Generates the call and move of a function */
	private void ActionFiftyTwo() throws CompilerError 
	{
		EType etype = (EType)SemStack.pop();
	       SymbolTableEntry id = (SymbolTableEntry)SemStack.pop();
	       
	       if (!id.IsFunction()) 
	       {
	    	   throw new SemanticError("ID is not a function");
	       }
	       
	       if (id.GetNumberOfParameters() > 0) 
	       {
	    	   throw new SemanticError("Wrong number of parameters");
	       }
	       Gen("call", "@"+id.GetName(), "@"+0);
	       VariableEntry temp = Create(GetTempVar(), id.GetType());
	       Gen("move", id.GetResult().GetName(), temp.GetName());
	       
	       SemStack.push(temp);
	       SemStack.push(null);
	}

	/* Checks that the currentfunction is the top of the semantic stack and push its result 
	 *   VariableEntry onto the stack
	 */
	private void ActionFiftyThree() throws SemanticError 
	{
		EType etype = (EType) SemStack.pop();
		SymbolTableEntry id = (SymbolTableEntry) SemStack.pop();
		if (id.IsFunction()) 
		{
			 if (id != CurrentFunction) 
			 {
				 throw new SemanticError("Illegal procedure");
			 }
			 SemStack.push(id.GetResult());
			 SemStack.push(EType.ARITHMETIC);
		}
		else 
		{
			SemStack.push(id);
			SemStack.push(etype);
		}
	}

	/* Check that the top of the semantic stack is a procedure */
	private void ActionFiftyFour() throws CompilerError 
	{
		EType etype = (EType) SemStack.pop();
		SymbolTableEntry id = (SymbolTableEntry) SemStack.peek();
		SemStack.push(etype);

		if (!(id.IsProcedure())) 
		{
			throw new SemanticError("id is not a procedure error");
		}
	}

	/* Generates the free quad, backpatched later */
	private void ActionFiftyFive() throws CompilerError 
	{
		Backpatch(GlobalStore, GlobalMemory);
		Gen("free", "@" + Integer.toString(GlobalMemory));
		Gen("PROCEND");
	}
 
	/* Generates the procbegin main quad and the alloc memory quad, backpatched later */
	private void ActionFiftySix() throws CompilerError 
	{
		Gen("PROCBEGIN", "@" + "main");
		GlobalStore = Quadyos.GetNextQuad();
		Gen("alloc", "@" + "_");
	}

	private int TypeCheck(SymbolTableEntry x, SymbolTableEntry y) throws CompilerError 
	{
		SymbolTableEntry symEnt_1 = LookupID(x.GetName());

		SymbolTableEntry symEnt_2 = LookupID(y.GetName());

		if ((symEnt_1 != null) && (symEnt_2 != null)) 
		{
			if (symEnt_1.GetType() == TokenType.INTEGER) 
			{
				if (symEnt_2.GetType() == TokenType.INTEGER) 
				{
					return 0;
				}
				else if (symEnt_2.GetType() == TokenType.REAL) 
				{
					return 3;
				}
			}
			else if (symEnt_1.GetType() == TokenType.REAL) 
			{
				if (symEnt_2.GetType() == TokenType.INTEGER) 
				{
					return 2;
				}
				else if (symEnt_2.GetType() == TokenType.REAL) 
				{
					return 1;
				}
			}
		}
		throw new SemanticError("SymbolTableEntry not in any table or not INT/REAL");
	}

	private int GetSTEAddress(SymbolTableEntry ste) throws CompilerError {
		if ((ste.IsArray()) || (ste.IsVariable())) {
			return ste.GetAddress();
		} else if (ste.IsConstant()) {

			VariableEntry temp = Create(GetTempVar(), ste.GetType());
			Gen("move", "@" + ste.GetName(), temp.GetName());

			return temp.GetAddress();
		}
		throw new SemanticError("SymbolTableEntry is not a constant, array, or variable " + ste.StringEntry());
	}

	private String GetSTEPrefix(SymbolTableEntry ste) 
	{
		if (Global) 
		{
            return "_";
        }
        else 
        { // local
            SymbolTableEntry entry = LocalTable.lookup(ste.GetName());
            if (entry == null)
            { // entry is a global variable
            	
            	SymbolTableEntry entryo = ConstantTable.lookup(ste.GetName());
            	
            	if (entryo == null) 
            	{
            		return "_"; 
            	}
            	else 
            	{
            		if (entryo.IsParameter()) 
            		{
                        return "^%";
                    }
                    else 
                    { // ste is not a parameter
                        return "%";
                    }
            	}
            }
            else 
            {
                if (entry.IsParameter()) 
                {
                    return "^%";
                }
                else 
                { // ste is not a parameter
                    return "%";
                }
            }
        }
	}
	
	private String GetParamPrefix(SymbolTableEntry param) 
	{
	     if (Global) 
	     {
             return "@_";
         }
         else 
         { // local
             if (param.IsParameter()) 
             {
                 return "%";
             }
             else 
             {
                 return "@%";
             }
         }
	}

	private Quadruple Gen(String... args) throws CompilerError 
	{

		Quadruple quado;

		String first = args[0];
		String[] inputs = new String[4];
		inputs[0] = first;
		
		int count = 1;
		for (String i : args) {
			if (!(i.equals(first))) {
				if (i.charAt(0) == '@') {
					String ely = i.substring(1);
					inputs[count] = ely;
					count++;
				} else if (i.charAt(0) == '~') {
					String ely = i.substring(1);
					String pr = GetParamPrefix(LookupID(ely));
					inputs[count] = pr + Integer.toString(GetSTEAddress(LookupID(ely)));
				} else if (!(LookupID(i) == null)) {
					String pr = GetSTEPrefix(LookupID(i));
					inputs[count] = pr + Integer.toString(GetSTEAddress((LookupID(i))));
					count++;
				}
			}
		}

		quado = new Quadruple(inputs);
		Quadyos.AddQuad(quado);
		return quado;
	}

	private void Backpatch(int i, int x) 
	{
		Quadyos.SetField(i, 1, Integer.toString(x));
	}

	private void Backpatch(List<Integer> list, int x) 
	{
		for (Integer i : list) 
		{
			if (Quadyos.GetField(i, 0).equals("goto")) 
			{
				Quadyos.SetField(i, 1, Integer.toString(x));
			}
			else
			{
				Quadyos.SetField(i, 3, Integer.toString(x));
			}
		}
	}

	private SymbolTableEntry LookupID(String id) 
	{
		SymbolTableEntry ste = LocalTable.lookup(id);

		if (ste == null) 
		{
			ste = GlobalTable.lookup(id);
		}
		if (ste == null) 
		{
			ste = ConstantTable.lookup(id);
		}
		return ste;
	}

	private List<Integer> MakeList(int i) 
	{
		List<Integer> listy = new ArrayList<Integer>();
		listy.add(i);
		return listy;
	}

	private List<Integer> Merge(List<Integer> listy1, List<Integer> listy2) 
	{
		List<Integer> listo = new ArrayList<Integer>();
		listo.addAll(listy1);
		listo.addAll(listy2);
		return listo;
	}

	private String GetTempVar() 
	{
		TempVars++;
		return ("$$TEMP" + TempVars);
	}

	private VariableEntry Create(String name, TokenType type) throws CompilerError 
	{
		VariableEntry ve = new VariableEntry();
		ve.SetType(type);
		ve.SetName(name);

		if (Global) 
		{
			ve.SetAddress( /*-1 */ GlobalMemory);
			GlobalMemory++;
			GlobalTable.insert(name, ve);
		}
		else 
		{
			ve.SetAddress( /*-1 */ LocalMemory);
			LocalMemory++;
			LocalTable.insert(name, ve);
		}
		return ve;
	}

	/* Prints out the semantic stack each time an item was popped from it
	 *   only if the SemPrint flag is set in the Parser class */
	public void SemanticDump() 
	{
		System.out.println(">>-  " + SemCount + "  -<<");

		String stemstack = "[";

		for (Object t : SemStack) 
		{
			if (t == null) 
			{
				stemstack += "null";
				stemstack += ", ";
			}
			else if (t instanceof SymbolTableEntry) 
			{
				SymbolTableEntry s = (SymbolTableEntry) t;
				stemstack += s.StringEntry();
				stemstack += ", ";
			}
			else if (t instanceof Token)
			{
				Token s = (Token) t;
				stemstack += s.TokenToString();
				stemstack += ", ";
			}
			else 
			{
				stemstack += t.toString();
				stemstack += ", ";
			}
		}
		if (!(SemStack.isEmpty())) 
		{
			stemstack = stemstack.substring(0, stemstack.length() - 2);
		}
		stemstack += "]";

		System.out.println("Semantic Stack ::==> " + stemstack);

		SemCount += 1;
	}
	
	public void PrintQuadruples() 
	{
		Quadyos.Print();
	}
}
