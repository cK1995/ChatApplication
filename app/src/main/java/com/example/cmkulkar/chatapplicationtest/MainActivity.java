package com.example.cmkulkar.chatapplicationtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    TextView textView;
    ArrayAdapter<String> chatNames;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef;
    public static String chatPersonName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.chatList);
        textView = (TextView) findViewById(R.id.textView);
        chatNames = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        dbRef = database.getReference("Users");
        listview.setAdapter(chatNames);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectUserNames((Map<String,Object>) dataSnapshot.getValue());
                Toast.makeText(getApplicationContext(),"Inside Cloud Function",Toast.LENGTH_SHORT);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                chatPersonName = chatNames.getItem(position);
                Intent intent = new Intent(arg0.getContext(),ChatScreen.class);
                startActivity(intent);
            }
        });

    }

    public void collectUserNames(Map<String, Object> users) {
        chatNames.clear();
        for (Map.Entry<String, Object> entry : users.entrySet()) {
            chatNames.add(entry.getKey().toString());
            textView.setText(entry.getKey().toString());
        }
    }
}
