package compiler;

import java.util.*;

public class SymbolTable 
{
	
	private Hashtable<String, SymbolTableEntry> HT;

	public SymbolTable() 
	{
		HT = new Hashtable<>();
	}
	
	public SymbolTable (int size) 
	{
		HT = new Hashtable<>(size);
	}
	
	public SymbolTableEntry lookup (String s) 
	{
		if (HT.containsKey(s)) 
		{
			return HT.get(s);
		}
		return null;
	}
	
	public void insert (String s, SymbolTableEntry e) throws CompilerError 
	{
		if (!(HT.containsKey(s))) 
		{
			HT.put(s, e);
		}
	}
	
	public int size () 
	{
		return HT.size();
	}
	
	public void dumpTable() 
	{
		System.out.println(">>- SYMBOL TABLE -<<");
		for (SymbolTableEntry s : HT.values()) 
		{
			s.PrintEntry();
		}
		System.out.println("\n");
	}
}
