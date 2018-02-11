package com.rcorchero.hastensports.data;

import com.rcorchero.hastensports.data.model.PlayerList;
import com.rcorchero.hastensports.data.network.NetworkService;
import com.rcorchero.hastensports.data.network.NetworkServiceFactory;
import com.rcorchero.hastensports.data.network.RemoteCallback;

import java.util.List;

public class DataManager {

    private static DataManager sInstance;

    private final NetworkService mNetworkService;

    public static DataManager getInstance() {

        if (sInstance == null) {

            sInstance = new DataManager();

        }

        return sInstance;

    }

    private DataManager() {

        mNetworkService = NetworkServiceFactory.makeNetworkService();

    }

    public void getPlayers(RemoteCallback<List<PlayerList>> listener) {

        mNetworkService.getPlayers(66851).enqueue(listener);

    }

}
