package com.orientechnologies.spring.boot.daemon;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.spring.boot.OrientDBFactory;
import com.orientechnologies.spring.boot.ServerConfig;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Enrico Risa on 10/11/15.
 */

public class InsertDaemon implements Runnable {

  private OrientDBFactory factory;
  private ServerConfig    server;
  private AtomicLong      writeCounter;

  public InsertDaemon(OrientDBFactory factory, ServerConfig server, AtomicLong writeCounter) {
    this.factory = factory;
    this.server = server;
    this.writeCounter = writeCounter;
  }

  @Override
  public void run() {

    OrientGraphNoTx graphtNoTx = factory.getGraphtNoTx();

    try {
      while (true) {
        new ODocument("V").field("uuid", UUID.randomUUID().toString()).field("port", server.getPort()).save();
        writeCounter.incrementAndGet();
      }
    } finally {
      graphtNoTx.shutdown();
    }

  }
}
