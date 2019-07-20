package compiler;

public class SemanticError extends CompilerError 
{

	public SemanticError(String errortext) 
	{
		this.ErrorText = errortext;
		this.ErrorType = "??? Semantic error on Line ";
	}
	
	public boolean IsLex() 
	{
		return false;
	}
}