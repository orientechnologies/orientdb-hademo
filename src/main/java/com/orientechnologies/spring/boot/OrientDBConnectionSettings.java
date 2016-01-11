package com.orientechnologies.spring.boot;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Enrico Risa on 17/10/14.
 */
@Component
@ConfigurationProperties(prefix = "connection")
public class OrientDBConnectionSettings {

  private String url;
  private String usr;
  private String pwd;

  private String rootUser;
  private String rootPwd;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUsr() {
    return usr;
  }

  public void setUsr(String usr) {
    this.usr = usr;
  }

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }


  public String getRootPwd() {
    return rootPwd;
  }

  public void setRootPwd(String rootPwd) {
    this.rootPwd = rootPwd;
  }

  public String getRootUser() {
    return rootUser;
  }

  public void setRootUser(String rootUser) {
    this.rootUser = rootUser;
  }
}
