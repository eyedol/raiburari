/*
 * Copyright (c) 2015 Henry Addo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.addhen.android.raiburari.domain.usecase;

import com.addhen.android.raiburari.domain.executor.PostExecutionThread;
import com.addhen.android.raiburari.domain.executor.ThreadExecutor;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * Base class to be extended by all Usecases
 *
 * @author Henry Addo
 */
public abstract class Usecase {

    private final ThreadExecutor mThreadExecutor;

    private final PostExecutionThread mPostExecutionThread;

    private CompositeDisposable mDisposable = new CompositeDisposable();

    protected Usecase(ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread) {
        this.mThreadExecutor = threadExecutor;
        this.mPostExecutionThread = postExecutionThread;
    }

    /**
     * Builds an {@link Flowable} which will be used when executing the current {@link
     * Usecase}.
     */
    protected abstract Observable buildUseCaseObservable();

    /**
     * Executes the current use case.
     *
     * @param usecaseDisposableObserver Usecase observable {@link #buildUseCaseObservable()}.
     */
    @SuppressWarnings("unchecked")
    public void execute(DisposableSubscriber usecaseDisposableObserver) {
        this.buildUseCaseObservable()
                .toFlowable(BackpressureStrategy.LATEST)
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutionThread.getScheduler())
                .subscribe(usecaseDisposableObserver);
        mDisposable.add(usecaseDisposableObserver);
    }

    /**
     * Unsubscribes from current {@link Disposable}.
     */
    public void unsubscribe() {
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        mDisposable.clear();
    }
}
