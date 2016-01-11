package com.orientechnologies.spring.boot.daemon;

import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.spring.boot.OrientDBFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Enrico Risa on 10/11/15.
 */
public class ReadDaemon implements Runnable {

  AtomicLong              readCounter = new AtomicLong(0);
  private OrientDBFactory factory;

  public ReadDaemon(OrientDBFactory factory) {
    this.factory = factory;
  }

  public Long getReadCounter() {
    return readCounter.get();
  }

  public void setReadCounter(Long readCounter) {
    this.readCounter.set(readCounter);
  }

  @Override
  public void run() {

    OrientGraphNoTx graphtNoTx = factory.getGraphtNoTx();

    try {
      while (true) {
        graphtNoTx.command(new OCommandSQL("select count(*) from V")).execute();
        readCounter.incrementAndGet();
      }
    } finally {

      graphtNoTx.shutdown();
    }
  }
}
