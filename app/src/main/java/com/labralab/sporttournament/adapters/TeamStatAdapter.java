package com.labralab.sporttournament.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.labralab.sporttournament.R;
import com.labralab.sporttournament.models.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TeamStatAdapter extends RecyclerView.Adapter<TeamStatsRecyclerViewHolder> {

    List<Team> items = new ArrayList<>();

    public TeamStatAdapter(Set<Team> items) {
        this.items.addAll(items);
        int i = items.size();

    }

    @Override
    public TeamStatsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_stat_maket, parent, false);
        return new TeamStatsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TeamStatsRecyclerViewHolder holder, int position) {
        Team item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.position.setText(Integer.toString(position + 1) + ".");
        holder.game.setText(Integer.toString(item.getGames()));
        holder.gameLost.setText(Integer.toString(item.getGames_lost()));
        holder.gameWon.setText(Integer.toString(item.getGames_won()));
        holder.points.setText(Integer.toString(item.getPoints()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class TeamStatsRecyclerViewHolder extends RecyclerView.ViewHolder   {

    TextView title;
    TextView position;
    TextView game;
    TextView points;
    TextView gameWon;
    TextView gameLost;

    View itemView;

    public TeamStatsRecyclerViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;

        position = (TextView) itemView.findViewById(R.id.team_stat_position);
        title = (TextView) itemView.findViewById(R.id.team_stat_title);
        game = (TextView) itemView.findViewById(R.id.team_stat_game);
        points = (TextView) itemView.findViewById(R.id.team_stat_points);
        gameWon = (TextView) itemView.findViewById(R.id.team_stat_game_won);
        gameLost = (TextView) itemView.findViewById(R.id.team_stat_game_lost);

    }
}
