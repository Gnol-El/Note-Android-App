package com.example.noteappdemo.event;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteappdemo.MainActivity;
import com.example.noteappdemo.R;

public class UpdateEventActivity extends AppCompatActivity {
    String title = "", date = "", hour = "", minute = "", content = "";
    String time = "";
    EventHandler dh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);

        dh = new EventHandler(this);
        int id = getIntent().getIntExtra("event_id", 0);
        Event e = dh.getEvent(id);

        EditText ed1 = findViewById(R.id.editTextTitle);
        title = e.getTitle();
        ed1.setText(title);

        TextView tv1 = findViewById(R.id.textView9);
        date = e.getDate();
        tv1.setText(date);
        CalendarView cv = findViewById(R.id.calendarView3);
        cv.setScaleX(0.9f);
        cv.setScaleY(0.9f);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                month++;
                date = day+"/"+month+"/"+year;
                tv1.setText(date);
            }
        });

        Spinner s1 = findViewById(R.id.spinner);
        Spinner s2 = findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.hour_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter1);
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hour = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.minute_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adapter2);
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minute = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        String[] time_temp = e.getTime().split(":");
        hour = time_temp[0];
        minute = time_temp[1];
        s1.setSelection(Integer.parseInt(hour));
        s2.setSelection(Integer.parseInt(minute));

        EditText ed2 = findViewById(R.id.editTextContent);
        content = e.getContent();
        ed2.setText(content);

        Button bt1 = findViewById(R.id.button5);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = ed1.getText().toString();
                content = ed2.getText().toString();
                time = hour + ":" + minute;
                if(title.equals("")){
                    ed1.setHintTextColor(Color.RED);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.title_alert),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(date.equals("")){
                    tv1.setHintTextColor(Color.RED);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.date_alert),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                e.setTitle(title);
                e.setDate(date);
                e.setTime(time);
                e.setContent(content);
                dh.updateEvent(e);
                Intent i = new Intent(UpdateEventActivity.this, MainActivity.class);
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