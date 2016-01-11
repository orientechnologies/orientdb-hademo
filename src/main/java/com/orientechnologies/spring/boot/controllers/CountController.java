package com.orientechnologies.spring.boot.controllers;

import com.orientechnologies.spring.boot.OrientDBFactory;
import com.orientechnologies.spring.boot.daemon.InsertDaemon;
import com.orientechnologies.spring.boot.dto.Count;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  protected InsertDaemon    insertDaemon;

  @Autowired
  protected OrientDBFactory factory;

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<Count> getCount() {

    return new ResponseEntity(insertDaemon.getCounter(), HttpStatus.OK);

  }
}
