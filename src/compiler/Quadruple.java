package compiler;

public class Quadruple 
{
	String[] Quad;
	
	public void PrintQuad() 
	{
		for (String i: Quad) 
		{
			System.out.println(i);
		}
	}
	
	/* An object made up of up to 4 strings that represent some memory operation */
	public Quadruple(String ... args) 
	{
		Quad = new String[4];

		if (args == null) 
		{
			Quad[0] = Quad[1] = Quad[2] = Quad[3] = null;
			
		} 
		else 
		{
			int count = 0;
			for (String i : args) 
			{
				Quad[count] = i;
				count++;
			}
		}
	}
}