package com.elena.fgosy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ActivityOtchet extends AppCompatActivity {

    ArrayAdapter<MyObject> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otchet);

         ArrayList<MyObject> myObjects = (ArrayList<MyObject>) getIntent().getSerializableExtra("MyClass");
        ListView lvSearch = (ListView) findViewById(R.id.listView_search);
        adapter = new MyObjectAdapter(this,R.layout.list_view_item, myObjects);
        lvSearch.setAdapter(adapter);
    }
}