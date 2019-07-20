package compiler;

import java.util.ArrayList;

public class SymbolTableEntry {
	
	protected String Name;
		
	protected boolean Reserved = false;
	
	/* Flag checks, mutators, and accessors overloaded in Entry classes */
	protected boolean IsVariable() {
		return false;
	}
	protected boolean IsProcedure() {
		return false;
	}
	protected boolean IsFunction() {
		return false;
	}
	protected boolean IsFunctionResult() {
		return false;
	}
	protected boolean IsParameter() {
		return false;
	}
	protected boolean IsArray() {
		return false;
	}
	protected boolean IsReserved() {
		return Reserved;
	}
	protected void SetReserved() {
		this.Reserved = true;
	}
	protected boolean IsConstant() {
		return false;
	}
	protected boolean IsSymbolTableEntry() {
		return true;
	}
	protected void PrintEntry() {
	}
	protected String StringEntry() {
		return "";
	}
	protected String GetName() {
		return Name;
	}
	protected void SetName(String name) {
		Name = name;
	}
	protected int GetAddress() {
		return -1;
	}
	protected TokenType GetType() {
		return null;
	}
	protected VariableEntry GetResult() {
		return null;
	}
	protected void SetNumberOfParameters(int num) {
	}
	protected int GetNumberOfParameters() {
		return -1;
	}
	protected ArrayList<SymbolTableEntry> GetParameterInfo() {
		return null;
	}
	protected void SetParameter() {
	}
	protected void AddParameter(SymbolTableEntry var) {
	}
	protected int GetLowerBound() {
		return -1;
	}
	protected int GetUpperBound() {
		return -1;
	}
}
