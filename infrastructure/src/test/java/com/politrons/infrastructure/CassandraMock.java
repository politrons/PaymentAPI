package com.politrons.infrastructure;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.github.nosan.embedded.cassandra.Cassandra;
import com.github.nosan.embedded.cassandra.Settings;
import com.github.nosan.embedded.cassandra.cql.CqlScript;
import com.github.nosan.embedded.cassandra.local.LocalCassandraFactory;

import java.util.List;

public class CassandraMock {

    static private Cassandra cassandra;

    public static void start() {
        LocalCassandraFactory cassandraFactory = new LocalCassandraFactory();
        cassandra = cassandraFactory.create();
        cassandra.start();
        Settings settings = cassandra.getSettings();
        executeScripts(settings);
    }

    public static void stopCassandra(){
        cassandra.stop();
    }

    private static void executeScripts(Settings settings) {
        SocketOptions socketOptions = new SocketOptions();
        socketOptions.setConnectTimeoutMillis(30000);
        socketOptions.setReadTimeoutMillis(30000);
        try (Cluster cluster = Cluster.builder().addContactPoints(settings.getAddress())
                .withPort(settings.getPort()).withSocketOptions(socketOptions)
                .withoutJMXReporting().withoutMetrics()
                .build()) {
            Session session = cluster.connect();
//            List<String> statements = CqlScript.classpath("schema.cql").getStatements();
//            statements.forEach(session::execute);
        }
    }


}