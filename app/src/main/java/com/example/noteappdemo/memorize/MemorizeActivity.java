package com.example.noteappdemo.memorize;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.noteappdemo.OptionsActivity;
import com.example.noteappdemo.R;

import java.util.List;

public class MemorizeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    MemorizeHandler dh;
    List<Memorize> memorizeList;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MemorizeAdapter memorizeAdapter;

    DialogInterface.OnClickListener dialogListener;
    int m_id;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorize);

        Button bt = findViewById(R.id.button7);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MemorizeActivity.this, CreateMemorizeActivity.class);
                startActivity(i);
            }
        });

        memorizeAdapter = new MemorizeAdapter(new MemorizeAdapter.iClickMemorize() {
            @Override
            public void delMemorize(Memorize memorize) {
                m_id = memorize.getId();
            }
        });
        dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == DialogInterface.BUTTON_POSITIVE){
                    dh.deleteMemorize(m_id);
                    setMemorizeList();
                }
            }
        };

        dh = new MemorizeHandler(this);

        recyclerView = findViewById(R.id.recyclerView2);
        recyclerView.hasFixedSize();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        registerForContextMenu(recyclerView);
        setMemorizeList();

        swipeRefreshLayout = findViewById(R.id.swipeContainer2);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.RED);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.expansion_menu, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_option:
                Intent i = new Intent(this, UpdateMemorizeActivity.class);
                i.putExtra("memo_id", m_id);
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

    private void setMemorizeList(){
        memorizeList = dh.getAllMemorize();
        memorizeAdapter = new MemorizeAdapter(memorizeList, this);
        recyclerView.setAdapter(memorizeAdapter);
    }

    @Override
    public void onRefresh() {
        setMemorizeList();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2500);
    }
}