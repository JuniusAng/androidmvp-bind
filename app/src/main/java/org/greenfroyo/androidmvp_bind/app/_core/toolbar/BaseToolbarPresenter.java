package org.greenfroyo.androidmvp_bind.app._core.toolbar;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import org.greenfroyo.androidmvp_bind.R;
import org.greenfroyo.androidmvp_bind.app._core.BaseDialog;
import org.greenfroyo.androidmvp_bind.app._core.BasePresenter;
import org.greenfroyo.androidmvp_bind.app.login.LoginDialog;
import org.greenfroyo.androidmvp_bind.provider.manager.DeviceInfoManager;
import org.greenfroyo.androidmvp_bind.provider.log.LogProvider;
import org.greenfroyo.androidmvp_bind.provider.user.UserProvider;

import java.util.Locale;

/**
 * Created by fchristysen on 7/20/16.
 */

public abstract class BaseToolbarPresenter<VM extends BaseToolbarViewModel>
        extends BasePresenter<VM>{

    private UserProvider mUserProvider;
    private LogProvider mLogProvider;
    private DeviceInfoManager mDeviceInfoManager;

    @Override
    public void onCreate(Bundle presenterState) {
        super.onCreate(presenterState);
        mUserProvider = new UserProvider();
        getViewModel().setLogin(mUserProvider.isLogin());
        mUserProvider.getIsLoginStream().subscribe(next -> {
            getViewModel().setLogin(next);
        });
        mLogProvider = new LogProvider();
        mDeviceInfoManager = new DeviceInfoManager();
        mDeviceInfoManager.getLocaleStream().subscribe(next -> {
            getViewModel().setToastMessage(this.getClass().getSimpleName() + " > Locale has been changed");
        });
    }

    public void showActivityCount(){
        getViewModel().setToastMessage("Current activity count is " + mLogProvider.getActivityCount()
                + "\n and stored count is " + mLogProvider.getStoredActivityCount());
    }

    public void increaseActivityCount(){
        mLogProvider.setActivityCount(mLogProvider.getActivityCount()+1);
        mLogProvider.setStoredActivityCount(mLogProvider.getStoredActivityCount()+1);
    }

    public void decreaseActivityCount(){
        mLogProvider.setActivityCount(mLogProvider.getActivityCount()-1);
        mLogProvider.setStoredActivityCount(mLogProvider.getStoredActivityCount()-1);
    }

    public void resetActivityCount(){
        mLogProvider.setActivityCount(1);
        mLogProvider.setStoredActivityCount(1);
    }

    public void switchLocale(){
        Locale locale = mDeviceInfoManager.getLocale();
        String lang;
        if(locale.getLanguage().equals("en")){
            lang = "id";
        }else{
            lang = "en";
        }
        mDeviceInfoManager.setLocale(new Locale(lang, locale.getCountry()));
    }

    public void openLoginDialog(Activity activity){
        final LoginDialog dialog = new LoginDialog(activity);
        dialog.setDialogListener(new BaseDialog.DialogListener() {
            @Override
            public void onComplete(Dialog dialog) {
                Log.d("login", "complete");
            }

            @Override
            public void onCancel(Dialog dialog) {
                Log.d("login", "cancel");
            }

            @Override
            public void onDismiss(Dialog dialog) {
                Log.d("login", "dismiss");
            }
        });
        dialog.show();
    }

    public void signOut(){
        mUserProvider.setLogin(false);
        getViewModel().setToastMessage(R.string.logout_success);
    }
}
