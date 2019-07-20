package compiler;

import java.io.*;

public class CharStream 
{ 
    private BufferedReader Reader;

    private int CurrentLine;
        
    private String Pushback = "";
    
    /* The CharStream constructor instantiates the BufferedReader and sets the currentLine to 1 */
    public CharStream(String filename) throws FileNotFoundException 
    {
        CurrentLine = 1;
        Reader = new BufferedReader(new FileReader(filename));
    }
    
    /* returns the next char to getNextToken() */
    public char GetChar() throws IOException 
    {
        char next;
        int nextint;
        //check if any chars have been pushbacked and uses first if so
        if (!(Pushback.equals(""))) 
        {
            next = Pushback.charAt(0);
            //remove from the pushback appropriately
            if (!(Pushback.length() == 1)) 
            {
                Pushback = Pushback.substring(1);
            } 
            else 
            {
                Pushback = "";
            }
            return next;
        //if no pushback call read on the charstream reader
        } 
        else 
        {
            nextint = Reader.read();
        }
        //if read returns -1, the endoffile was reached, return Character.MIN_VALUE
        //  so getNextToken knows
        if (nextint == -1) 
        {
            return Character.MIN_VALUE;
        //otherwise cast the read int as a char and return
        }
        else 
        {
            next = (char) nextint;
        }
        //increment the currentLine when a newline char is seen
        if (next == '\n') 
        {
            CurrentLine += 1;
        }
        return next;
    }
    
    /* adds a char to the pushback object, which is checked before the next read
     *   two types (the realconstant 'e' case handles differently) */
    public void Pushback(char c) 
    {
    	this.Pushback += c;
    }
    public void Pushback(char c, int i) 
    {
    	this.Pushback = c + Pushback;
    }
    
    public int GetCurrentLine() 
    {
    	return CurrentLine;
    }
    
    public void IncrementLine()
    {
    	CurrentLine++;
    }
}
