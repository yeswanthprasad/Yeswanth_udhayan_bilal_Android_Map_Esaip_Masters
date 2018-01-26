package com.example.yeswanth.mapapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeswanth on 24/01/18.
 */

public class ListDataActivity extends AppCompatActivity {
    private static final String TAG = "ListDataActivity";
    DatabaseHelper mDatabaseHealper;
    private ListView mListView;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        mListView = (ListView) findViewById(R.id.listView);
        mDatabaseHealper = new DatabaseHelper(this);

        populateListView();
    }

    private void populateListView() {
        Log.d(TAG,"PopulateListView: Displying data in the ListView.");
        Cursor data = mDatabaseHealper.getData();
        ArrayList<String> ListData = new ArrayList<>();
        while (data.moveToNext()){
            ListData.add(data.getString(1));

        }
        final ListAdapter adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,ListData);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "On Item Click: You Clicked on: " + name);

                Cursor data = mDatabaseHealper.getItemID(name);
                int itemID = -1;
                while (data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){

                    Log.d(TAG, "On Item Click: The ID is- " +itemID);
                    Intent editScreanIntent = new Intent(ListDataActivity.this, EditDataActivity.class);
                    editScreanIntent.putExtra("id",itemID);
                    editScreanIntent.putExtra("name",name);
                    startActivity(editScreanIntent);
                }
                else {
                    toastMessage("No ID Associated with the Name");
                }
            }
        });
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
