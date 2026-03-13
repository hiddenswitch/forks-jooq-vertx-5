package io.github.jklingsporn.vertx.jooq.rx.jdbc;

import io.vertx.reactivex.core.Vertx;
import io.reactivex.Observable;
import io.reactivex.Single;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class RXTool {
    private RXTool() {
    }


    public static <T> Single<T> executeBlocking(Callable<T> blockingCallable, Vertx
        vertx) {
        return vertx.rxExecuteBlocking(blockingCallable,false).toSingle();
    }

    public static <T> Observable<T> executeBlockingObservable(Callable<List<T>> blockingCallable, Vertx
        vertx) {
        return executeBlocking(blockingCallable,vertx)
                .flatMapObservable(Observable::fromIterable);
    }



    public static <T> Single<T> failure(Throwable e) {
        return Single.error(e);
    }


}
