package com.example.noteappdemo.event;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteappdemo.R;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private static EventAdapter.iClickEvent iClickEvent;
    List<Event> eventList;
    Context context;

    public interface iClickEvent{
        void delEvent(Event event);
    }
    public EventAdapter(iClickEvent iClickEvent) {
        EventAdapter.iClickEvent = iClickEvent;
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public EventAdapter(List<Event> eventList, Context context) {
        this.eventList = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_event, parent, false);
        return new EventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Event e = eventList.get(position);
        if(e == null) return;

        holder.tv_time.setText(e.getTime());
        holder.tv_title.setText(e.getTitle());
        holder.tv_content.setText(e.getContent());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setPosition(holder.getPosition());
                EventAdapter.iClickEvent.delEvent(e);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener{
        TextView tv_time;
        TextView tv_title;
        TextView tv_content;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(Menu.NONE, R.id.edit_option, Menu.NONE, R.string.edit_option);
            contextMenu.add(Menu.NONE, R.id.delete_option, Menu.NONE, R.string.delete_option);
        }
    }

    @Override
    public void onViewRecycled(EventViewHolder holder){
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }
}
