package compiler;

public class LexicalError extends CompilerError
{
	
    public LexicalError(String errortext) 
    {
        this.ErrorText = errortext;
        this.ErrorType = "\n??? Lexer error on Line ";
    }
    
    public boolean IsLex() 
    {
    	return true;
    }
}
