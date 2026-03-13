package io.github.jklingsporn.vertx.jooq.generate;

import io.vertx.core.Vertx;
import io.vertx.pgclient.PgBuilder;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;

/**
 * Created by jensklingsporn on 15.02.18.
 */
public class ReactiveDatabaseClientProvider {

    public static int POOL_SIZE = 8;
    private static ReactiveDatabaseClientProvider INSTANCE;
    public static ReactiveDatabaseClientProvider getInstance() {
        return INSTANCE == null ? INSTANCE = new ReactiveDatabaseClientProvider() : INSTANCE;
    }

    private final Vertx vertx;
    private final Pool pgClient;
    private final io.vertx.reactivex.sqlclient.SqlClient rxPgClient;
    private final io.vertx.rxjava3.sqlclient.SqlClient rx3PgClient;
    private final io.vertx.mutiny.sqlclient.SqlClient mutinyClient;

    private ReactiveDatabaseClientProvider() {
        this.vertx = Vertx.vertx();
        this.pgClient = PgBuilder.pool().connectingTo(getOptions()).with(new PoolOptions().setMaxSize(POOL_SIZE)).using(vertx).build();
        this.rxPgClient = new io.vertx.reactivex.sqlclient.Pool(PgBuilder.pool().connectingTo(getOptions()).with(new PoolOptions().setMaxSize(POOL_SIZE)).using(vertx).build());
        this.rx3PgClient = new io.vertx.rxjava3.sqlclient.Pool(PgBuilder.pool().connectingTo(getOptions()).with(new PoolOptions().setMaxSize(POOL_SIZE)).using(vertx).build());
        this.mutinyClient = new io.vertx.mutiny.sqlclient.Pool(PgBuilder.pool().connectingTo(getOptions()).with(new PoolOptions().setMaxSize(POOL_SIZE)).using(vertx).build());
    }

    public SqlClient getClient() {
        return pgClient;
    }

    private PgConnectOptions getOptions() {
        return new PgConnectOptions().setHost("127.0.0.1")
                .setPort(5432)
                .setUser(Credentials.POSTGRES.getUser())
                .setDatabase("postgres")
                .setPassword(Credentials.POSTGRES.getPassword())
                ;
    }

    public io.vertx.reactivex.sqlclient.SqlClient rxGetClient() {
        return rxPgClient;
    }

    public io.vertx.rxjava3.sqlclient.SqlClient rx3GetClient() {
        return rx3PgClient;
    }

    public io.vertx.mutiny.sqlclient.SqlClient mutinyGetClient() {
        return mutinyClient;
    }

    public Vertx getVertx() {
        return vertx;
    }

}
