package org.greenfroyo.androidmvp_bind.app._core;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.databinding.Observable;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import org.greenfroyo.mvp_bind.presenter.PresenterFactory;
import org.greenfroyo.mvp_bind.presenter.PresenterManager;
import org.greenfroyo.mvp_bind.view.MvpView;

/**
 * Created by fchristysen on 7/26/16.
 */

public abstract class BaseDialog<P extends BasePresenter<VM>, VM extends BaseViewModel>
        extends Dialog
        implements MvpView<P, VM>, PresenterFactory<P> {

    private String TAG = this.getClass().getSimpleName();
    protected final String WINDOW_HIERARCHY_TAG = "android:viewHierarchyState";
    protected final String WINDOW_VIEW_TAG = "android:views";

    private ViewDataBinding mBinding;
    private PresenterManager<P> mPresenterManager = new PresenterManager(this);
    private Observable.OnPropertyChangedCallback mPropertyChangedCallback;

    @Override
    public abstract P createPresenter();

    public BaseDialog(Activity activity) {
        super(activity);
        setOwnerActivity(activity);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenterManager.onRestoreInstanceState(savedInstanceState);
        mPropertyChangedCallback = getPropertyChangedCallback();

        mBinding = onInitView(getPresenter().getViewModel());
        onInitListener();
    }

    /**
     * Initialize your ViewDataBinding, view initialization, and view model binding here
     *
     * @param viewModel the object to be bind into binding class
     */
    protected abstract ViewDataBinding onInitView(VM viewModel);

    /**
     * Set listener for your view here
     */
    protected void onInitListener() {

    }

    public Observable.OnPropertyChangedCallback getPropertyChangedCallback() {
        return new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (i == BR.toastMessage) {
                    if (getViewModel().needToShowToast()) {
                        Toast.makeText(getOwnerActivity(), getViewModel().getToastMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Bundle hierarchyState = savedInstanceState.getBundle(WINDOW_HIERARCHY_TAG);
            if (hierarchyState != null) {
                SparseArray screenState = hierarchyState.getSparseParcelableArray(WINDOW_VIEW_TAG);
                onRestoreViewState(screenState);
            }
        }
    }

    /**
     * This will be called if there is previously saved savedInstanceState
     * Some child view may not be attached yet to its activity during onRestoreInstanceState
     * therefore the viewState must be passed manually.
     *
     * @param viewState SparseArray containing all view state in current screen
     */
    protected void onRestoreViewState(@Nullable SparseArray<Parcelable> viewState) {
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mPresenterManager.onAttachedView(this);
        getViewModel().addOnPropertyChangedCallback(mPropertyChangedCallback);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPresenterManager.onDetachedView(getOwnerActivity().isFinishing());
        getViewModel().removeOnPropertyChangedCallback(mPropertyChangedCallback);
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle outState = super.onSaveInstanceState();
        mPresenterManager.onSaveInstanceState(outState);
        return outState;
    }

    @Override
    public final P getPresenter() {
        return mPresenterManager.getPresenter();
    }

    public VM getViewModel() {
        return getPresenter().getViewModel();
    }

    public <T extends ViewDataBinding> T setBindView(int layoutId) {
        T viewDataBinding = DataBindingUtil.inflate(getLayoutInflater(), layoutId, null, false);
        setContentView(viewDataBinding.getRoot());
        return viewDataBinding;
    }
}
