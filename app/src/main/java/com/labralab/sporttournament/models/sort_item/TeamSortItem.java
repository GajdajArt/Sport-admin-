package com.labralab.sporttournament.models.sort_item;

/**
 * Created by pc on 22.01.2018.
 */

public class TeamSortItem {

    public static final int SEPARATOR_ID = 0;
    public static final int TEAM_ID = 1;
    private int id;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
