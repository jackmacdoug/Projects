package compiler;

public enum NonTerminal implements RSymbol 
{
	program, identifier_list, identifier_list_tail, declarations,
	declaration_list, declaration_list_tail, type, standard_type, 
	array_type, sub_declarations, subprogram_declaration, subprogram_head, 
	arguments, parameter_list, parameter_list_tail, compound_statement, 
	statement_list, statement_list_tail, statement, else_clause,
	elementary_statement, es_tail, subscript, parameters,
	expression_list, expression_list_tail, expression, 
	expression_tail, simple_expression, simple_expression_tail, 
	term, term_tail, factor, factor_tail, actual_parameters, 
	sign, Goal, constant
	; 
	
    public boolean IsToken() 
    {
    	return false;
    }
    
    public boolean IsAction() 
    {
    	return false;
    }
}
