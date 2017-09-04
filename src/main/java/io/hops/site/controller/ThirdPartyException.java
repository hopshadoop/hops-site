package io.hops.site.controller;

public class ThirdPartyException {
  public static enum Error {
    USER_NOT_REGISTERED("user not registered");
    
    private final String msg;
    
    Error(String msg) {
      this.msg = msg;
    }
    
    public boolean is(String msg) {
      return this.msg.equals(msg);
    }

    @Override
    public String toString() {
      return msg;
    }
  }
}
