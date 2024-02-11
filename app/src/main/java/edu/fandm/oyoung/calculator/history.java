package edu.fandm.oyoung.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class history extends AppCompatActivity {
    ListView list;
    ArrayList<String> allEquations = Crunchr.historyArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    //Source: https://www.geeksforgeeks.org/android-listview-in-java-with-example/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        list = (ListView) findViewById(R.id.history_listView);
        ArrayAdapter<String> historyArrAdapter;
        historyArrAdapter = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, allEquations);
        list.setAdapter(historyArrAdapter);


    }
}