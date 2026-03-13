package io.github.jklingsporn.vertx.jooq.classic.jdbc;

import io.github.jklingsporn.vertx.jooq.classic.ClassicQueryExecutor;
import io.github.jklingsporn.vertx.jooq.shared.internal.AbstractQueryExecutor;
import io.github.jklingsporn.vertx.jooq.shared.internal.QueryResult;
import io.github.jklingsporn.vertx.jooq.shared.internal.jdbc.JDBCQueryResult;
import io.github.jklingsporn.vertx.jooq.shared.internal.jdbc.JDBCQueryExecutor;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;

import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * Created by jensklingsporn on 05.02.18.
 */
public class JDBCClassicGenericQueryExecutor extends AbstractQueryExecutor implements JDBCQueryExecutor<Future<?>>, ClassicQueryExecutor{

    protected final Vertx vertx;

    public JDBCClassicGenericQueryExecutor(Configuration configuration, Vertx vertx) {
        super(configuration);
        this.vertx = vertx;
    }

    @Override
    public <X> Future<X> executeAny(Function<DSLContext, X> function){
        return executeBlocking(() -> function.apply(DSL.using(configuration())));
    }

    protected <X> Future<X> executeBlocking(Callable<X> blockingCallable){
        return vertx.executeBlocking(blockingCallable, false);
    }

    @Override
    public Future<Integer> execute(Function<DSLContext, ? extends Query> queryFunction) {
        return executeBlocking(() -> createQuery(queryFunction).execute());
    }

    @Override
    public <R extends Record> Future<QueryResult> query(Function<DSLContext, ? extends ResultQuery<R>> queryFunction) {
        return executeBlocking(() -> new JDBCQueryResult(createQuery(queryFunction).fetch()));
    }
}
