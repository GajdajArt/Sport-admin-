package com.labralab.sporttournament.models.simple_models;

import com.labralab.sporttournament.db.TournamentRepository;
import com.labralab.sporttournament.models.Game;
import com.labralab.sporttournament.models.Team;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.annotations.Ignore;

/**
 * Created by pc on 25.12.2017.
 */

public class SimplePlayoff {


    private String playoffTitle;

    private int countGames = -1;
    private int teamInPlayoff;



    private boolean isTeamsSort;

    private List<Game> playoffGameList = new ArrayList<>();
    private List<Team> playoffTeamList = new ArrayList<>();

    private List<Game> playoffFirstTurGames = new ArrayList<>();
    private List<Game> playoffSecondTurGames = new ArrayList<>();
    private List<Game> playoffLastTurGames = new ArrayList<>();

    public SimplePlayoff() {
    }

    public boolean getIsTeamsSort() {
        return isTeamsSort;
    }

    public void setTeamsSort(boolean teamsSort) {
        isTeamsSort = teamsSort;
    }

    public String getPlayoffTitle() {
        return playoffTitle;
    }

    public void setPlayoffTitle(String playoffTitle) {
        this.playoffTitle = playoffTitle;
    }

    public int getCountGames() {
        return countGames;
    }

    public void setCountGames(int countGames) {
        this.countGames = countGames;
    }

    public int getTeamInPlayoff() {
        return teamInPlayoff;
    }

    public void setTeamInPlayoff(int teamInPlayoff) {
        this.teamInPlayoff = teamInPlayoff;
    }

    public List<Game> getPlayoffGameList() {
        return playoffGameList;
    }

    public void setPlayoffGameList(List<Game> playoffGameList) {
        this.playoffGameList = playoffGameList;
    }

    public List<Team> getPlayoffTeamList() {
        return playoffTeamList;
    }

    public void setPlayoffTeamList(List<Team> playoffTeamList) {
        this.playoffTeamList = playoffTeamList;
    }

    public List<Game> getPlayoffFirstTurGames() {
        return playoffFirstTurGames;
    }

    public void setPlayoffFirstTurGames(List<Game> playoffFirstTurGames) {
        this.playoffFirstTurGames = playoffFirstTurGames;
    }

    public List<Game> getPlayoffSecondTurGames() {
        return playoffSecondTurGames;
    }

    public void setPlayoffSecondTurGames(List<Game> playoffSecondTurGames) {
        this.playoffSecondTurGames = playoffSecondTurGames;
    }

    public List<Game> getPlayoffLastTurGames() {
        return playoffLastTurGames;
    }

    public void setPlayoffLastTurGames(List<Game> playoffLastTurGames) {
        this.playoffLastTurGames = playoffLastTurGames;
    }
}
