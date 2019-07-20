package compiler;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Compiler 
{	
	private static CharStream Stream;

	private static LexicalAnalyzer LA;
	private static Parser PA;
	private static SemanticAction SA;
	
	public Compiler(String filename) throws FileNotFoundException, CompilerError 
	{
		Stream = new CharStream(filename);
		
		LA = new LexicalAnalyzer(Stream);
		SA = new SemanticAction();
		
		PA = new Parser(LA, SA);
	}
	
	public void Compile() throws IOException, CompilerError 
	{
		PA.Parse();
		SA.PrintQuadruples();
	}
	
	public void TestLexer() throws IOException, CompilerError 
	{
		LA.TestLexer();
	}
	
	public void TestParser() throws IOException, CompilerError 
	{
		PA.TestParser();
	}
	
	public int GetCurrentLine() 
	{
		return Stream.GetCurrentLine();
	}
}
