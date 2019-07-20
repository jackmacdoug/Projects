package compiler;

public enum TokenType implements RSymbol 
{
    PROGRAM, BEGIN, END, VAR, FUNCTION, PROCEDURE, 
    RESULT, INTEGER, REAL, ARRAY, OF, IDENTIFIER, 
    IF, THEN, ELSE, WHILE, DO, NOT, 
    INTCONSTANT, REALCONSTANT, RELOP, MULOP, ADDOP, ASSIGNOP, 
    COMMA, SEMICOLON, COLON, LEFTPAREN, RIGHTPAREN, DOUBLEDOT, 
    LEFTBRACKET, RIGHTBRACKET, UNARYMINUS, UNARYPLUS, ENDMARKER, ENDOFFILE, $;


    public boolean IsToken() 
    {
    	return true;
    }
    
    public boolean IsAction() 
    {
    	return false;
    }
}
