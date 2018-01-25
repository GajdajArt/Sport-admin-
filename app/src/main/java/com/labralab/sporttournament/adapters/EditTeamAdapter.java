package com.labralab.sporttournament.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.labralab.sporttournament.R;
import com.labralab.sporttournament.dialogs.DateDialog;
import com.labralab.sporttournament.models.Team;
import com.labralab.sporttournament.models.sort_item.TeamSortItem;
import com.labralab.sporttournament.utils.TournamentUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pc on 21.01.2018.
 */

public class EditTeamAdapter extends RecyclerView.Adapter<EditTeamViewHolder> {

    List<Team> teams = new ArrayList<>();
    List<TeamSortItem> simpleTeams = new ArrayList<>();

    public EditTeamAdapter(List<Team> items) {
        this.teams = items;
        setContent();

    }

    @Override
    public int getItemViewType(int position) {
        if (simpleTeams.get(position).getId() == TeamSortItem.SEPARATOR_ID) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public EditTeamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_team_position_in_pf_separator, parent, false);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_team_position_in_pf_maket, parent, false);
                break;
        }

        return new EditTeamViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(EditTeamViewHolder holder, int position) {

        final int pos = position;

        holder.title.setText(simpleTeams.get(position).getTitle());

        if (simpleTeams.get(position).getId() == TeamSortItem.TEAM_ID) {

            holder.upBtton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int oldPos = TournamentUtil.getTeam(teams, simpleTeams.get(pos).getTitle());

                    if (oldPos != 0) {
                        Collections.swap(teams, oldPos, oldPos - 1);
                        simpleTeams.clear();
                        setContent();
                        EditTeamAdapter.this.notifyDataSetChanged();
                    }

                }
            });

            holder.downButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int oldPos = TournamentUtil.getTeam(teams, simpleTeams.get(pos).getTitle());

                    if (oldPos != teams.size() - 1){
                        Collections.swap(teams, oldPos, oldPos + 1);
                        simpleTeams.clear();
                        setContent();
                        EditTeamAdapter.this.notifyDataSetChanged();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return simpleTeams.size();
    }

    private void setContent() {
        int i = 0;
        TeamSortItem firstSeparator = new TeamSortItem();
        firstSeparator.setTitle("Игра №1");
        firstSeparator.setId(TeamSortItem.SEPARATOR_ID);
        simpleTeams.add(firstSeparator);

        for (Team team : teams) {
            i++;
            if (i == 3 || i == 5 || i == 7) {

                TeamSortItem separator = new TeamSortItem();
                separator.setTitle("Игра №2");
                separator.setId(TeamSortItem.SEPARATOR_ID);
                simpleTeams.add(separator);
            }
            simpleTeams.add(TournamentUtil.hardTeamToSort(team));
        }
    }

    public List<Team> getTeams(){
        return teams;
    }
}

class EditTeamViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    ImageButton upBtton;
    ImageButton downButton;
    View itemView;


    public EditTeamViewHolder(View itemView, int viewType) {

        super(itemView);
        this.itemView = itemView;


        switch (viewType) {
            case 0:
                title = (TextView) itemView.findViewById(R.id.separator);
                break;
            case 1:
                title = (TextView) itemView.findViewById(R.id.team_sort_title);
                upBtton = (ImageButton) itemView.findViewById(R.id.button_up);
                downButton = (ImageButton) itemView.findViewById(R.id.button_down);
                break;
        }
    }
}