package com.orientechnologies.spring.boot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.orientechnologies.spring.boot.OrientDBFactory;
import com.orientechnologies.spring.boot.daemon.CounterDaemon;
import com.orientechnologies.spring.boot.dto.Count;

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
}
