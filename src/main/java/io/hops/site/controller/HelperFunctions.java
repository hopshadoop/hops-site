package io.hops.site.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hops.site.dao.entity.PopularDataset;
import io.hops.site.dao.entity.RegisteredCluster;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import io.hops.site.dao.facade.PopularDatasetFacade;
import io.hops.site.dao.facade.RegisteredClusterFacade;
import io.hops.site.dto.AddressJSON;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
@TransactionAttribute(TransactionAttributeType.NEVER)
public class HelperFunctions {

  @EJB
  RegisteredClusterFacade registeredClusterFacade;
  @EJB
  PopularDatasetFacade popularDatasetFacade;

  private final ObjectMapper mapper = new ObjectMapper();

  public boolean isValid(String cert) {
    return true;
  }

  public boolean ClusterRegisteredWithEmail(String email) {
    return !this.registeredClusterFacade.findByEmail(email).isEmpty();
  }

  public String registerCluster(String search_endpoint, String email, String cert, AddressJSON gvod_endpoint) {

    try {
      String uniqueId = UUID.randomUUID().toString();
      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
      Date date = new Date();

      this.registeredClusterFacade.create(new RegisteredCluster(uniqueId, search_endpoint, email, cert, mapper.
              writeValueAsString(gvod_endpoint), 0, dateFormat.format(date), dateFormat.format(date)));

      return uniqueId;
    } catch (JsonProcessingException ex) {
      Logger.getLogger(HelperFunctions.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
  }

  public List<RegisteredCluster> getAllRegisteredClusters() {

    return this.registeredClusterFacade.findAll();
  }

  public List<PopularDataset> getTopTenDatasets() {

    List<PopularDataset> to_ret = new LinkedList<>(popularDatasetFacade.findAll());

    if (to_ret.size() <= 10) {
      return to_ret;
    } else {
      List<PopularDataset> top_ten = new LinkedList<>();
      for (int i = 0; i < 10; i++) {
        top_ten.add(to_ret.get(i));
      }
      return top_ten;
    }
  }

  public boolean ClusterRegisteredWithId(String cluster_id) {

    return this.registeredClusterFacade.find(cluster_id) != null;
  }

}
