package io.github.jklingsporn.vertx.jooq.mutiny.jdbc;

import io.github.jklingsporn.vertx.jooq.mutiny.MutinyQueryExecutor;
import io.github.jklingsporn.vertx.jooq.shared.internal.AbstractQueryExecutor;
import io.github.jklingsporn.vertx.jooq.shared.internal.QueryResult;
import io.github.jklingsporn.vertx.jooq.shared.internal.jdbc.JDBCQueryExecutor;
import io.github.jklingsporn.vertx.jooq.shared.internal.jdbc.JDBCQueryResult;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.Vertx;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * Created by jensklingsporn on 05.02.18.
 */
public class JDBCMutinyGenericQueryExecutor extends AbstractQueryExecutor implements JDBCQueryExecutor<Uni<?>>, MutinyQueryExecutor {

    protected final Vertx vertx;

    public JDBCMutinyGenericQueryExecutor(Configuration configuration, Vertx vertx) {
        super(configuration);
        this.vertx = vertx;
    }

    @Override
    public <X> Uni<X> executeAny(Function<DSLContext, X> function){
        return executeBlocking(() -> function.apply(DSL.using(configuration())));
    }

    <X> Uni<X> executeBlocking(Callable<X> blockingCallable) {
        return vertx.executeBlocking(blockingCallable, false);
    }

    @Override
    public Uni<Integer> execute(Function<DSLContext, ? extends Query> queryFunction) {
        return executeBlocking(() -> createQuery(queryFunction).execute());
    }

    @Override
    public <R extends Record> Uni<QueryResult> query(Function<DSLContext, ? extends ResultQuery<R>> queryFunction) {
        return executeBlocking(() -> new JDBCQueryResult(createQuery(queryFunction).fetch()));
    }
}
