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

import java.util.List;

public class EventActivity extends AppCompatActivity {
    String title = "", date = "", hour = "", minute = "", content = "";
    String time = "";
    EventHandler dh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        dh = new EventHandler(this);

        CalendarView cv = findViewById(R.id.calendarView3);
        cv.setScaleX(0.9f);
        cv.setScaleY(0.9f);
        TextView tv1 = findViewById(R.id.textView9);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                month++;
                date = day+"/"+month+"/"+year;
                tv1.setText(date);
            }
        });

        Spinner sp1 = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.hour_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter1);
        sp1.setSelection(7);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hour = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        Spinner sp2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.minute_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter2);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minute = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        EditText ed1 = findViewById(R.id.editTextTitle);
        EditText ed2 = findViewById(R.id.editTextContent);
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
                if(checkDuplicatedTime(dh)){
                    TextView tv2 = findViewById(R.id.textView7);
                    TextView tv3 = findViewById(R.id.textView8);
                    tv2.setTextColor(Color.RED);
                    tv3.setTextColor(Color.RED);
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.time_duplicated),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Event e = new Event(title, date, time, content);
                dh.addEvent(e);
                Intent i = new Intent(EventActivity.this, MainActivity.class);
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

    private boolean checkDuplicatedTime(EventHandler dh) {
        boolean check = false;
        List<Event> eventList = dh.getAllEventsByDate(date);
        for (Event e:
                eventList) {
            if (e.getTime().equals(time)) {
                check = true;
                break;
            }
        }
        return check;
    }
}