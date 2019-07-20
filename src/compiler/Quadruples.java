package compiler;

import java.util.Enumeration;
import java.util.Vector;

public class Quadruples {
	private Vector<Quadruple> Quadys;
	private int NextQuad;
	
	public Quadruples()
	{		
		Quadys = new Vector<Quadruple>();
		NextQuad = 0;
		Quadys.add(new Quadruple(null, null, null, null));
		NextQuad++;
	}

	public String GetField(int quadindex, int field)
	{
		return Quadys.elementAt(quadindex).Quad[field];
	}

	public void SetField(int quadindex, int index, String field)
	{
		Quadys.elementAt(quadindex).Quad[index] = field;
	}

	public int GetNextQuad()
	{
		return NextQuad;
	}
	
	public void IncrementNextQuad()
	{
		NextQuad++;
	}

	public Quadruple GetQuad(int index)
	{
		return Quadys.elementAt(index);
	}

	public void AddQuad(Quadruple quad)
	{
		Quadys.add(NextQuad, quad);
		NextQuad++;
	}

	public void Print()
	{
		int quadlabel = 1;
		
		System.out.println("CODE");

		Enumeration<Quadruple> e = this.Quadys.elements();
		e.nextElement();
		
		while (e.hasMoreElements())
		{
			Quadruple quad = e.nextElement();
			System.out.print(quadlabel + ":  " + quad.Quad[0]);

			if (quad.Quad[1] != null)
				System.out.print(""
						+ " " + quad.Quad[1]);

			if (quad.Quad[2] != null)
				System.out.print(", " + quad.Quad[2]);

			if (quad.Quad[3] != null)
				System.out.print(", " + quad.Quad[3]); 

			System.out.println();
			quadlabel++;
		}
	}
}
