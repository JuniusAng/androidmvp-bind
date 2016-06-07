package org.greenfroyo.androidmvp_bind.app.home;

import android.os.Bundle;
import android.support.annotation.Nullable;

import org.greenfroyo.androidmvp_bind.app._core.BasePresenter;
import org.greenfroyo.androidmvp_bind.domain.Home;
import org.greenfroyo.androidmvp_bind.provider.HomeProvider;

/**
 * Created by fchristysen on 6/7/16.
 */

public class HomePresenter extends BasePresenter {

    //Provider
    private HomeProvider mHomeProvider;

    @Override
    public void onCreate(@Nullable Bundle presenterState) {
        super.onCreate(presenterState);
    }

    @Override
    protected void onViewAttached() {
        super.onViewAttached();
        mHomeProvider = new HomeProvider(getView().getContext());

        if(Home.isAllowedToShow()){
        }
    }

    @Override
    protected void onViewDetached() {
        super.onViewDetached();
    }

    @Override
    public void onSaveInstanceState(Bundle outPresenterState) {
        super.onSaveInstanceState(outPresenterState);
    }
}