package compiler;

public abstract class CompilerError extends Exception 
{

	public int LineNum;
    public String ErrorText;
    public String ErrorType;
    
    public int GetLine() 
    {
        return LineNum;
    }
    public String GetError() 
    {
        return ErrorText;
    }
    public String GetType() 
    {
    	return ErrorType;
    }
    
    public abstract boolean IsLex();

}