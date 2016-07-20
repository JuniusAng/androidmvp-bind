package org.greenfroyo.androidmvp_bind.app._core.toolbar;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;

import org.greenfroyo.androidmvp_bind.R;
import org.greenfroyo.androidmvp_bind.app._core.BaseActivity;
import org.greenfroyo.androidmvp_bind.databinding.BaseToolbarActivityBinding;

/**
 * Created by fchristysen on 7/20/16.
 */

public abstract class BaseToolbarActivity<P extends BaseToolbarPresenter<VM>, VM extends BaseToolbarViewModel> extends BaseActivity<P, VM> {

    private BaseToolbarActivityBinding mToolbarBinding;

    public <T extends ViewDataBinding> T setBindView(int layoutId) {
        mToolbarBinding = super.setBindView(R.layout.base_toolbar_activity);
        setSupportActionBar(mToolbarBinding.toolbar);
        T binding = DataBindingUtil.inflate(getLayoutInflater(), layoutId, null, false);
        mToolbarBinding.toolbarContent.addView(binding.getRoot());
        return binding;
    }
}