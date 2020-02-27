package com.dzm.ffmpeg.http.base;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onError(BaseResponse.build(e.getMessage()));
    }

    @Override
    public void onComplete() {
    }


    public abstract void onSuccess(T t);

    public abstract void onError(BaseResponse error);

}
