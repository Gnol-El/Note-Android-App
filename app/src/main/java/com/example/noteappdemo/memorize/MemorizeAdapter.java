package com.example.noteappdemo.memorize;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteappdemo.R;
import com.example.noteappdemo.event.Event;
import com.example.noteappdemo.event.EventAdapter;

import java.util.List;

public class MemorizeAdapter extends RecyclerView.Adapter<MemorizeAdapter.MemorizeViewHolder>{
    private static MemorizeAdapter.iClickMemorize iClickMemorize;
    List<Memorize> memorizeList;
    Context context;

    public interface iClickMemorize{
        void delMemorize(Memorize memorize);
    }
    public MemorizeAdapter(MemorizeAdapter.iClickMemorize iClickMemorize) {
        MemorizeAdapter.iClickMemorize = iClickMemorize;
    }

    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public MemorizeAdapter(List<Memorize> list, Context context) {
        this.memorizeList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MemorizeAdapter.MemorizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_memorize, parent, false);
        return new MemorizeAdapter.MemorizeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MemorizeAdapter.MemorizeViewHolder holder, int position) {
        final Memorize m = memorizeList.get(position);
        if(m == null) return;

        holder.tv_title.setText(m.getTitle());
        holder.tv_content.setText(m.getContent());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setPosition(holder.getPosition());
                MemorizeAdapter.iClickMemorize.delMemorize(m);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return memorizeList.size();
    }

    public class MemorizeViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener{
        TextView tv_title;
        TextView tv_content;

        public MemorizeViewHolder(@NonNull View v) {
            super(v);
            tv_title = v.findViewById(R.id.title_memo);
            tv_content = v.findViewById(R.id.content_memo);
        }
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(Menu.NONE, R.id.edit_option, Menu.NONE, R.string.edit_option);
            contextMenu.add(Menu.NONE, R.id.delete_option, Menu.NONE, R.string.delete_option);
        }
    }

    @Override
    public void onViewRecycled(MemorizeAdapter.MemorizeViewHolder holder){
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }
}
