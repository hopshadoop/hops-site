/*
 * Copyright 2017.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.hops.site.rest;

import io.hops.site.controller.DatasetController;
import io.hops.site.controller.HopsSiteSettings;
import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.facade.DatasetFacade;
import io.hops.site.dto.DatasetDTO;
import io.hops.site.old_dto.DatasetIssueDTO;
import io.hops.site.old_dto.JsonResponse;
import io.hops.site.old_dto.PopularDatasetDTO;
import io.hops.site.rest.annotation.NoCache;
import io.hops.site.rest.exception.AppException;
import io.swagger.annotations.Api;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jersey.repackaged.com.google.common.primitives.Ints;

@Path("private/dataset")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "private/dataset",
        description = "Dataset service")
@TransactionAttribute(TransactionAttributeType.NEVER)
public class PrivateDatasetService {

  private final static Logger LOG = Logger.getLogger(PrivateDatasetService.class.getName());
  @EJB
  private DatasetController datasetCtrl;
  @EJB
  private DatasetFacade datasetFacade;

  @POST
  @NoCache
  @Path("/publish/{publicCId}")
  public Response publish(@PathParam("publicCId") String publicCId, DatasetDTO.Proto msg) throws AppException {
    publishDatasetSanityCheck(msg);
    try {
      LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset:cluster:{0} publishing", 
        new Object[]{publicCId});
      String publicDSId = datasetCtrl.publishDataset(publicCId, msg);
      LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset:done - {0} cluster:{1} publishing", 
        new Object[]{publicDSId, publicCId});
      return Response.ok(publicDSId).build();
    } catch (AppException ex) {
      LOG.log(Level.WARNING, "could not publish dataset - {0}", ex.getMessage());
      throw ex;
    }
  }

  private void publishDatasetSanityCheck(DatasetDTO.Proto dataset) {
  }
  
  @POST
  @NoCache
  @Path("unpublish/{publicCId}/{publicDSId}")
  public Response unpublish(@PathParam("publicCId") String publicCId, @PathParam("publicDSId") String publicDSId) {
    try {
      LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset - {0} cluster:{1} unpublishing", 
        new Object[]{publicDSId, publicCId});
      datasetCtrl.unpublishDataset(publicDSId, publicCId);
      LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset:done - {0} cluster:{1} unpublishing", 
        new Object[]{publicDSId, publicCId});
      return Response.ok("ok").build();
    } catch (AppException ex) {
      LOG.log(Level.WARNING, "could not unpublish dataset - {0}", ex.getMessage());
      return Response.status(ex.getStatus()).entity(new JsonResponse(ex.getMessage())).build();
    }
  }
  
  @GET
  @NoCache
  @Path("byPublicId/{publicDSId}")
  public Response getByPublicId(@PathParam("publicDSId") String publicDSId) {
    Optional<Dataset> dataset = datasetCtrl.findDatasetByPublicId(publicDSId);
    if (!dataset.isPresent()) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    DatasetDTO.Owner owner = new DatasetDTO.Owner(dataset.get().getOwner());
    int datasetRating = dataset.get().getRating() == null ? 0 : dataset.get().getRating();
    DatasetDTO.Complete ds = new DatasetDTO.Complete(owner, dataset.get().getName(), dataset.get().getDescription(),
            dataset.get().getCategories(), dataset.get().getMadePublicOn(), datasetRating, dataset.get().getDsSize());
    LOG.log(Level.INFO, "Get dataset for id:{0}", publicDSId);
    return Response.status(Response.Status.OK).entity(ds).build();
  }

  
  @POST
  @NoCache
  @Path("download/{publicCId}/{publicDSId}")
  public Response download(@PathParam("publicCId") String publicCId, @PathParam("publicDSId") String publicDSId) {
    try {
      LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset - {0} cluster:{1} downloading", 
        new Object[]{publicDSId, publicCId});
      datasetCtrl.download(publicDSId, publicCId);
      LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset:done - {0} cluster:{1} downloading", 
        new Object[]{publicDSId, publicCId});
      return Response.ok("ok").build();
    } catch (AppException ex) {
      LOG.log(Level.WARNING, "could not download dataset - {}", ex.getMessage());
      return Response.status(ex.getStatus()).entity(new JsonResponse(ex.getMessage())).build();
    }
  }

  @POST
  @NoCache
  @Path("complete/{publicCId}/{publicDSId}")
  public Response complete(@PathParam("publicCId") String publicCId, @PathParam("publicDSId") String publicDSId) {
    try {
      LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset - {0} cluster:{1} completing", 
        new Object[]{publicDSId, publicCId});
      datasetCtrl.complete(publicDSId, publicCId);
      LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset:done - {0} cluster:{1} downloading", 
        new Object[]{publicDSId, publicCId});
      return Response.ok("ok").build();
    } catch (AppException ex) {
      LOG.log(Level.WARNING, "could not complete download dataset - {}", ex.getMessage());
      return Response.status(ex.getStatus()).entity(new JsonResponse(ex.getMessage())).build();
    }
  }

  @POST
  @NoCache
  @Path("remove/{publicCId}/{publicDSId}")
  public Response remove(@PathParam("publicCId") String publicCId, @PathParam("publicDSId") String publicDSId) {
    try {
      LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset - {0} cluster:{1} removing", 
        new Object[]{publicDSId, publicCId});
      datasetCtrl.remove(publicDSId, publicCId);
      LOG.log(HopsSiteSettings.DELA_DEBUG, "hops_site:dataset:done - {0} cluster:{1} removing", 
        new Object[]{publicDSId, publicCId});
      return Response.ok("ok").build();
    } catch (AppException ex) {
      LOG.log(Level.WARNING, "could not remove conn dataset - {}", ex.getMessage());
      return Response.status(ex.getStatus()).entity(new JsonResponse(ex.getMessage())).build();
    }
  }
  
  //******************************************************************************************************************** 
  
  @GET
  @NoCache
  public Response getAll() {
    List<Dataset> result = datasetFacade.findAll();
    Comparator<Dataset> lexiComp = new Comparator<Dataset>() {
      @Override
      public int compare(Dataset o1, Dataset o2) {
        int res = o1.getName().compareTo(o2.getName());
        if(res == 0) {
          res = Ints.compare(o1.getVersion(), o2.getVersion());
        }
        return res;
      }
    };
    Collections.sort(result, lexiComp);
    GenericEntity<List<Dataset>> genericResult = new GenericEntity<List<Dataset>>(result){};
    return Response.ok(genericResult).build();
  }
  
  @GET
  @NoCache
  @Path("popular")
  public Response popular(PopularDatasetDTO req) {
    List<PopularDatasetDTO> result = new LinkedList<>();
    GenericEntity genericResult = new GenericEntity<List<PopularDatasetDTO>>(result){};
    return Response.ok(genericResult).build();
  }
  
  @POST
  @Path("datasetIssue")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addDatasetIssue(DatasetIssueDTO datasetIssue) {
    datasetCtrl.reportDatasetIssue(datasetIssue);
    LOG.log(Level.INFO, "Add dataset issue for dataset: {0}", datasetIssue.getDatasetId());
    return Response.ok().build();
  }
}
