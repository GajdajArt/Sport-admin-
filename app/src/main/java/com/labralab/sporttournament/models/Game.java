package com.labralab.sporttournament.models;


import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

import io.realm.RealmObject;

@IgnoreExtraProperties
public class Game extends RealmObject implements Serializable {

    private int day;
    private int year;
    private int month;

    private Team team_1;
    private Team team_2;
    private int score_1;
    private int score_2;


    public Game(Team team_1, Team team_2, int score_1, int score_2, int day, int month, int year) {
        this.team_1 = team_1;
        this.team_2 = team_2;
        this.score_1 = score_1;
        this.score_2 = score_2;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public Game(String team_1, String team_2, int score_1, int score_2) {

        this.score_1 = score_1;
        this.score_2 = score_2;
    }

    public Game(Team team_1, Team team_2) {

        this.team_1 = team_1;
        this.team_2 = team_2;
        this.score_1 = 0;
        this.score_2 = 0;
        this.day = 0;
        this.month = 0;
        this.year = 0;
    }

    public Game() {
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Team getTeam_1() {
        return team_1;
    }

    public void setTeam_1(Team team_1) {
        this.team_1 = team_1;
    }

    public Team getTeam_2() {
        return team_2;
    }

    public void setTeam_2(Team team_2) {
        this.team_2 = team_2;
    }

    public int getScore_1() {
        return score_1;
    }

    public void setScore_1(int score_1) {
        this.score_1 = score_1;
    }

    public int getScore_2() {
        return score_2;
    }

    public void setScore_2(int score_2) {
        this.score_2 = score_2;
    }

}
