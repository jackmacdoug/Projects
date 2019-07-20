package compiler;

public class SymbolTableError extends CompilerError 
{

	public SymbolTableError(String errortext) 
	{
		this.LineNum = -1;
		this.ErrorText = errortext;
		this.ErrorType = "??? Symbol Table error: ";
	}
	
	public boolean IsLex() {
		return false;
	}

}
