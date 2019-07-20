package compiler;

//type / value pair
public class Token 
{
    private TokenType Type;
    private String Value;
    
    /* one blank constructor and one that takes input */

    public Token() 
    {
    }

    public Token(TokenType newtype, String newval)
    {
        Type = newtype;
        Value = newval;
    }
    
    public void Clear() 
    {
        Type = null;
        Value = "";
    }
    
    /* Prints the type and value of the Token */
    public String TokenToString() 
    {
    	String eek;
        if (Value == null) 
        {
            eek = "None)";
        } 
        else if ((Type == TokenType.RELOP) || (Type == TokenType.MULOP) 
                || (Type == TokenType.ADDOP)) 
        {
            eek = Value + ")";
        } 
        else 
        {
            eek = "'" + Value + "')";
        }
        String feek = "('" + Type.toString() + "', " + eek;
        return feek;
    }
    
    /* Returns the quadruple-ready string to be used in Gen */
    public String GetOpCode() 
    {
    	if (Type == TokenType.ADDOP) 
    	{
    		switch(Value) 
    		{
    		case "1":
    			return "add";
    		case "2": 
    			return "sub";
    		default:
    			return "or";
    		}
    	}
    	else if (Type == TokenType.MULOP) 
    	{ 
    		switch(Value) 
    		{
    		case "1":
    			return "mul";
    		case "2": 
    			return "div";
    		case "3":
    			return "DIV";
    		case "4":
    			return "mod";
    		default:
    			return "and";
    		}
    	}
    	else 
    	{
    		switch(Value) 
    		{
    		case "1":
    			return "beq";
    		case "2": 
    			return "bne";
    		case "3":
    			return "blt";
    		case "4":
    			return "bgt";
    		case "5":
    			return "ble";
    		default:
    			return "bge";
    		}
    	}
    }
    
    /* Printing format subject to your approval */
    public void PrintToken() 
    {
        System.out.println(this.TokenToString());
    }
    
    /* Used when popping an int Token off of the stack in the SemanticAction */
    public int TokenToInt() 
    {
    	return Integer.parseInt(Value);
    }
    
    /* Mutators and accessors */
    public void SetType (TokenType newtype) 
    {
        Type = newtype;
    }
    public TokenType GetType() 
    {
    	return Type;
    }
    public void SetValue (String newval) 
    {
        Value = newval;
    }
    public String GetValue() 
    {
    	return Value;
    }
}
