package org.greenfroyo.androidmvp_bind.app._core;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenfroyo.mvp_bind.presenter.PresenterFactory;
import org.greenfroyo.mvp_bind.presenter.PresenterManager;
import org.greenfroyo.mvp_bind.util.AppUtil;
import org.greenfroyo.mvp_bind.view.MvpView;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by fchristysen on 6/30/16.
 */

public abstract class BaseFragment<P extends BasePresenter<VM>, VM extends BaseViewModel>
        extends Fragment
        implements MvpView<P, VM>, PresenterFactory<P> {

    private String TAG;
    protected final String WINDOW_HIERARCHY_TAG = "android:viewHierarchyState";
    protected final String WINDOW_VIEW_TAG = "android:views";

    private ViewDataBinding mBinding;
    private PresenterManager<P> mPresenterManager= new PresenterManager(this);

    @Override
    public abstract P createPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.TAG = this.getClass().getSimpleName();
        AppUtil.log(TAG + " : " + "onCreate");

        mPresenterManager.onRestoreInstanceState(savedInstanceState);
    }

    /** Initialize your ViewDataBinding, view initialization, and view model binding here
     *  @param viewModel the object to be bind into binding class
     */
    protected abstract ViewDataBinding onInitView(LayoutInflater inflater, VM viewModel);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = onInitView(inflater, getPresenter().getViewModel());
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            Bundle hierarchyState = savedInstanceState.getBundle(WINDOW_HIERARCHY_TAG);
            if(hierarchyState!=null){
                SparseArray screenState = hierarchyState.getSparseParcelableArray(WINDOW_VIEW_TAG);
                onRestoreViewState(screenState);
            }
        }
    }

    /**
     * This will be called if activity's savedInstanceState is not null
     * Some child view that's not attached yet will need viewState to call onRestoreViewState manually
     * @param viewState SparseArray containing all view state in current screen
     */
    protected void onRestoreViewState(@Nullable SparseArray<Parcelable> viewState){};

    @Override
    public void onResume() {
        super.onResume();
        AppUtil.log(TAG + " : " + "onAttachedView");
        mPresenterManager.onAttachedView(this);
        getPresenter().getViewModel().getEventBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        AppUtil.log(TAG + " : " + "onDetachedView");
        mPresenterManager.onDetachedView(getActivity().isFinishing());
        getPresenter().getViewModel().getEventBus().unregister(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenterManager.onSaveInstanceState(outState);
        AppUtil.log(TAG + " : " + "onSaveInstanceState");
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public final P getPresenter() {
        return mPresenterManager.getPresenter();
    }

    @Subscribe
    public void onSnackbarEvent(BaseViewModel.SnackbarEvent event){
        Toast.makeText(getActivity(), event.getMessage(), Toast.LENGTH_SHORT).show();
    }
}