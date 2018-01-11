package com.labralab.sporttournament.db;



import com.labralab.sporttournament.models.simple_models.BasePOJO;
import com.labralab.sporttournament.models.simple_models.TournList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by pc on 06.01.2018.
 */

public interface Api {

    @GET("/.json")
    Call<BasePOJO> detData();
}
