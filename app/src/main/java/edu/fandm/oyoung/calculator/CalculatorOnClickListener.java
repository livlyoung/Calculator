package edu.fandm.oyoung.calculator;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class CalculatorOnClickListener implements View.OnClickListener{
    public static final String TAG = "CalcOnClickListener";

    @Override
    public void onClick(View v) {
        //Dani and I discussed this
        TextView tv;
        try{
            View parent = (View) v.getParent().getParent();
            tv = (TextView) parent.findViewById(R.id.textView_equation);
            String equation;
            Button b = (Button) v;
            String button_clicked = b.getText().toString();
            equation = tv.getText().toString();

            if(equation.contains("=")){
                String [] parts = equation.split("=");
                equation = parts[1];
                tv.setText(equation);
            }

            String final_text = equation + button_clicked;
            tv.setText(final_text);

        }catch(IllegalArgumentException e){
            Log.d(TAG, "Exception Caught.");
        }
    }
}
