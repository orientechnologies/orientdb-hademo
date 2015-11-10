package com.orientechnologies.spring.boot.daemon;

import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.orientechnologies.spring.boot.OrientDBFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by Enrico Risa on 10/11/15.
 */
@Component
public class InsertDaemon {

  public static int       limit = 1000;
  @Autowired
  private OrientDBFactory factory;

  @Scheduled(fixedDelay = 1000)
  public void insert() {

    OrientGraphNoTx graphtNoTx = factory.getGraphtNoTx();
    long v = graphtNoTx.countVertices("V");
    if (v < limit) {
      graphtNoTx.command(new OCommandSQL("insert into v set uuid = ?")).execute(UUID.randomUUID().toString());
    }
  }
}
