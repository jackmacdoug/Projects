package compiler;

public class SyntacticError extends CompilerError 
{
    public SyntacticError(String errortext) 
    {
        this.ErrorText = errortext;
        this.ErrorType = "\n??? Parser error on Line ";
    }
    
    public boolean IsLex() 
    {
    	return false;
    }
}