package compiler;

import java.util.ArrayList;

public class ProcedureEntry extends SymbolTableEntry 
{

	protected int NumberOfParameters;
	protected ArrayList<SymbolTableEntry> ParameterInfo;
	
	public ProcedureEntry (String name) 
	{
		Name = name;
		ParameterInfo = new ArrayList<>();
	}
	
	public ProcedureEntry (String name, int numberofparameters, ArrayList<SymbolTableEntry> parameterinfo) 
	{
		Name = name;
		NumberOfParameters = numberofparameters;
		ParameterInfo = parameterinfo;
	}
	
	/* Inherited methods, mutators, and accessors */
	protected boolean IsProcedure() 
	{
		return true;
	}
	protected void PrintEntry() 
	{
		System.out.println("ProcedureEntry:");
		System.out.println("  name: " + Name);
		System.out.println("  numberOfParameters: " + NumberOfParameters);
		System.out.println("  parameterInfo: " + ParameterInfo);
	}
	protected String StringEntry() 
	{
		return "{Procedure Entry, " + Name + "}";
	}
	protected void AddParameter(SymbolTableEntry var) 
	{
		ParameterInfo.add(var);
	}
	protected ArrayList<SymbolTableEntry> GetParameterInfo() 
	{
		return this.ParameterInfo;
	}
	protected int GetNumberOfParameters() 
	{
		return this.NumberOfParameters;
	}
	protected void SetNumberOfParameters(int num) 
	{
		this.NumberOfParameters = num;
	}
}
