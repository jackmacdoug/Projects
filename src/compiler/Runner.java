package compiler;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Runner 
{
	private static final String FILENAME = "/Users/johnmacdougall/Desktop/CompilerAlt/Misc_Tests/recursion.txt";

	public static void main(String[] args) throws FileNotFoundException, IOException, CompilerError 
	{
		
		Compiler CA = new Compiler(FILENAME);
		
		try 
		{
			CA.Compile();
			
			//CA.TestLexer();
			
			//CA.TestParser();	
		}
		catch(CompilerError e) 
		{
			System.out.println("\n" + e.GetType() + CA.GetCurrentLine() + ": "+ e.GetError());
		}	
	}

}
