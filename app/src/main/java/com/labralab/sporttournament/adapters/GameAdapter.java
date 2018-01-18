package com.labralab.sporttournament.adapters;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.labralab.sporttournament.dialogs.GameDialog;
import com.labralab.sporttournament.dialogs.QuickActionDialog;
import com.labralab.sporttournament.R;
import com.labralab.sporttournament.models.Game;

import java.util.ArrayList;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameViewHolder> {

    List<Game> items = new ArrayList<>();

    public GameAdapter(List<Game> items) {
        this.items = items;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_maket, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        Game item = items.get(position);
        holder.teamOneTV.setText(item.getTeam_1().getTitle());
        holder.teamTwoTV.setText(item.getTeam_2().getTitle());
        holder.scoreOneTV.setText(Integer.toString(item.getScore_1()));
        holder.scoreTwoTV.setText(Integer.toString(item.getScore_2()));
        String myDay = String.format("%02d", item.getDay());
        String myMonth = String.format("%02d", item.getMonth());
        holder.dateTV.setText(myDay + "." +
                myMonth + "." +
                item.getYear());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class GameViewHolder extends RecyclerView.ViewHolder implements
        View.OnLongClickListener, View.OnClickListener {

    public static final int TOURN_ADAPTER_ID = 3;
    TextView teamOneTV;
    TextView teamTwoTV;
    TextView scoreTwoTV;
    TextView scoreOneTV;
    TextView dateTV;

    View itemView;

    public GameViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        teamOneTV = (TextView) itemView.findViewById(R.id.show_team_one);
        teamTwoTV = (TextView) itemView.findViewById(R.id.show_team_two);
        scoreOneTV = (TextView) itemView.findViewById(R.id.score1);
        scoreTwoTV = (TextView) itemView.findViewById(R.id.score2);
        dateTV = (TextView) itemView.findViewById(R.id.t_v_date);
    }


    @Override
    public boolean onLongClick(View view) {

        DialogFragment editGameDialog = new GameDialog();
        Bundle bundle = new Bundle();
        bundle.putString("teamOne", teamOneTV.getText().toString());
        bundle.putString("teamTwo", teamTwoTV.getText().toString());
        bundle.putString("scoreTwo", scoreTwoTV.getText().toString());
        bundle.putString("scoreOne", scoreOneTV.getText().toString());
        editGameDialog.setArguments(bundle);

        QuickActionDialog quickActionDialog = new QuickActionDialog(itemView,
                TOURN_ADAPTER_ID,
                editGameDialog, bundle);
        quickActionDialog.onActionStart();
        return true;
    }

    @Override
    public void onClick(View view) {


        DialogFragment editGameDialog = new GameDialog();
        Bundle bundle = new Bundle();
        bundle.putString("teamOne", teamOneTV.getText().toString());
        bundle.putString("teamTwo", teamTwoTV.getText().toString());
        bundle.putString("scoreTwo", scoreTwoTV.getText().toString());
        bundle.putString("scoreOne", scoreOneTV.getText().toString());
        editGameDialog.setArguments(bundle);

        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        editGameDialog.show(fragmentManager, "Tag");

    }
}