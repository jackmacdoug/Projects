package compiler;

public class ConstantEntry extends SymbolTableEntry 
{

	protected TokenType Type;
	
	public ConstantEntry(String name) 
	{
		Name = name;
	}
	
	public ConstantEntry(String name, TokenType type) 
	{
		Name = name;
		Type = type;
	}
	
	/* Inherited methods, mutators, and accessors */
	protected void PrintEntry() 
	{
		System.out.println("ConstantEntry:");
		System.out.println("  name: " + Name);
		System.out.println("  type: " + Type);
	}
	protected String StringEntry() 
	{
		return "{Constant Entry, " + Name + "}";
	}
	protected boolean IsConstant() 
	{
		return true;
	}
	protected TokenType GetType() 
	{
		return Type;
	}
}
