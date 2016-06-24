package org.greenfroyo.androidmvp_bind.app.intentparam.back;

import android.databinding.Bindable;

import org.greenfroyo.androidmvp_bind.app._core.BaseViewModel;

/**
 * Created by fchristysen on 6/7/16.
 */

public class IntentParamBackViewModel extends BaseViewModel{
    private int mValue;

    @Bindable
    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        this.mValue = value;
    }

}