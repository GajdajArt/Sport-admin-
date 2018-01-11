package com.labralab.sporttournament.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.labralab.sporttournament.dialogs.QuickActionDialog;
import com.labralab.sporttournament.R;
import com.labralab.sporttournament.models.Team;

import java.util.ArrayList;
import java.util.List;


public class TeamAdapter extends RecyclerView.Adapter<TeamRecyclerViewHolder> {

    List<Team> items = new ArrayList<>();

    public TeamAdapter(List<Team> items) {
        this.items = items;
    }

    @Override
    public TeamRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_maket, parent, false);
        return new TeamRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TeamRecyclerViewHolder holder, int position) {
        Team item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.position.setText(Integer.toString(position + 1));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

class TeamRecyclerViewHolder extends RecyclerView.ViewHolder implements
 View.OnLongClickListener, View.OnClickListener  {

    public static final int TOURN_ADAPTER_ID = 2;
    TextView title;
    TextView position;
    View itemView;

    public TeamRecyclerViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        itemView.setOnClickListener(this);


        position = (TextView) itemView.findViewById(R.id.team_position);
        title = (TextView) itemView.findViewById(R.id.team_title);

    }


    @Override
    public boolean onLongClick(View view) {

        return true;
    }

    @Override
    public void onClick(View view) {

        QuickActionDialog quickActionDialog = new QuickActionDialog(position,
                TOURN_ADAPTER_ID,
                title.getText().toString());
        quickActionDialog.onActionStart();
    }
}

