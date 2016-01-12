package com.orientechnologies.spring.boot.controllers;

import com.orientechnologies.spring.boot.OrientDBFactory;
import com.orientechnologies.spring.boot.daemon.CounterDaemon;
import com.orientechnologies.spring.boot.dto.Count;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Enrico Risa on 17/10/15.
 */

@RestController
@RequestMapping("counts")
public class CountController {

  @Autowired
  protected CounterDaemon   counterDaemon;

  @Autowired
  protected OrientDBFactory factory;

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<Count> getCount() {

    return new ResponseEntity(counterDaemon.getCounter(), HttpStatus.OK);

  }

  @RequestMapping(value = "{action}/{state}", method = RequestMethod.GET)
  public ResponseEntity<Count> pause(@PathVariable("action") String action, @PathVariable("state") Boolean state) {
    counterDaemon.changeState(action, state);
    return new ResponseEntity(HttpStatus.OK);

  }
}
