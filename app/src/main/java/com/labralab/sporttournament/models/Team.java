package com.labralab.sporttournament.models;


import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

import io.realm.RealmObject;


@IgnoreExtraProperties
public class Team extends RealmObject implements Comparable<Team>, Serializable {


    private String title;
    private int points;
    private int games;
    private int games_won;
    private int games_lost;


    public Team(String title) {
        this.title = title;
        this.points = 0;
        this.games = 0;
        this.games_won = 0;
        this.games_lost = 0;
    }

    public Team() {

    }

    //Getters and Setters
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getGames_won() {
        return games_won;
    }

    public void setGames_won(int games_won) {
        this.games_won = games_won;
    }

    public int getGames_lost() {
        return games_lost;
    }

    public void setGames_lost(int games_lost) {
        this.games_lost = games_lost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //Methods for edit params
    public void plusGame() {
        this.games++;
    }

    public void minusGame() {
        this.games--;
    }

    public void plusPoints() {
        this.points++;
    }

    public void minusPoints() {
        this.points--;
    }

    public void plusGameLost() {
        this.games_lost++;
    }

    public void minusGameLost() {
        this.games_lost--;
    }

    public void plusGameWon() {
        this.games_won++;
    }

    public void minusGameWon() {
        this.games_won--;
    }

    //Method for compare
    @Override
    public int compareTo(Team team) {
        int pointsSort = team.points - this.points;
        int gameSort = this.games - team.games;
        if (pointsSort == 0) {
            pointsSort = 1;
        }
        return pointsSort;
    }
}

