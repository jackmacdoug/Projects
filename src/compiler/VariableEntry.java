package compiler;

public class VariableEntry extends SymbolTableEntry 
{

	protected int Address;
	protected TokenType Type;
	protected boolean Parameter = false;

	public VariableEntry () 
	{
	}
	
	public VariableEntry (String name) 
	{
		Name = name;
	}
	
	public VariableEntry(String name, int address, TokenType type) 
	{
		Name = name;
		Address = address;
		Type = type;
	}
	
	/* Inherited methods, mutators, and accessors */
	protected boolean IsVariable() 
	{
		return true;
	}
	protected void PrintEntry() 
	{
		System.out.println("VariableEntry:");
		System.out.println("  name: " + Name);
		System.out.println("  address: " + Address);
		System.out.println("  type: " + Type);
	}
	protected String StringEntry() 
	{
		return "{Variable Entry, " + Name + "}";
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
	protected void SetAddress(int address) 
	{
		Address = address;
	}
	protected void SetParameter() 
	{
		Parameter = true;
	}
	protected boolean IsParameter() 
	{
		return Parameter;
	}
}
