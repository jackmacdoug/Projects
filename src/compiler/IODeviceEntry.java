package compiler;

public class IODeviceEntry extends SymbolTableEntry 
{

	public IODeviceEntry (String name) 
	{
		Name = name;
	}
	
	/* Inherited methods */
	protected void PrintEntry() 
	{
		System.out.println("IODeviceEntry:");
		System.out.println("  name: " + Name);
	}
	protected String StringEntry() 
	{
		return "{IODevice Entry, " + Name + "}";
	}
}
