package com.example.noteappdemo.memorize;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.noteappdemo.R;

public class CreateMemorizeActivity extends AppCompatActivity {
    String title = "", content= "";
    MemorizeHandler dh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_memorize);

        dh = new MemorizeHandler(this);

        EditText ed1 = findViewById(R.id.editTextTitle2);
        EditText ed2 = findViewById(R.id.editTextContent2);
        Button bt1 = findViewById(R.id.button5);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = ed1.getText().toString();
                content = ed2.getText().toString();
                if(content.equals("")){
                    ed2.setHintTextColor(Color.RED);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.content_alert),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Memorize m = new Memorize(title, content);
                dh.addMemorize(m);
                Intent i = new Intent(CreateMemorizeActivity.this, MemorizeActivity.class);
                startActivity(i);
            }
        });
        Button bt2 = findViewById(R.id.button6);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}