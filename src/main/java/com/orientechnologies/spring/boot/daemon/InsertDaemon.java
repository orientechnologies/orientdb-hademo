package com.orientechnologies.spring.boot.daemon;

import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.spring.boot.OrientDBFactory;
import com.orientechnologies.spring.boot.ServerConfig;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by Enrico Risa on 10/11/15.
 */
@Component
public class InsertDaemon {

  @Autowired
  private OrientDBFactory factory;

  @Autowired
  ServerConfig server;

  @Scheduled(fixedDelay = 10000000)
  public void insert() {

    OrientGraphNoTx graphtNoTx = factory.getGraphtNoTx();

    try {

      while (true) {
        new ODocument("V").field("uuid", UUID.randomUUID().toString()).field("port", server.getPort()).save();
      }

    } finally {
      graphtNoTx.shutdown();
    }
  }
}
