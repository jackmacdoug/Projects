package compiler;

public class ArrayEntry extends SymbolTableEntry 
{

	protected int Address;
	protected TokenType Type;
	protected int UpperBound;
	protected int LowerBound;
	protected boolean Parameter = false;
	
	public ArrayEntry(String name) 
	{
		this.Name = name;
	}
	
	public ArrayEntry (String name, int address, TokenType type, int upperbound, int lowerbound) 
	{
		Name = name;
		Address = address;
		Type = type;
		UpperBound = upperbound;
		LowerBound = lowerbound;
	}
	
	/* Inherited methods, mutators, and accessors */
	protected boolean IsArray() 
	{
		return true;
	}
	protected boolean IsParameter() 
	{
		return Parameter;
	}
	protected void PrintEntry() 
	{
		System.out.println("ArrayEntry:");
		System.out.println("  name: " + Name);
		System.out.println("  address: " + Address);
		System.out.println("  type: " + Type);
		System.out.println("  upperBound: " + UpperBound);
		System.out.println("  lowerBound: " + LowerBound);
	}
	protected String StringEntry() 
	{
		return "{Array Entry, " + Name + "}";
	}
	protected int GetAddress() 
	{
		return Address;
	}
	protected TokenType GetType() 
	{
		return Type;
	}
	protected void SetType(TokenType type) 
	{
		Type = type;
	}
	protected void SetUpperBound(int upperbound) 
	{
		UpperBound = upperbound;
	}
	protected void SetLowerBound(int lowerbound) 
	{
		LowerBound = lowerbound;
	}
	protected void SetAddress(int address) 
	{
		Address = address;
	}
	protected int GetLowerBound() 
	{
		return LowerBound;
	}
	protected void SetParameter() 
	{
		Parameter = true;
	}
}
