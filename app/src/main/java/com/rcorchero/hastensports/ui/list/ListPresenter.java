package com.rcorchero.hastensports.ui.list;

import android.support.annotation.NonNull;

import com.rcorchero.hastensports.data.DataManager;
import com.rcorchero.hastensports.data.model.PlayerList;
import com.rcorchero.hastensports.data.network.RemoteCallback;
import com.rcorchero.hastensports.ui.base.BasePresenter;

import java.util.List;

public class ListPresenter extends BasePresenter<ListContract.ListView> implements ListContract.ViewActions {

    private final DataManager mDataManager;

    public ListPresenter(@NonNull DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void onInitialListRequested() {
        getPlayers();
    }

    private void getPlayers() {

        if (!isViewAttached()) return;
        mView.showMessageLayout(false);
        mView.showProgress();
        mDataManager.getPlayers(
                new RemoteCallback <List<PlayerList>>() {
                    @Override
                    public void onSuccess(List<PlayerList> response) {

                        if (!isViewAttached()) return;

                        mView.hideProgress();

                        if (response.isEmpty()) {

                            mView.showEmpty();
                            return;

                        }

                        mView.showPlayers(response);

                    }

                    @Override
                    public void onUnauthorized() {
                        if (!isViewAttached()) return;
                        mView.hideProgress();
                        mView.showUnauthorizedError();
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (!isViewAttached()) return;
                        mView.hideProgress();
                        mView.showError(throwable.getMessage());
                    }
                });
    }
}