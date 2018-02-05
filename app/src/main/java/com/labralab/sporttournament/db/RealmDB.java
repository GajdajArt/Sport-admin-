package com.labralab.sporttournament.db;

import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.labralab.sporttournament.fragments.StartFragment;
import com.labralab.sporttournament.models.Tournament;
import com.labralab.sporttournament.models.simple_models.BasePOJO;
import com.labralab.sporttournament.models.simple_models.SimpleTournament;
import com.labralab.sporttournament.models.simple_models.TournList;
import com.labralab.sporttournament.utils.TournamentUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pc on 23.12.2017.
 */

public class RealmDB implements TournamentRepository {

    Realm realm;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Retrofit retrofit;
    Api api;

    TournList tournList;


    public RealmDB() {

        tournList = new TournList();

        realm = Realm.getDefaultInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("tournaments");

        retrofit = new Retrofit.Builder()
                .baseUrl("https://sporttournament-4679a.firebaseio.com")//url of firebase app
                .addConverterFactory(GsonConverterFactory.create())//use for convert JSON file into object
                .build();

        api = retrofit.create(Api.class);

    }

    @Override
    public void createTournament(final Tournament tournament) {


        addToRealm(tournament);

        //copy to firebase
        SimpleTournament simpleTournament = TournamentUtil.tournToSimpleTourn(tournament);
        tournList.addTournament(simpleTournament);
        readRealmDB();
        myRef.setValue(tournList);

    }


    @Override
    public List<Tournament> readDBTourn(final StartFragment startFragment) {

        final List<Tournament> tournFromRealm = new ArrayList<>();
        RealmResults<Tournament> results = realm.where(Tournament.class).findAll();
        tournFromRealm.addAll(results);
        startFragment.setData(tournFromRealm);

        final List<Tournament> fBData = new ArrayList<>();

        Call<BasePOJO> call = api.detData();

        call.enqueue(new Callback<BasePOJO>() {
            @Override
            public void onResponse(Call<BasePOJO> call, Response<BasePOJO> response) {
                delAllFromRealm();
                if (response.body() != null) {
                    tournList = response.body().tournaments;

                    for (SimpleTournament st : tournList.getList()) {
                        fBData.add(TournamentUtil.simpleTournToHard(st));
                        addToRealm(TournamentUtil.simpleTournToHard(st));
                    }
                }
                startFragment.setData(fBData);
                startFragment.hideRefreshing();
            }

            @Override
            public void onFailure(Call<BasePOJO> call, Throwable t) {
                Toast.makeText(startFragment.getContext()
                        ,"Связь с сервером не установлена"
                        , Toast.LENGTH_LONG).show();
            }
        });

        return tournFromRealm;
    }

    @Override
    public void delTournament(final String title, Boolean isPlayoff) {

        delFromRealm(title);
        readRealmDB();
        myRef.setValue(tournList);

    }

    public Tournament getTournament(String title) {

        Tournament tournament = realm.where(Tournament.class)
                .equalTo("title", title)
                .findFirst();
        return tournament;
    }


    public void delFromRealm(String title) {

        boolean flag = false;
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
            flag = true;
        }

        RealmResults<Tournament> results = realm.where(Tournament.class)
                .equalTo("title", title)
                .findAll();
        results.deleteFirstFromRealm();

        if (flag) {
            realm.commitTransaction();
        }
    }

    private void delAllFromRealm() {
        boolean flag = false;
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
            flag = true;
        }

        RealmResults<Tournament> results = realm.where(Tournament.class)
                .findAll();
        results.deleteAllFromRealm();

        if (flag) {
            realm.commitTransaction();
        }
    }

    public void addToRealm(Tournament tournament) {

        //copy to Realm
        boolean flag = false;
        if (!realm.isInTransaction()) {
            realm.beginTransaction();
            flag = true;
        }

        realm.copyToRealm(tournament);


        if (flag) {
            realm.commitTransaction();
        }
    }

    public void readRealmDB() {

        RealmResults<Tournament> results = realm.where(Tournament.class).findAll();
        List<SimpleTournament> items = new ArrayList<>();
        for (Tournament tournament : results) {
            items.add(TournamentUtil.tournToSimpleTourn(tournament));
        }
        tournList.setList(items);
    }

}