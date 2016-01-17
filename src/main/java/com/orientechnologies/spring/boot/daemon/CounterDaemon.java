package com.orientechnologies.spring.boot.daemon;

import com.orientechnologies.orient.core.Orient;
import com.orientechnologies.spring.boot.DaemonConfig;
import com.orientechnologies.spring.boot.OrientDBFactory;
import com.orientechnologies.spring.boot.ServerConfig;
import com.orientechnologies.spring.boot.dto.Count;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Enrico Risa on 12/01/16.
 */
@Component
public class CounterDaemon {

  AtomicLong writeCounter = new AtomicLong(0);
  AtomicLong readCounter  = new AtomicLong(0);

  ReadDaemon[]   readThreads;
  InsertDaemon[] writeThreads;

  @Autowired
  ServerConfig server;

  @Autowired
  DaemonConfig daemonConfig;

  protected Count         count = new Count();
  @Autowired
  private OrientDBFactory factory;

  @Scheduled(fixedDelay = 10000000)
  public void start() throws InterruptedException {

    Orient.instance().scheduleTask(new TimerTask() {
      @Override
      public void run() {
        updateCounter();
      }
    }, 1000, 1000);
    int threadsNumber = daemonConfig.getThreads();

    readThreads = new ReadDaemon[threadsNumber];
    writeThreads = new InsertDaemon[threadsNumber];

    for (int i = 0; i < threadsNumber; i++) {
      readThreads[i] = new ReadDaemon(factory, readCounter);
    }
    for (int i = 0; i < threadsNumber; i++) {
      readThreads[i].start();
    }
    for (int i = 0; i < threadsNumber; i++) {
      writeThreads[i] = new InsertDaemon(factory, server, writeCounter);
    }

    for (int i = 0; i < threadsNumber; i++) {
      writeThreads[i].start();
    }

    for (int i = 0; i < threadsNumber; i++) {
      readThreads[i].join();
    }
    for (int i = 0; i < threadsNumber; i++) {
      writeThreads[i].join();
    }

  }

  public void changeState(String action, Boolean state) {

    if ("read".equalsIgnoreCase(action)) {

      if (Boolean.TRUE.equals(state)) {
        resumeRead();
      } else {
        pauseRead();
      }
    } else if ("write".equalsIgnoreCase(action)) {
      if (Boolean.TRUE.equals(state)) {
        resumeWrite();
      } else {
        pauseWrite();
      }
    }
  }

  public void pauseRead() {
    for (ReadDaemon readThread : readThreads) {
      readThread.pauseRead();
    }
  }

  public void resumeRead() {
    for (ReadDaemon readThread : readThreads) {
      readThread.resumeRead();
    }
  }

  public void pauseWrite() {
    for (InsertDaemon wThread : writeThreads) {
      wThread.pauseWrite();
    }
  }

  public void resumeWrite() {
    for (InsertDaemon wThread : writeThreads) {
      wThread.resumeWrite();
    }
  }

  public Count getCounter() {

    synchronized (count) {
      return count;
    }

  }

  protected void updateCounter() {
    try {
      System.out.printf("Insert/s [%d] , Read/s [%d] \n", writeCounter.get(), readCounter.get());

      OrientGraphNoTx graphtNoTx = factory.getGraphtNoTx();
      try {
        long v = graphtNoTx.countVertices("V");
        long c = writeCounter.get();
        long r = readCounter.get();
        synchronized (count) {
          count.setCount(v);
          count.setWriteSec(c);
          count.setReadSec(r);
        }
        writeCounter.set(0);
        readCounter.set(0);
      } catch (Throwable e) {
        e.printStackTrace();
      } finally {
        graphtNoTx.shutdown();
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }
}
