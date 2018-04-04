package com.example.cmkulkar.chatapplicationtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
    Button logoutButton;
    ArrayAdapter<String> chatNames;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef;
    public static String chatPersonName = "";
    public String loggedInAs;
    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.chatList);
        textView = (TextView) findViewById(R.id.textView);
        logoutButton = (Button) findViewById(R.id.btnLogout);
        chatNames = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        dbRef = database.getReference("Users");
        listview.setAdapter(chatNames);
        sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        spEditor = sp.edit();
        loggedInAs = sp.getString("loggedInAs","");

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

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spEditor.putBoolean("isUserLoggedIn",false);
                spEditor.commit();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void collectUserNames(Map<String, Object> users) {
        chatNames.clear();
        for (Map.Entry<String, Object> entry : users.entrySet()) {
            if (! entry.getKey().toString().equals(loggedInAs)) {
                chatNames.add(entry.getKey().toString());
            }
        }
    }
}
