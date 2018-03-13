package com.zhoutianchu.framework.fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jakewharton.rxbinding2.view.RxView;
import com.zhoutianchu.framework.activity.BaseActivity;
import com.zhoutianchu.framework.bean.base.HttpResponse;
import com.zhoutianchu.framework.bean.base.Message;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.concurrent.TimeUnit;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhoutianchu on 2017/4/12.
 */

public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;

    /**
     * 网络请求返回后的前置过滤器
     *
     * @param <T>
     * @return
     */
    protected <T extends HttpResponse> ObservableTransformer<T, T> resp_filter() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io()).onErrorResumeNext(
                        err -> {
                            return Observable.just((T) new HttpResponse<>(HttpResponse.NOT_FOUND, err.getMessage()));
                        }
                ).filter(resp->{
                    if(getActivity()==null)
                        return false;
                    return !getActivity().isFinishing();
                }).observeOn(AndroidSchedulers.mainThread()).filter(resp -> {
                    hideProgress();
                    if (!resp.isSuccess()) {
                        showToast(resp.getMsgInfo());
                        return false;
                    }
                    return true;
                }).observeOn(AndroidSchedulers.mainThread());
    }


    protected <T extends HttpResponse> ObservableTransformer<T, T> resp_filter_without_tips() {
        return upstream ->
                upstream.subscribeOn(Schedulers.io()).onErrorResumeNext(
                        err -> {
                            return Observable.just((T) new HttpResponse<>(HttpResponse.NOT_FOUND, err.getMessage()));
                        }
                ).filter(resp->{
                    if(getActivity()==null)
                        return false;
                    return !getActivity().isFinishing();
                }).observeOn(AndroidSchedulers.mainThread());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View view = setLayout(inflater, container, savedInstanceState);
        if (view != null)
            view.setOnTouchListener((v1, v2) -> true);
        unbinder = ButterKnife.bind(this, view);
        addAction();
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    protected void showProgress() {
        ((BaseActivity) getActivity()).showProgress();
    }

    protected void hideProgress() {
        ((BaseActivity) getActivity()).hideProgress();
    }

    protected void showToast(String msg) {
        ((BaseActivity) getActivity()).showToast(msg);
    }

    protected void showToast(int id) {
        ((BaseActivity) getActivity()).showToast(id);
    }

    protected abstract View setLayout(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState);

    protected abstract void addAction();

    protected abstract void initData();

    protected Observable throttleFirst(View view) {
        return RxView.clicks(view).throttleFirst(1000, TimeUnit.MILLISECONDS);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(Message msg) {

    }


}
