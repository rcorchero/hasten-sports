package com.rcorchero.hastensports.data.network;

import com.rcorchero.hastensports.data.model.PlayerList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NetworkService {

    /**
     * Retrieve list of players
     */
    @GET("bins/{id}")
    Call<List<PlayerList>> getPlayers(@Path("id") int id);

}
