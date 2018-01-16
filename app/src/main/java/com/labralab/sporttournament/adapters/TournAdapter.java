package com.labralab.sporttournament.adapters;

import android.content.Context;
import android.content.Intent;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.labralab.sporttournament.dialogs.QuickActionDialog;
import com.labralab.sporttournament.R;
import com.labralab.sporttournament.TeamActivity;
import com.labralab.sporttournament.models.Tournament;

import java.util.ArrayList;
import java.util.List;


public class TournAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    public static List<Tournament> items = new ArrayList<>();


    public TournAdapter(List<Tournament> items) {
        this.items = items;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tourn_maket, parent, false);

        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Tournament item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getYearOfTourn());
        holder.type.setText(item.getType());

        if(item.getType().equals("Футбол")){
            holder.tournImage.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(R.drawable.f1f));
        } else {
            holder.tournImage.setImageDrawable(holder.itemView.getContext().getResources().getDrawable(R.drawable.b1b));
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData (List<Tournament> items){
        this.items = items;
        this.notifyDataSetChanged();
    }

}

class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

    public static final int TOURN_ADAPTER_ID = 1;

    View itemView;
    TextView title;
    TextView date;
    TextView type;
    ImageView tournImage;


    public RecyclerViewHolder(View itemView) {
        super(itemView);

        itemView.setOnLongClickListener(this);
        itemView.setOnClickListener(this);
        this.itemView = itemView;
        title = (TextView) itemView.findViewById(R.id.title);
        date = (TextView) itemView.findViewById(R.id.date);
        type = (TextView) itemView.findViewById(R.id.type);
        tournImage = (ImageView) itemView.findViewById(R.id.tournImage);


    }

    //Обработка нажатия
    @Override
    public void onClick(View view) {

        //Запускаем TeamActivity с заданным турниром
        Context context = view.getContext();
        Intent intent = new Intent(context, TeamActivity.class);
        String tableName = title.getText().toString();
        intent.putExtra("title", tableName);
        view.getContext().startActivity(intent);

    }

    //Обработка длинного нажатия
    @Override
    public boolean onLongClick(View view) {

        //Вызываем диалог изменения - удаления
        QuickActionDialog quickActionDialog = new QuickActionDialog(tournImage,
                TOURN_ADAPTER_ID, title.getText().toString());
        quickActionDialog.onActionStart();
        return true;
    }
}
