package ast;

import ast.Expr.*;

public class TreeEvaluator implements Expr.Visitor<Object> {
	
	public Object evaluate(Expr expr) {
		Object value = call(expr);
		if (value == null) {
			return "null";
		}
		return value;
		
	}
	
	private Object call(Expr expr) {
		return expr.accept(this);
	}
	
	@Override
	public Object visitBinOp(binOp expr) {
		Object left = call(expr.getLeft());
		Object right = call(expr.getRight());
		Object op =  expr.getOp();
		String leftString = left.toString();
		String rightString = right.toString();
		
		switch (expr.getOp().getType()) {
		case PLUS:
			if (left instanceof Integer && right instanceof Integer) {
				
		          return Integer.parseInt(leftString) + Integer.parseInt(rightString);                  
		        } 
			if (left instanceof Double || right instanceof Double) {
				
				double temp =  Double.parseDouble(leftString) + Double.parseDouble(rightString);
				double returnDouble = (double)temp;
				
		          return returnDouble;                     
		        } 

		    if (left instanceof String && right instanceof String) {
		          return leftString + rightString;                  
		        } 
		case MINUS:
			
			if (left instanceof Integer && right instanceof Integer) {
				
		          return Integer.parseInt(leftString) - Integer.parseInt(rightString);                  
		        } 
			if (left instanceof Double || right instanceof Double) {
				
				double temp =  Double.parseDouble(leftString) - Double.parseDouble(rightString);
				double returnDouble = (double)temp;
				
		          return returnDouble;                     
		        } 

		case SLASH:    
			
			if (left instanceof Integer && right instanceof Integer) {
				
		          return Integer.parseInt(leftString) / Integer.parseInt(rightString);                  
		        } 
			if (left instanceof Double || right instanceof Double) {
				
				double temp =  Double.parseDouble(leftString) / Double.parseDouble(rightString);
				double returnDouble = (double)temp;
				
		          return returnDouble;                     
		        } 

		case STAR:
			if (left instanceof Integer && right instanceof Integer) {
				
		          return Integer.parseInt(leftString) * Integer.parseInt(rightString);                  
		        } 
			if (left instanceof Double || right instanceof Double) {
				
				double temp =  Double.parseDouble(leftString) * Double.parseDouble(rightString);
				double returnDouble = (double)temp;
				
		          return returnDouble;                     
		        } 
 
			
		case GREATER:                          
			
	        return Double.parseDouble(leftString) > Double.parseDouble(rightString); 
	    case GREATER_EQUAL:            
	    	
	    	 return Double.parseDouble(leftString) >= Double.parseDouble(rightString); 
	    case LESS:      
	    	
	    	 return Double.parseDouble(leftString) < Double.parseDouble(rightString); 
	    case LESS_EQUAL:  
	    	
	    	 return Double.parseDouble(leftString) <= Double.parseDouble(rightString); 
	    case EXCLAMATION_EQUAL: 
	    	return !isEqual(left, right);
	    case EQUAL_EQUAL: 
	    	return isEqual(left, right);
				
		}
		
		return null;
	}

	@Override
	public Object visitLiteral(Literal expr) {
		
		return expr.getVal();
	}

	@Override
	public Object visitUnaryOp(unaryOp expr) {
		Object right = call(expr.getRight());
		String rightString = right.toString();
		
		
		switch (expr.getOp().getType()) {
		case EXCLAMATION:
			return !isTruthy(right);
		case MINUS:
			return -Double.parseDouble(rightString);
			
		}
			
		return null;
	}

	@Override
	public Object visitGroup(Group expr) {
		
		return call(expr.getNode());
	}
	
	private boolean isEqual(Object left, Object right) {
		boolean isEqual = left.equals(right);
		if (left == null && right ==null) {
			return true;
		}
		if (left == null || right == null) {
			return false;
		}
		
		
		return isEqual;
	}
	private boolean isTruthy(Object obj) {
		if (obj == null) {
			return false;
		}
		else if (obj instanceof Boolean) {
			return (Boolean)obj;
		}
		return true;
		
	}

}
