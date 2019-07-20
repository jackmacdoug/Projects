package compiler;

import java.util.*;

public class FunctionEntry extends SymbolTableEntry 
{

	protected int NumberOfParameters;
	protected ArrayList<SymbolTableEntry> ParameterInfo;
	protected VariableEntry Result;
	protected TokenType Type;
	
	public FunctionEntry(String name) 
	{
		Name = name;
		ParameterInfo = new ArrayList<>();
	}
	
	public FunctionEntry(String name, VariableEntry result) 
	{
		Name = name;
		Result = result;
		ParameterInfo = new ArrayList<>();
	}
	
	public FunctionEntry (String name, int numberofparameters, 
			ArrayList<SymbolTableEntry> parameterinfo, VariableEntry result) 
	{
		Name = name;
		NumberOfParameters = numberofparameters;
		ParameterInfo = parameterinfo;
		Result = result;
	}
	
	/* Inherited methods, mutators, and accessors */
	protected boolean IsFunction() 
	{
		return true;
	}
	protected void PrintEntry() 
	{
		System.out.println("FunctionEntry:");
		System.out.println("  name: " + Name);
		System.out.println("  numberOfParameters: " + NumberOfParameters);
		System.out.println("  parameterInfo: " + ParameterInfo);
		System.out.println("  result: " + Result);
	}
	protected String StringEntry() 
	{
		return "{Function Entry, " + Name + "}";
	}
	protected VariableEntry GetResult() 
	{
		return Result;
	}
	protected void SetType(TokenType type) 
	{
		Type = type;
	}
	protected TokenType GetType() 
	{
		return Type;
	}
	protected void SetResultType(TokenType type) 
	{
		Result.SetType(type);
	}
	protected void SetNumberOfParameters(int num) 
	{
		NumberOfParameters = num;
	}
	protected int GetNumberOfParameters() 
	{
		return NumberOfParameters;
	}
	protected ArrayList<SymbolTableEntry> GetParameterInfo() 
	{
		return ParameterInfo;
	}
	protected void AddParameter(SymbolTableEntry var) 
	{
		ParameterInfo.add(var);
	}
}
