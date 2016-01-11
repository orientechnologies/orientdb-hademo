package com.orientechnologies.spring.boot.daemon;

import com.orientechnologies.orient.core.Orient;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.spring.boot.OrientDBFactory;
import com.orientechnologies.spring.boot.ServerConfig;
import com.orientechnologies.spring.boot.dto.Count;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Enrico Risa on 10/11/15.
 */
@Component
public class InsertDaemon {

  AtomicLong              counter = new AtomicLong(0);

  private ReadDaemon      readDaemon;
  @Autowired
  private OrientDBFactory factory;

  @Autowired
  ServerConfig            server;

  protected Count         count   = new Count();

  @PostConstruct
  protected void initReadDemon() {
    readDaemon = new ReadDaemon(factory);
  }

  @Scheduled(fixedDelay = 10000000)
  public void insert() {

    OrientGraphNoTx graphtNoTx = factory.getGraphtNoTx();

    try {
      Orient.instance().scheduleTask(new TimerTask() {
        @Override
        public void run() {
          updateCounter();
        }
      }, 1000, 1000);
      new Thread(readDaemon).start();
      while (true) {
        new ODocument("V").field("uuid", UUID.randomUUID().toString()).field("port", server.getPort()).save();
        counter.incrementAndGet();

      }

    } finally {
      graphtNoTx.shutdown();
    }
  }

  protected void updateCounter() {
    System.out.printf("Insert/s [%d] , Read/s [%d] \n", counter.get(), readDaemon.getReadCounter());
    OrientGraphNoTx graphtNoTx = factory.getGraphtNoTx();
    try {
      long v = graphtNoTx.countVertices("V");
      long c = counter.get();
      long r = readDaemon.getReadCounter();
      synchronized (count) {
        count.setCount(v);
        count.setWriteSec(c);
        count.setReadSec(r);
      }
      counter.set(0);
      readDaemon.setReadCounter(0l);

    } finally {
      graphtNoTx.shutdown();
    }
  }

  public Count getCounter() {

    synchronized (count) {
      return count;
    }

  }

}
