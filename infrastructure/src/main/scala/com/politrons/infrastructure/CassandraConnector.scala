package com.politrons.infrastructure

import com.datastax.driver.core._
import com.github.nosan.embedded.cassandra.local.LocalCassandraFactory
import com.github.nosan.embedded.cassandra.{Cassandra, Settings}
import io.vavr.control.Try

/**
  * Simple cassandra client, following the datastax documentation
  * (http://www.datastax.com/documentation/developer/java-driver/2.0/java-driver/quick_start/qsSimpleClientCreate_t.html).
  */
object CassandraConnector {

  lazy val defaultTable =
    s"""CREATE TABLE IF NOT EXISTS paymentsSchema.payment (
          id uuid,
          timestamp varchar,
          event varchar,
          PRIMARY KEY (id )
        );"""

  var session: Session = _
  private var cluster: Cluster = _
  private var cassandra: Cassandra = _

  def isStarted(): Boolean = session != null && !session.isClosed

  def start(): Unit = {
    val cassandraFactory = new LocalCassandraFactory
    cassandra = cassandraFactory.create()
    cassandra.start()
    initCassandra()
  }

  def stop() {
    session.close()
    cluster.close()
  }

  def addPayment(query: String): Try[ResultSet] = executeQuery(query)

  def fetchPayment(query: String): Try[ResultSet] = executeQuery(query)


  /**
    * Init the session/cluster to cassandra nd create the keyspace and table in case does not exist.
    */
  def initCassandra(): Unit = {
    initCluster(cassandra.getSettings)
    cleanCassandra()
    createSchema()
    createTable()
  }

  private def initCluster(settings: Settings): Unit = {
    val socketOptions = new SocketOptions
    socketOptions.setConnectTimeoutMillis(30000)
    socketOptions.setReadTimeoutMillis(30000)
    cluster = Cluster.builder
      .addContactPoints(settings.getAddress)
      .withPort(settings.getPort)
      .withSocketOptions(socketOptions)
      .withoutJMXReporting
      .withoutMetrics
      .build
    session = cluster.connect
  }

  private def createSchema(keySpace: String = "paymentsSchema"): Unit = {
    session.execute(s"CREATE KEYSPACE IF NOT EXISTS $keySpace WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};")
    println(s"Keyspace $keySpace created if not exists")
  }

  private def createTable(script: String = defaultTable): Unit = {
    session.execute(script)
    println(s"Table created if not exist")
  }

  /**
    * Clean the table data from the test
    */
  private def cleanCassandra(keySpace: String = "paymentsSchema", table: String = "payment"): Unit = {
    executeQuery(s"""truncate  $keySpace.$table""")
  }

  private def executeQuery(query: String): Try[ResultSet] = {
    Try.of(() => session.execute(query))
  }
}
