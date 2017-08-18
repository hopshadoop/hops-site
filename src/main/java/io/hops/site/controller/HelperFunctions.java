package io.hops.site.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class HelperFunctions {

  private final static Logger LOGGER = Logger.getLogger(HelperFunctions.class.getName());

  private final ObjectMapper mapper = new ObjectMapper();

  public boolean isValid(String cert) {
    return true;
  }
}
