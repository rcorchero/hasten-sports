package com.rcorchero.hastensports.ui.list;

import com.rcorchero.hastensports.data.model.PlayerList;
import com.rcorchero.hastensports.ui.base.RemoteView;

import java.util.List;

public interface ListContract {

    interface ViewActions {

        void onInitialListRequested();

    }

    interface ListView extends RemoteView {

        void showPlayers(List<PlayerList> playerList);

    }
}
