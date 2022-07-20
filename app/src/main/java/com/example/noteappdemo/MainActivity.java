package com.example.noteappdemo;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noteappdemo.event.Event;
import com.example.noteappdemo.event.EventAdapter;
import com.example.noteappdemo.event.EventHandler;
import com.example.noteappdemo.event.UpdateEventActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    EventHandler dh;
    List<Event> eventList;
    List<Event> sameDateList;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    DialogInterface.OnClickListener dialogListener;
    private EventAdapter eventAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    String date = "";
    int ev_id;

    Date currentTime;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/M/yyyy-hh:mm");
    String strTime;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!date.equals("")) setEventList();

        createNotificationChannel();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        CalendarView calendarView=(CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                month++;
                date = dayOfMonth+"/"+month+"/"+year;
                eventList = dh.getAllEventsByDate(date);
                TextView show = findViewById(R.id.textView2);
                if(eventList.isEmpty()){
                    show.setText(getResources().getString(R.string.no_event));
                }
                else{
                    eventList = dh.getAllEventsByDate(date);
                    show.setText("");
                }
                setEventList();
            }
        });

        dh = new EventHandler(this);
        eventAdapter = new EventAdapter(new EventAdapter.iClickEvent() {
            @Override
            public void delEvent(Event e) {
                ev_id = e.getId();
            }
        });

        Button bt1 = findViewById(R.id.button);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, OptionsActivity.class);
                startActivity(i);
            }
        });

        dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == DialogInterface.BUTTON_POSITIVE){
                    dh.deleteEvent(ev_id);
                    setEventList();
                }
            }
        };

        swipeRefreshLayout = findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        registerForContextMenu(recyclerView);

        new Thread(){
            public void run(){
                try{
                    while (!isInterrupted()){
                        Thread.sleep(1000);
                        currentTime = Calendar.getInstance().getTime();
                        strTime = formatter.format(currentTime);
                        String[] temp = strTime.split("-");

                        sameDateList = dh.getAllEventsByDate(temp[0]);
                        boolean check = false;
                        String title = "";
                        String content = "";
                        for (Event e:
                                sameDateList) {
                            if (e.getTime().equals(temp[1])) {
                                check = true;
                                title = e.getTitle();
                                content = e.getContent();
                                break;
                            }
                        }
                        if (check){
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "CHANNEL_ID")
                                    .setSmallIcon(R.drawable.notification)
                                    .setContentTitle(title)
                                    .setContentText(content)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setContentIntent(pendingIntent)
                                    .setAutoCancel(true)
                                    .setVisibility(NotificationCompat.VISIBILITY_PRIVATE);
                            notificationManagerCompat.notify(100, builder.build());
                        }
                    }
                } catch (InterruptedException ignored){
                }
            }
        }.start();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.expansion_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = -1;
        try {
            position = ((EventAdapter) recyclerView.getAdapter()).getPosition();
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }

        switch (item.getItemId()){
            case R.id.edit_option:
                Intent i = new Intent(this, UpdateEventActivity.class);
                i.putExtra("event_id", ev_id);
                startActivity(i);
                break;
            case R.id.delete_option:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.delete_confirm))
                        .setPositiveButton(getResources().getString(R.string.yes_option), dialogListener)
                        .setNegativeButton(getResources().getString(R.string.no_option), dialogListener)
                        .show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        setEventList();
    }
    private void setEventList(){
        eventList = dh.getAllEventsByDate(date);
        mAdapter = new EventAdapter(eventList, this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        setEventList();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2500);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "clgt";
            String description = "no";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}