package com.labralab.sporttournament.models.simple_models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by pc on 07.01.2018.
 */

public class BasePOJO {

    @SerializedName("tournaments")
    @Expose
    public TournList tournaments;
}
