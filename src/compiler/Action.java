package compiler;

public enum Action implements RSymbol 
{
	one, two, three, four, five, six, seven, nine, eleven, thirteen, fifteen, sixteen,
	seventeen, nineteen, twenty, twentyone, twentytwo, twentyfour, twentyfive, 
	twentysix, twentyseven, twentyeight, twentynine, thirty, thirtyone, thirtytwo, 
	thirtythree, thirtyfour, thirtyfive, thirtysix, thirtyseven, thirtyeight, thirtynine, 
	forty, fortyone, fortytwo, fortythree, fortyfour, fortyfive, fortysix, fortyseven, 
	fortyeight, fortynine, fifty, fiftyone, fiftyoneread, fiftyonewrite, fiftytwo, 
	fiftythree, fiftyfour, fiftyfive, fiftysix
	;
	
	public boolean IsToken() 
	{
		return false;
	}
	
	public boolean IsAction() 
	{
		return true;
	}
}
