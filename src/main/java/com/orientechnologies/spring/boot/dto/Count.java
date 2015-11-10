package com.orientechnologies.spring.boot.dto;

/**
 * Created by Enrico Risa on 17/10/15.
 */
public class Count {

  long count;
  long limit;

  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  public void setLimit(long limit) {
    this.limit = limit;
  }

  public long getLimit() {
    return limit;
  }
}
