package com.orientechnologies.spring.boot;

import com.orientechnologies.orient.client.remote.OServerAdmin;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
public class OrientDBFactory {

  private OrientGraphFactory         factory;

  @Autowired
  private OrientDBConnectionSettings settings;

  ThreadLocal<OrientGraph>           graphThreadLocal = new ThreadLocal<OrientGraph>();

  public OrientDBFactory() {

  }

  @PostConstruct
  public void initFactory() {
    factory = new OrientGraphFactory(settings.getUrl(), settings.getUsr(), settings.getPwd());

    try {

      OServerAdmin admin = new OServerAdmin(settings.getUrl());
      admin.connect(settings.getRootUser(), settings.getRootPwd());
      if (!admin.existsDatabase()) {
        admin.createDatabase("demo", "graph", "plocal");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public OrientGraph getGraph() {

    OrientGraph graph = graphThreadLocal.get();
    if (graph == null) {
      graph = factory.getTx();
      graphThreadLocal.set(graph);
    }
    return graph;
  }

  public OrientGraphNoTx getGraphtNoTx() {
    return factory.getNoTx();
  }

  public void unsetDb() {
    graphThreadLocal.set(null);
  }

}
