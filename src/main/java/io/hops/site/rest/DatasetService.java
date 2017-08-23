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

import io.hops.site.common.AppException;
import io.hops.site.controller.DatasetController;
import io.hops.site.dto.BasicIdentifyDTO;
import io.hops.site.dto.DatasetIssueDTO;
import io.hops.site.dto.JsonResponse;
import io.hops.site.dto.OkDTO;
import io.hops.site.dto.PublishDatasetDTO;
import io.hops.site.dto.SearchDTO;
import io.hops.site.rest.annotation.NoCache;
import io.swagger.annotations.Api;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("dataset")
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/dataset",
        description = "Dataset service")
@TransactionAttribute(TransactionAttributeType.NEVER)
public class DatasetService {

  private final static Logger LOGGER = Logger.getLogger(DatasetService.class.getName());
  @EJB
  private DatasetController datasetCtrl;

  @POST
  @NoCache
  @Path("search")
  public Response search(SearchDTO.Params searchParams,
    @Context HttpServletRequest req) {
    searchDTOParamsSanityCheck(searchParams);
    try {
      SearchDTO.BaseResult searchResult = datasetCtrl.search(searchParams);
      return Response.ok().entity(searchResult).build();
    } catch (AppException ex) {
      return Response.status(ex.getStatus()).entity(new JsonResponse(ex.getMessage())).build();
    }
  }
  
  private void searchDTOParamsSanityCheck(SearchDTO.Params searchParams) {
  }
  
  @POST
  @NoCache
  @Path("page")
  public Response getPage(@QueryParam("sessionId") String sessionId, @QueryParam("startItem") int startItem,
    @QueryParam("nrElem") int nrElem) {
    try {
      SearchDTO.Page result = datasetCtrl.getSearchPage(sessionId, startItem, nrElem);
      return Response.ok().entity(result).build();
    } catch (AppException ex) {
      return Response.status(ex.getStatus()).entity(new JsonResponse(ex.getMessage())).build();
    }
  }
  
  @PUT
  @NoCache
  @Path("publish")
  public Response publish(PublishDatasetDTO.Request msg) {
    publishDatasetSanityCheck(msg);
    try {
      String datasetPublicId = datasetCtrl.publishDataset(msg);
      LOGGER.log(Level.INFO, "published dataset:{}", msg.getName());
      return Response.ok().entity(new PublishDatasetDTO.Response(datasetPublicId)).build();
    } catch (AppException ex) {
      LOGGER.log(Level.WARNING, "could not publish dataset - {}", ex.getMessage());
      return Response.status(ex.getStatus()).entity(new JsonResponse(ex.getMessage())).build();
    }
  }

  private void publishDatasetSanityCheck(PublishDatasetDTO.Request publishDataset) {
  }
  
  @POST
  @NoCache
  @Path("unpublish")
  public Response unpublish(BasicIdentifyDTO msg) {
    try {
      datasetCtrl.unpublishDataset(msg.getDatasetId(), msg.getClusterId());
      LOGGER.log(Level.INFO, "unpublished dataset:{}", msg.getDatasetId());
      return Response.ok(new OkDTO()).build();
    } catch (AppException ex) {
      LOGGER.log(Level.WARNING, "could not unpublish dataset - {}", ex.getMessage());
      return Response.status(ex.getStatus()).entity(new JsonResponse(ex.getMessage())).build();
    }
  }
  
  @POST
  @NoCache
  @Path("download")
  public Response download(BasicIdentifyDTO msg) {
    try {
      datasetCtrl.download(msg.getDatasetId(), msg.getClusterId());
      LOGGER.log(Level.INFO, "download dataset:{}", msg.getDatasetId());
      return Response.ok().entity(new OkDTO()).build();
    } catch (AppException ex) {
      LOGGER.log(Level.WARNING, "could not download dataset - {}", ex.getMessage());
      return Response.status(ex.getStatus()).entity(new JsonResponse(ex.getMessage())).build();
    }
  }

  @POST
  @NoCache
  @Path("complete")
  public Response downloadComplete(BasicIdentifyDTO msg) {
    try {
      datasetCtrl.complete(msg.getDatasetId(), msg.getClusterId());
      LOGGER.log(Level.INFO, "download complete dataset:{}", msg.getDatasetId());
      return Response.ok().entity(new OkDTO()).build();
    } catch (AppException ex) {
      LOGGER.log(Level.WARNING, "could not complete download dataset - {}", ex.getMessage());
      return Response.status(ex.getStatus()).entity(new JsonResponse(ex.getMessage())).build();
    }
  }

  @POST
  @NoCache
  @Path("remove")
  public Response downloadAbort(BasicIdentifyDTO msg) {
    try {
      datasetCtrl.remove(msg.getDatasetId(), msg.getClusterId());
      LOGGER.log(Level.INFO, "remove conn dataset:{}", msg.getDatasetId());
      return Response.ok().build();
    } catch (AppException ex) {
      LOGGER.log(Level.WARNING, "could not remove conn dataset - {}", ex.getMessage());
      return Response.status(ex.getStatus()).entity(new JsonResponse(ex.getMessage())).build();
    }
  }
  
  @POST
  @Path("datasetIssue")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addDatasetIssue(DatasetIssueDTO datasetIssue) {
    datasetCtrl.reportDatasetIssue(datasetIssue);
    LOGGER.log(Level.INFO, "Add dataset issue for dataset: {0}", datasetIssue.getDatasetId());
    return Response.ok().build();
  }
}
