package edu.fandm.oyoung.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class Crunchr extends AppCompatActivity {
    public static final String TAG = "Crunchr_Main";
    public static ArrayList<String> historyArr = new ArrayList<String>();


    public static String infixToPostfix(String expression) {
        //Source: ChatGPT
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();
        boolean isDigit = false;
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                postfix.append(c);
                isDigit = true;
            } else {
                if (isDigit) {
                    postfix.append(" ");
                }

                if (c == '^') {
                    stack.push(c);
                } else if (c == '+' || c == '-') {
                    while (!stack.isEmpty() && (stack.peek() == '^' || stack.peek() == '*' || stack.peek() == '/' || stack.peek() == '+' || stack.peek() == '-')) {
                        postfix.append(stack.pop()).append(" ");
                    }
                    stack.push(c);
                } else if (c == '*' || c == '/') {
                    while (!stack.isEmpty() && (stack.peek() == '^' || stack.peek() == '*' || stack.peek() == '/')) {
                        postfix.append(stack.pop()).append(" ");
                    }
                    stack.push(c);
                } else if (c == '(') {
                    stack.push(c);
                } else if (c == ')') {
                    while (!stack.isEmpty() && stack.peek() != '(') {
                        postfix.append(stack.pop()).append(" ");
                    }
                    try{
                        stack.pop();
                    }catch(EmptyStackException e){
                        Log.d("infixToPostfix", e.toString());
                    }
                }
                isDigit = false;
            }
        }

        if (isDigit) {
            postfix.append(" ");
        }

        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(" ");
        }

        return postfix.toString().trim();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        Button one = (Button) findViewById(R.id.one);
        Button two = (Button) findViewById(R.id.two);
        Button three = (Button) findViewById(R.id.three);
        Button four = (Button) findViewById(R.id.four);
        Button five = (Button) findViewById(R.id.five);
        Button six = (Button) findViewById(R.id.six);
        Button seven = (Button) findViewById(R.id.seven);
        Button eight = (Button) findViewById(R.id.eight);
        Button nine = (Button) findViewById(R.id.nine);
        Button zero = (Button) findViewById(R.id.zero);
        Button decimal = (Button) findViewById(R.id.decimal);
        Button add = (Button) findViewById(R.id.add);
        Button subtract = (Button) findViewById(R.id.subtract);
        Button multiply = (Button) findViewById(R.id.multiply);
        Button divide = (Button) findViewById(R.id.divide);
        Button exponent = (Button) findViewById(R.id.exponentiation);
        Button openParenthesis = (Button) findViewById(R.id.open_parenthesis);
        Button closedParenthesis = (Button) findViewById(R.id.closing_parenthesis);

        one.setOnClickListener(new CalculatorOnClickListener());
        two.setOnClickListener(new CalculatorOnClickListener());
        three.setOnClickListener(new CalculatorOnClickListener());
        four.setOnClickListener(new CalculatorOnClickListener());
        five.setOnClickListener(new CalculatorOnClickListener());
        six.setOnClickListener(new CalculatorOnClickListener());
        seven.setOnClickListener(new CalculatorOnClickListener());
        eight.setOnClickListener(new CalculatorOnClickListener());
        nine.setOnClickListener(new CalculatorOnClickListener());
        zero.setOnClickListener(new CalculatorOnClickListener());
        decimal.setOnClickListener(new CalculatorOnClickListener());
        add.setOnClickListener(new CalculatorOnClickListener());
        subtract.setOnClickListener(new CalculatorOnClickListener());
        multiply.setOnClickListener(new CalculatorOnClickListener());
        divide.setOnClickListener(new CalculatorOnClickListener());
        exponent.setOnClickListener(new CalculatorOnClickListener());
        openParenthesis.setOnClickListener(new CalculatorOnClickListener());
        closedParenthesis.setOnClickListener(new CalculatorOnClickListener());




        Button enter = (Button) findViewById(R.id.enter);
        Button clear = (Button) findViewById(R.id.clear);
        Button historyButton = (Button) findViewById(R.id.history);

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), history.class);
                startActivity(i);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parent = (View) view.getParent().getParent();
                TextView tv = (TextView) parent.findViewById(R.id.textView_equation);
                tv.setText("");

            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parent = (View) view.getParent().getParent();
                TextView tv = (TextView) parent.findViewById(R.id.textView_equation);
                String equation = tv.getText().toString();
                Log.d(TAG, equation);
                String strExpr = infixToPostfix(equation);
                Log.d(TAG, strExpr);
                String [] splitted = strExpr.split(" ");
                Expression expr = null;
                try{
                    expr = Expression.expressionFromPostfix(splitted);
                }catch(IllegalArgumentException s){
                    Toast.makeText(Crunchr.this, s.toString(), Toast.LENGTH_SHORT).show();
                }

                try{
                    expr.simplify().toString();
                    tv.setText(expr.toInfix() + "=" + expr.simplify().toString());
                    historyArr.add(expr.toInfix() + "=" + expr.simplify().toString());

                }catch(Exception e){
                    Toast.makeText(Crunchr.this, e.toString(), Toast.LENGTH_SHORT).show();
                    tv.setText("");

                }
            }
        });



    }
}