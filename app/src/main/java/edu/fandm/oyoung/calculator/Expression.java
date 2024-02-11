package edu.fandm.oyoung.calculator;

import android.util.Log;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

//Source: Expression.java was written by me in CS2. I adapted it for this project.

/**
 * A class representing an abstract arithmetic expression
 */
public abstract class Expression{

    public static final String TAG = "Expression.java";
    /**
     * Creates a tree from an expression in postfix notation
     * @param postfix an array of Strings representing a postfix arithmetic expression
     * @return a new Expression that represents postfix
     */
    public static Expression expressionFromPostfix(String[] postfix){

        Stack<Expression> postFixStack = new Stack<Expression>();


        for(int i = 0; i<postfix.length; i++){
            try{
                double intComponent = Double.parseDouble(postfix[i]);
                DoubleOperand Double = new DoubleOperand(intComponent);
                postFixStack.push(Double);
            }catch(NumberFormatException e){

                Expression right = null;
                Expression left = null;

                if(postfix[i].equals("+") || postfix[i].equals("-") || postfix[i].equals("*") || postfix[i].equals("/") || postfix[i].equals("^")){
                    try{
                        right = postFixStack.pop();
                        left = postFixStack.pop();

                    }catch(EmptyStackException s){
                        Log.d(TAG, "invalid expression");
                    }



                }

                if(postfix[i].equals("+")){
                    SumExpression getSum = new SumExpression(left,right);
                    postFixStack.push(getSum);

                }else if (postfix[i].equals("*")){
                    ProductExpression getProduct = new ProductExpression(left, right);
                    postFixStack.push(getProduct);

                }else if(postfix[i].equals("-")){
                    DifferenceExpression getDiff = new DifferenceExpression(left, right);
                    postFixStack.push(getDiff);

                }else if(postfix[i].equals("/")){
                    QuotientExpression getQuo = new QuotientExpression(left, right);
                    postFixStack.push(getQuo);

                }else if(postfix[i].equals("^")){
                    ExponentExpression getExp = new ExponentExpression(left, right);
                    postFixStack.push(getExp);

                }else{
                    throw new IllegalArgumentException("invalid postfix entry");
                }
            }
        }
        if(postFixStack.size() == 1){
            return postFixStack.peek();
        }
        return null;
    }

    /**
     * @return a String that represents this expression in prefix notation.
     */
    public abstract String toPrefix();

    /**
     * @return a String that represents this expression in infix notation.
     */
    public abstract String toInfix();

    /**
     * @return a String that represents this expression in postfix notation.
     */
    public abstract String toPostfix();

    /**
     * @return a String that represents the expression in infix notation
     */
    @Override
    public String toString(){
        return toInfix();
    }

    /**
     * @return a new Expression mathematically equivalent to this one, but simplified.
     */
    public abstract Expression simplify();

    /**
     * Evaluates the expression given assignments of values to variables.
     * @param assignments a HashMap from Strings (variable names) to Doubles (values).
     * @return the result of evaluating the expression with the given variable assignments
     */
    public abstract double evaluate(HashMap<String, Double> assignments);

    /**
     * @return a Set of the variables contained in this expression
     */
    public abstract Set<String> getVariables();

    @Override
    public abstract boolean equals(Object obj);

    /**
     * Prints the expression as a tree in DOT format for visualization
     * @param filename the name of the output file
     */
    public void drawExpression(String filename) throws IOException{
        BufferedWriter bw = null;
        FileWriter fw = new FileWriter(filename);
        bw = new BufferedWriter(fw);

        bw.write("graph Expression {\n");

        drawExprHelper(bw);

        bw.write("}\n");

        bw.close();
        fw.close();
    }

    /**
     * Recursively prints the vertices and edges of the expression tree for visualization
     * @param bw the BufferedWriter to write to
     */
    protected abstract void drawExprHelper(BufferedWriter bw) throws IOException;
}

/**
 * A class representing an abstract operand
 */
abstract class Operand extends Expression{
}

/**
 * A class representing an expression containing only a single Double value
 */
class DoubleOperand extends Operand{
    protected double operand;
    /**
     * Create the expression
     * @param operand the Double value this expression represents
     */
    public DoubleOperand(double operand){
        this.operand = operand;
    }

    /**
     * @return a String that represents this expression in prefix notation.
     */
    public String toPrefix(){
        return Double.toString(operand);
    }

    /**
     * @return a String that represents this expression in postfix notation.
     */
    public String toPostfix(){
        return Double.toString(operand);
    }

    /**
     * @return a String that represents the expression in infix notation
     */
    public String toInfix(){
        return Double.toString(operand);
    }

    /**
     * @return a new Expression mathematically equivalent to this one, but simplified.
     */
    public Expression simplify(){
        DoubleOperand o = new DoubleOperand(operand);
        return o;
    }

    /**
     * Evaluates the expression given assignments of values to variables.
     * @param assignments a HashMap from Strings (variable names) to Doubles (values).
     * @return the result of evaluating the expression with the given variable assignments
     */
    public double evaluate(HashMap<String, Double> assignments){
        return operand;
    }

    /**
     * @return a Set of the variables contained in this expression
     */
    public Set<String> getVariables(){
        TreeSet<String> set = new TreeSet<String>();
        return set;
    }

    /**
     * @param obj and Object to compare to
     * @return true if obj is a logically equivalent Expression
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof DoubleOperand){
            DoubleOperand intObj = (DoubleOperand) obj;
            if(this.operand == intObj.operand){
                return true;
            }
        }
        return false;
    }

    /**
     * Recursively prints the vertices and edges of the expression tree for visualization
     * @param bw the BufferedWriter to write to
     */
    protected void drawExprHelper(BufferedWriter bw) throws IOException{
        bw.write("\tnode"+hashCode()+"[label="+operand+"];\n");
    }
}

/**
 * A class representing an expression containing only a single variable
 */
class VariableOperand extends Operand{
    protected String variable;

    /**
     * Create the expression
     * @param variable the variable name contained with this expression
     */
    public VariableOperand(String variable){
        this.variable = variable;
    }

    /**
     * @return a String that represents this expression in prefix notation.
     */
    public String toPrefix(){
        return variable;
    }

    /**
     * @return a String that represents this expression in postfix notation.
     */
    public String toPostfix(){
        return variable;
    }

    /**
     * @return a String that represents the expression in infix notation
     */
    public String toInfix(){
        return variable;
    }

    /**
     * @return a new Expression mathematically equivalent to this one, but simplified.
     */
    public Expression simplify(){
        VariableOperand v = new VariableOperand(variable);
        return v;
    }

    /**
     * Evaluates the expression given assignments of values to variables.
     * @param assignments a HashMap from Strings (variable names) to Doubles (values).
     * @return the result of evaluating the expression with the given variable assignments
     */
    public double evaluate(HashMap<String, Double> assignments){
        return assignments.get(variable);
    }

    /**
     * @return a Set of the variables contained in this expression
     */
    public Set<String> getVariables(){
        TreeSet<String> set = new TreeSet<String>();
        set.add(variable);
        return set;
    }

    /**
     * @param obj and Object to compare to
     * @return true if obj is a logically equivalent Expression
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof VariableOperand){
            VariableOperand varObj = (VariableOperand) obj;
            if(this.variable.equals(varObj.variable)){
                return true;
            }
        }
        return false;
    }

    /**
     * Recursively prints the vertices and edges of the expression tree for visualization
     * @param bw the BufferedWriter to write to
     */
    protected void drawExprHelper(BufferedWriter bw) throws IOException{
        bw.write("\tnode"+hashCode()+"[label="+variable+"];\n");
    }
}

/**
 * A class representing an expression involving an operator
 */
abstract class OperatorExpression extends Expression{

    protected Expression left;
    protected Expression right;

    /**
     * Create the expression
     * @param left the expression representing the left operand
     * @param right the expression representing the right operand
     */
    public OperatorExpression(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }

    /**
     * @return a Set of the variables contained in this expression
     */
    public Set<String> getVariables(){
        TreeSet<String> set = new TreeSet<String>();
        set.addAll(left.getVariables());
        set.addAll(right.getVariables());
        return set;
    }

    /**
     * @return a String that represents this expression in prefix notation.
     */
    public String toPrefix(){
        return getOperator() + left.toPrefix() + right.toPrefix();
    }

    /**
     * @return a String that represents this expression in postfix notation.
     */
    public String toPostfix(){
        return left.toPostfix() + right.toPostfix() + getOperator();
    }

    /**
     * @return a String that represents the expression in infix notation
     */
    public String toInfix(){
        return "(" + left.toInfix()+ getOperator() + right.toInfix() + ")";
    }

    /**
     * @return a string representing the operator
     */
    protected abstract String getOperator();

    /**
     * Recursively prints the vertices and edges of the expression tree for visualization
     * @param bw the BufferedWriter to write to
     */
    protected void drawExprHelper(BufferedWriter bw) throws IOException{
        String rootID = "\tnode"+hashCode();
        bw.write(rootID+"[label=\""+getOperator()+"\"];\n");

        bw.write(rootID + " -- node" + left.hashCode() + ";\n");
        bw.write(rootID + " -- node" + right.hashCode() + ";\n");
        left.drawExprHelper(bw);
        right.drawExprHelper(bw);
    }
}

/**
 * A class representing an expression involving an sum
 */
class SumExpression extends OperatorExpression{
    /**
     * Create the expression
     * @param left the expression representing the left operand
     * @param right the expression representing the right operand
     */
    public SumExpression(Expression left, Expression right){
        super(left, right);
    }

    /**
     * @return a string representing the operand
     */
    protected String getOperator(){
        return "+";
    }

    /**
     * @return a new Expression mathematically equivalent to this one, but simplified.
     */
    public Expression simplify(){

        SumExpression expression = new SumExpression(left, right);

        DoubleOperand zero = new DoubleOperand(0);

        Double rightOperand;
        Double leftOperand;

        try{
            rightOperand = Double.parseDouble(right.simplify().toString());
            leftOperand = Double.parseDouble(left.simplify().toString());
            Double sum = leftOperand + rightOperand;
            DoubleOperand oper = new DoubleOperand(sum);
            return oper.simplify();
        }catch(NumberFormatException e){
        }

        //x + 0 = x, 1 + 0 = 1.
        if(right.equals(zero)){
            return left.simplify();
        }else if(left.equals(zero)){
            return right.simplify();
        }else if(left instanceof DoubleOperand && right instanceof Expression){
            SumExpression ex1 = new SumExpression(left, right.simplify());
            return ex1;
        }else if(right instanceof DoubleOperand && left instanceof Expression){
            SumExpression ex2 = new SumExpression(left.simplify(), right);
            return ex2;
        }else if(right instanceof Expression && left instanceof Expression){
            SumExpression ex3 = new SumExpression(left.simplify(), right.simplify());
            return ex3.simplify();

        }

        return expression; //if it cannot be simplified it just returns the original expression
    }

    /**
     * Evaluates the expression given assignments of values to variables.
     * @param assignments a HashMap from Strings (variable names) to Doubles (values).
     * @return the result of evaluating the expression with the given variable assignments
     */
    public double evaluate(HashMap<String, Double> assignments){
        double result = left.evaluate(assignments) + right.evaluate(assignments);
        return result;
    }


    /**
     * @param obj and Object to compare to
     * @return true if obj is a logically equivalent Expression
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof SumExpression){
            SumExpression oper = (SumExpression) obj;
            if(this.left.equals(oper.left) && this.right.equals(oper.right)){
                return true;
            }
            if(this.left.equals(oper.right) && this.right.equals(oper.left)){//commutativity
                return true;
            }
        }
        return false;
    }
}

/**
 * A class representing an expression involving an differnce
 */
class DifferenceExpression extends OperatorExpression
{
    /**
     * Create the expression
     * @param left the expression representing the left operand
     * @param right the expression representing the right operand
     */
    public DifferenceExpression(Expression left, Expression right){
        super(left, right);
    }

    /**
     * @return a string representing the operand
     */
    protected String getOperator(){
        return "-";
    }

    /**
     * @return a new Expression mathematically equivalent to this one, but simplified.
     */
    public Expression simplify(){
        DifferenceExpression expression = new DifferenceExpression(left, right);

        DoubleOperand zero = new DoubleOperand(0);
        try{
            Double rightOperand = Double.parseDouble(right.simplify().toString());
            Double leftOperand = Double.parseDouble(left.simplify().toString());
            Double difference = leftOperand - rightOperand;
            DoubleOperand oper = new DoubleOperand(difference);
            return oper.simplify();
        }catch(NumberFormatException e){
        }


        if(right.equals(zero)){
            return left.simplify();
        }else if(right.equals(left)){
            return zero;
        }else if(left instanceof DoubleOperand && right instanceof Expression){
            DifferenceExpression ex1 = new DifferenceExpression(left, right.simplify());
            return ex1;
        }else if(right instanceof DoubleOperand && left instanceof Expression){
            DifferenceExpression ex2 = new DifferenceExpression(left.simplify(), right);
            return ex2;
        }else if(right instanceof Expression && left instanceof Expression){
            DifferenceExpression ex3 = new DifferenceExpression(left.simplify(), right.simplify());
            return ex3.simplify();

        }

        return expression; //if it cannot be simplified it just returns the original expression
    }

    /**
     * Evaluates the expression given assignments of values to variables.
     * @param assignments a HashMap from Strings (variable names) to Doubles (values).
     * @return the result of evaluating the expression with the given variable assignments
     */
    public double evaluate(HashMap<String, Double> assignments){
        double result = left.evaluate(assignments) - right.evaluate(assignments);
        return result;
    }

    /**
     * @param obj and Object to compare to
     * @return true if obj is a logically equivalent Expression
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof DifferenceExpression){
            DifferenceExpression oper = (DifferenceExpression) obj;
            if(this.left.equals(oper.left) && this.right.equals(oper.right)){
                return true;
            }
        }
        return false;
    }
}

/**
 * A class representing an expression involving a product
 */
class ProductExpression extends OperatorExpression
{
    /**
     * Create the expression
     * @param left the expression representing the left operand
     * @param right the expression representing the right operand
     */
    public ProductExpression(Expression left, Expression right){
        super(left, right);
    }

    /**
     * @return a string representing the operand
     */
    protected String getOperator(){
        return "*";
    }

    /**
     * @return a new Expression mathematically equivalent to this one, but simplified.
     */
    public Expression simplify(){
        ProductExpression expression = new ProductExpression(left, right);

        DoubleOperand zero = new DoubleOperand(0);
        DoubleOperand one = new DoubleOperand(1);

        try{
            Double rightOperand = Double.parseDouble(right.simplify().toString());
            Double leftOperand = Double.parseDouble(left.simplify().toString());
            Double product = leftOperand * rightOperand;
            DoubleOperand oper = new DoubleOperand(product);
            return oper.simplify();
        }catch(NumberFormatException e){
        }

        //x * 0 = 0, 1 * 0 = 0
        if(right.equals(zero)){
            return zero;
        }else if(left.equals(zero)){ //0 * x = 0,  0 * 1 = 0
            return zero;
        }else if(right.equals(one)){//x * 1 = x
            return left.simplify();
        }else if(left.equals(one)){
            right.simplify();
        }else if(left instanceof DoubleOperand && right instanceof Expression){
            ProductExpression ex1 = new ProductExpression(left, right.simplify());
            return ex1;
        }else if(right instanceof DoubleOperand && left instanceof Expression){
            ProductExpression ex2 = new ProductExpression(left.simplify(), right);
            return ex2;
        }else if(right instanceof Expression && left instanceof Expression){
            ProductExpression ex3 = new ProductExpression(left.simplify(), right.simplify());
            return ex3.simplify();

        }
        return expression;
    }

    /**
     * Evaluates the expression given assignments of values to variables.
     * @param assignments a HashMap from Strings (variable names) to Doubles (values).
     * @return the result of evaluating the expression with the given variable assignments
     */
    public double evaluate(HashMap<String, Double> assignments){
        double result = left.evaluate(assignments) * right.evaluate(assignments);
        return result;
    }

    /**
     * @param obj and Object to compare to
     * @return true if obj is a logically equivalent Expression
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof ProductExpression){
            ProductExpression oper = (ProductExpression) obj;
            if(this.left.equals(oper.left) && this.right.equals(oper.right)){
                return true;
            }
            if(this.left.equals(oper.right) && this.right.equals(oper.left)){//commutativity
                return true;
            }
        }
        return false;
    }
}

/**
 * A class representing an expression involving a division
 */
class QuotientExpression extends OperatorExpression
{
    /**
     * Create the expression
     * @param left the expression representing the left operand
     * @param right the expression representing the right operand
     */
    public QuotientExpression(Expression left, Expression right){
        super(left, right);
    }

    /**
     * @return a string representing the operand
     */
    protected String getOperator(){
        return "/";
    }

    /**
     * @return a new Expression mathematically equivalent to this one, but simplified.
     */
    public Expression simplify(){
        QuotientExpression expression = new QuotientExpression(left, right);

        DoubleOperand zero = new DoubleOperand(0);
        DoubleOperand one = new DoubleOperand(1);

        try{
            Double rightOperand = Double.parseDouble(right.simplify().toString());
            Double leftOperand = Double.parseDouble(left.simplify().toString());

            if(rightOperand != 0){
                Double quotient = leftOperand / rightOperand;
                DoubleOperand oper = new DoubleOperand(quotient);
                return oper.simplify();
            }else{
              throw new IllegalArgumentException("You cannot divide by zero.");
            }

        }catch(NumberFormatException e){

        }


        if(left.equals(zero)){ //0 / x = 0, 0 / 1 = 0...
            return zero;
        }else if(right.equals(one)){ // x / 1 = x, 5 / 1 = 5...
            return left.simplify();
        }else if(right.equals(left)){ // x / x = 1, 5 / 5 = 1...
            return one;
        }else if(left instanceof DoubleOperand && right instanceof Expression){
            QuotientExpression ex1 = new QuotientExpression(left, right.simplify());
            return ex1;
        }else if(right instanceof DoubleOperand && left instanceof Expression){
            QuotientExpression ex2 = new QuotientExpression(left.simplify(), right);
            return ex2;
        }else if(right instanceof Expression && left instanceof Expression){
            QuotientExpression ex3 = new QuotientExpression(left.simplify(), right.simplify());
            return ex3.simplify();

        }

        return expression;
    }

    /**
     * Evaluates the expression given assignments of values to variables.
     * @param assignments a HashMap from Strings (variable names) to Doubles (values).
     * @return the numerica result of evaluating the expression with the given variable assignments
     */
    public double evaluate(HashMap<String, Double> assignments){
        double result = left.evaluate(assignments) / right.evaluate(assignments);
        return result;
    }

    /**
     * @param obj and Object to compare to
     * @return true if obj is a logically equivalent Expression
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof QuotientExpression){
            QuotientExpression oper = (QuotientExpression) obj;
            if(this.left.equals(oper.left) && this.right.equals(oper.right)){
                return true;
            }
        }
        return false;
    }
}


/**
 * A class representing an expression involving a exponent
 */
class ExponentExpression extends OperatorExpression
{
    /**
     * Create the expression
     * @param left the expression representing the left operand
     * @param right the expression representing the right operand
     */
    public ExponentExpression(Expression left, Expression right){
        super(left, right);
    }

    /**
     * @return a string representing the operand
     */
    protected String getOperator(){
        return "^";
    }

    /**
     * @return a new Expression mathematically equivalent to this one, but simplified.
     */
    public Expression simplify(){
        ExponentExpression expression = new ExponentExpression(left, right);

        Double rightOperand = Double.parseDouble(right.simplify().toString());
        Double leftOperand = Double.parseDouble(left.simplify().toString());

        Double power = Math.pow(leftOperand, rightOperand);
        DoubleOperand oper = new DoubleOperand(power);
        return oper;
    }

    /**
     * Evaluates the expression given assignments of values to variables.
     * @param assignments a HashMap from Strings (variable names) to Doubles (values).
     * @return the numerica result of evaluating the expression with the given variable assignments
     */
    public double evaluate(HashMap<String, Double> assignments){
        double result = Math.pow(left.evaluate(assignments),right.evaluate(assignments));
        return result;
    }

    /**
     * @param obj and Object to compare to
     * @return true if obj is a logically equivalent Expression
     */
    @Override
    public boolean equals(Object obj){
        if(obj instanceof ExponentExpression){
            ExponentExpression oper = (ExponentExpression) obj;
            if(this.left.equals(oper.left) && this.right.equals(oper.right)){
                return true;
            }
        }
        return false;
    }
}