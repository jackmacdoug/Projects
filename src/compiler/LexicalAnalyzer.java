
package compiler;

import java.io.*;
import java.util.*;

public class LexicalAnalyzer 
{
	
    private Hashtable<String, Token> Keywords;
    
    private CharStream Stream;
    
    private String Buffer = "";
    
    //max size (in length) for both identifiers and constants
    private final int MAX_SIZE = 32;
    
    private TokenType[] Addops = new TokenType[]{TokenType.RIGHTPAREN, 
        TokenType.RIGHTBRACKET, TokenType.IDENTIFIER, TokenType.INTCONSTANT, 
        TokenType.REALCONSTANT};
    
    /* The LexicalAnalyzer constructor takes in a CharStream object and initializes the
     *   keywords Hashtable (used to get the index of token keywords)
     */
    public LexicalAnalyzer(CharStream stream) throws FileNotFoundException 
    {
    	
        Stream = stream;
        
        Keywords = new Hashtable<>();

        Keywords.put("IF", new Token(TokenType.IF, null));
        Keywords.put("THEN", new Token(TokenType.THEN, null));
        Keywords.put("ELSE", new Token(TokenType.ELSE, null));
        Keywords.put("WHILE", new Token(TokenType.WHILE, null));
        Keywords.put("DO", new Token(TokenType.DO, null));
        Keywords.put("INTEGER", new Token(TokenType.INTEGER, null));
        Keywords.put("REAL", new Token(TokenType.REAL, null));
        Keywords.put("ARRAY", new Token(TokenType.ARRAY, null));
        Keywords.put("OF", new Token(TokenType.OF, null));
        Keywords.put("RESULT", new Token(TokenType.RESULT, null));
        Keywords.put("DIV", new Token(TokenType.MULOP, "3"));
        Keywords.put("MOD", new Token(TokenType.MULOP, "4"));
        Keywords.put("AND", new Token(TokenType.MULOP, "5"));
        Keywords.put("NOT", new Token(TokenType.NOT, null));
        Keywords.put("OR", new Token(TokenType.ADDOP, "3"));
        Keywords.put("PROGRAM", new Token(TokenType.PROGRAM, null));
        Keywords.put("BEGIN", new Token(TokenType.BEGIN, null));
        Keywords.put("END", new Token(TokenType.END, null));
        Keywords.put("VAR", new Token(TokenType.VAR, null));
        Keywords.put("FUNCTION", new Token(TokenType.FUNCTION, null));
        Keywords.put("PROCEDURE", new Token(TokenType.PROCEDURE, null));
    }
    
    /* returns a single token, taking in the lastSeen Token as input */
    public Token GetNextToken(Token lastSeen) throws IOException, CompilerError 
    {
        Token token = new Token();
        Buffer = "";
        token.Clear();
        
        //sets first char and loops through whitespace, newlines, and tabs        
        char c = SkipWhitespace();
        
        //ignore comments
        while (c == '{') 
        {
            c = Stream.GetChar();
            while (!(c == '}')) 
            {
                c = Stream.GetChar();
            }
            c = SkipWhitespace();
        }
        
        //first char is a letter (must be IDENTIFIER or keyword)
        if (Character.isLetter(c)) 
        {
            token = readLetter(c);
        }
        
        //first char is a number (must be INTCONSTANT or REALCONSTANT)
        else if (Character.isDigit(c)) 
        {
            token = ReadNumber(c);
        }
        
        //getChar returns Character.MIN_VALUE when it reaches the end of the file
        //  and sets the endoffile token 
        else if (c == Character.MIN_VALUE) 
        {
            token.SetType(TokenType.ENDOFFILE);
            token.SetValue(null);
            
        //first char is a symbol
        }
        else 
        {
            token = ReadSymbol(c, lastSeen);
        }
        return token;
    }
    
    private Token readLetter(char nextchar) throws IOException, LexicalError 
    {
        Token token;
        
        //loop on letters and digits adding both to the buffer
        while ((Character.isDigit(nextchar) || Character.isLetter(nextchar)) && (Buffer.length() <= MAX_SIZE)) 
        {
            Buffer += nextchar;
            nextchar = Stream.GetChar();
        }
        
        //if the buffer exceeds max_size, throw an error
        if (Buffer.length() > MAX_SIZE) 
        {
            throw new LexicalError("Identifier too long (MAX_SIZE = 32)");
        }
        
        //if you see anything but whitespace, push it back
        if (!(nextchar == ' ')) 
        {
            Stream.Pushback(nextchar, 1);
        }
        
        token = KeyOrIdent(Buffer);
        return token;
    }
    
    private Token ReadNumber(char nextchar) throws IOException, CompilerError 
    {
        Token token = new Token();
        
        char lookahead;
        Buffer += nextchar;
        
        //first loop on digits, adding them to the buffer
        nextchar = LoopNums();
        
        //if nextChar is a '.' it could be a REALCONSTANT, a '..', or an ENDMARKER
        //  so we need an extra lookahead
        if (nextchar == '.') 
        {
            lookahead = Stream.GetChar();
            
            //double '.' means pushback both and return the INTCONSTANT token
            if ((lookahead == '.') || (!(Character.isDigit(lookahead)))) 
            {
                Stream.Pushback(lookahead);
                Stream.Pushback(nextchar);
                token.SetType(TokenType.INTCONSTANT);
                
            //single '.' means just pushback the second '.' and loop on numbers again
            //  until you see anything besides 0-9 then return the REALCONSTANT token
            }
            else 
            {
                Stream.Pushback(lookahead);
                Buffer += nextchar;
                nextchar = LoopNums();
                if ((nextchar == 'e') || (nextchar == 'E')) 
                {
                    ECase();
                }
                else 
                {
                    Stream.Pushback(nextchar);
                }
                token.SetType(TokenType.REALCONSTANT);
            }
            
        //if there was no '.' then check for an 'e' to tell if intconstant or realconstant
        }
        else if ((nextchar == 'e') || (nextchar == 'E'))
        {
            ECase();
            token.SetType(TokenType.REALCONSTANT);
        } 
        else 
        {
            Stream.Pushback(nextchar);
            token.SetType(TokenType.INTCONSTANT);
        }
        
        //check the constant to see if it's too long, throw an error if so
        if (Buffer.length() > MAX_SIZE) 
        {
            throw new LexicalError("Constant too long (MAX_SIZE = 32)");
        }
        
        token.SetValue(Buffer);
        return token;
    }
    
    private Token ReadSymbol(char nextchar, Token lastseen) throws IOException, LexicalError 
    {
        Token token = new Token();
        
        //some symbols have additional possibilities that require lookahead, 
        //  but most map to only one token
        char lookahead;

        switch (nextchar) 
        {
            case '.' :
                lookahead = Stream.GetChar();
                if (lookahead == '.') 
                {
                    token.SetType(TokenType.DOUBLEDOT);
                }
                else 
                {
                    Stream.Pushback(lookahead);
                    token.SetType(TokenType.ENDMARKER);
                }
                break;
            case ',' :
                token.SetType(TokenType.COMMA);
                break;
            case ';' :
                token.SetType(TokenType.SEMICOLON);
                break;
            case ':' :
                lookahead = Stream.GetChar();
                if (lookahead == '=') 
                {
                    token.SetType(TokenType.ASSIGNOP);
                } 
                else 
                {
                    Stream.Pushback(lookahead);
                    token.SetType(TokenType.COLON);
                }
                break;
            case '+' :
                if (Arrays.asList(Addops).contains(lastseen.GetType())) 
                {
                    token.SetType(TokenType.ADDOP);
                    token.SetValue("1");
                } 
                else 
                {
                    token.SetType(TokenType.UNARYPLUS);
                }
                break;
            case '-' :
                if (Arrays.asList(Addops).contains(lastseen.GetType())) 
                {
                    token.SetType(TokenType.ADDOP);
                    token.SetValue("2");
                } 
                else 
                {
                    token.SetType(TokenType.UNARYMINUS);
                }
                break;
            case '*' :
                token.SetType(TokenType.MULOP);
                token.SetValue("1");
                break;
            case '/' :
                token.SetType(TokenType.MULOP);
                token.SetValue("2");
                break;
            case '=' :
                token.SetType(TokenType.RELOP);
                token.SetValue("1");
                break;
            case '<' :
                token.SetType(TokenType.RELOP);
                lookahead = Stream.GetChar();
                if (lookahead == '>') 
                {
                    token.SetValue("2");
                } 
                else if (lookahead == '=') 
                {
                    token.SetValue("5");
                } 
                else 
                {
                    token.SetValue("3");
                    Stream.Pushback(lookahead);
                } 
                break;
            case '>' :
                token.SetType(TokenType.RELOP);
                lookahead = Stream.GetChar();
                if (lookahead == '=') 
                {
                    token.SetValue("6");
                } 
                else 
                {
                    token.SetValue("4");
                    Stream.Pushback(lookahead);
                }
                break;
            case '(' :
                token.SetType(TokenType.LEFTPAREN);
                break;
            case ')' :
                token.SetType(TokenType.RIGHTPAREN);
                break;
            case '[' :
                token.SetType(TokenType.LEFTBRACKET);
                break;
            case ']' :
                token.SetType(TokenType.RIGHTBRACKET);
                break;
                
            //always an error if we see a '}' that wasn't handled by comment skipping
            case '}' :
                throw new LexicalError("Ill-formed comment");
                
            //default case means the char is not a valid char in vascal
            default:
                throw new LexicalError("Bad character ( " + nextchar  + " )");
        	}
        return token;
    	}
    
    	/* loops through whitespace, newlines, and tabs */
        private char SkipWhitespace() throws IOException 
        {
            char newC = Stream.GetChar();
            
            while ((newC == ' ') || (newC == '\n') || (newC == '\t')) 
            {
                newC = Stream.GetChar();
                
                if (newC == '\n') {
                	Stream.IncrementLine();
                }
            }
            return newC;
        }
        
        /* loops through numbers, adding them to the buffer */
        private char LoopNums()  throws IOException 
        {
            char newC = Stream.GetChar();
            while (Character.isDigit(newC)) 
            {
                Buffer += newC;
                newC = Stream.GetChar();
            }
            return newC;
        }
        
        /* deals with the buffer and pushback when an 'e' is seen during or after a constant */
        private void ECase() throws IOException, CompilerError
        {
            char newC;
            char lookahead;
            newC = Stream.GetChar();

            if ((newC == '-') || (newC == '+')) 
            {
                lookahead = Stream.GetChar();

                if (Character.isDigit(lookahead)) 
                {
                    Buffer += 'e';
                    Buffer += newC;
                    Buffer += lookahead;
                    Stream.Pushback(LoopNums());
                } 
                else 
                {
                	throw new LexicalError("Number in scientific notation without digital exponent");
                }
            }
            else if (Character.isDigit(newC)) 
            {
                Buffer += 'e';
                Buffer += newC;
                Stream.Pushback(LoopNums());
            }
            else
            {
            	throw new LexicalError("Number in scientific notation without digital exponent");
            }
        }
        
        /* takes the complete string buffer and returns a keyword token if it matches
         *   the keywords hashtable, otherwise must be an identifier token
         */
        private Token KeyOrIdent(String lexeme) 
        {
            Token token = new Token();
            
            if (Keywords.containsKey(lexeme.toUpperCase())) 
            {
                token = Keywords.get(lexeme.toUpperCase());
            }
            else 
            {
                token.SetType(TokenType.IDENTIFIER);
                token.SetValue(lexeme.toUpperCase());
            }
            return token;
        }
        
        /* Runs through the charstream, printing each token */
        public void TestLexer() throws IOException, CompilerError 
        {
        	Token lastseen = GetNextToken(null);
        	while (!(lastseen.GetType() == TokenType.ENDOFFILE)) {
        		lastseen.PrintToken();
        		lastseen = GetNextToken(lastseen);
        	}
        	lastseen.PrintToken();
        	System.out.println(" [ ACCEPT ]");
        }
}
