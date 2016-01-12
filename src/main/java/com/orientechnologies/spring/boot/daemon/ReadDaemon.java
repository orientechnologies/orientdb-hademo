package com.orientechnologies.spring.boot.daemon;

import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.spring.boot.OrientDBFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Enrico Risa on 10/11/15.
 */
public class ReadDaemon extends Thread {

  AtomicLong               readCounter = new AtomicLong(0);
  private OrientDBFactory  factory;
  private volatile boolean running     = true;
  private Object           lock        = new Object();

  public ReadDaemon(OrientDBFactory factory, AtomicLong readCounter) {
    this.factory = factory;
    this.readCounter = readCounter;
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
        if (running) {
          graphtNoTx.command(new OCommandSQL("select count(*) from V")).execute();
          readCounter.incrementAndGet();
        } else {
          synchronized (lock) {
            lock.wait();
          }

        }
      }
    } catch (InterruptedException e) {

    } finally {

      graphtNoTx.shutdown();
    }
  }

  public void pauseRead() {
    running = false;
  }

  public void resumeRead() {
    synchronized (lock) {
      lock.notifyAll();
    }
    running = true;
  }
}
