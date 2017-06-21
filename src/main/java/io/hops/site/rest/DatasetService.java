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
import io.hops.site.dao.entity.Dataset;
import io.hops.site.dto.DatasetDTO;
import io.hops.site.dto.DatasetIssueDTO;
import io.hops.site.rest.annotation.NoCache;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
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
  private DatasetController datasetController;

  @GET
  @NoCache
  public Response getAll() {
    List<Dataset> datasets = datasetController.findAllDatasets();
    GenericEntity<List<Dataset>> datasetList
            = new GenericEntity<List<Dataset>>(datasets) {
    };
    LOGGER.log(Level.INFO, "Get all datasets");
    return Response.ok().entity(datasetList).build();
  }

  @GET
  @NoCache
  @Path("{datasetId}")
  public Response getADataset(@PathParam("datasetId") Integer datasetId) {
    Dataset dataset = datasetController.findDataset(datasetId);
    LOGGER.log(Level.INFO, "Get a dataset with id: {0}", dataset.getId());
    return Response.ok().entity(dataset).build();
  }

  @GET
  @NoCache
  @Path("byPublicId/{publicId}")
  public Response getByPublicId(@PathParam("publicId") String publicId) {
    Dataset dataset = datasetController.findDatasetByPublicId(publicId);
    LOGGER.log(Level.INFO, "Get a dataset with public id: {0}:", dataset.getId());
    return Response.ok().entity(dataset).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addDatase(DatasetDTO dataset) {
    datasetController.addDataset(dataset);
    LOGGER.log(Level.INFO, "Add new dataset with public id: {0}", dataset.getPublicId());
    return Response.ok().build();
  }

  @POST
  @Path("datasetIssue")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addDatasetIssue(DatasetIssueDTO datasetIssue) {
    datasetController.reportDatasetIssue(datasetIssue);
    LOGGER.log(Level.INFO, "Add dataset issue for dataset: {0}", datasetIssue.getDatasetId());
    return Response.ok().build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateDataset(DatasetDTO dataset) {
    datasetController.updateDataset(dataset);
    LOGGER.log(Level.INFO, "Update rating with id: {0}", dataset.getId());
    return Response.ok().build();
  }

  @PUT
  @Path("addCategory")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addCategory(DatasetDTO dataset) {
    datasetController.addCategory(dataset);
    LOGGER.log(Level.INFO, "Update rating with id: {0}", dataset.getId());
    return Response.ok().build();
  }

  @DELETE
  @Path("{datasetId}")
  public Response deleteDataset(@PathParam("datasetId") Integer datasetId) {
    datasetController.removeDataset(datasetId);
    LOGGER.log(Level.INFO, "Delete dataset with id: {0}", datasetId);
    return Response.ok().build();
  }

  @DELETE
  @Path("byPublicId/{publicId}")
  public Response deleteDataset(@PathParam("publicId") String publicId) {
    datasetController.removeDataset(publicId);
    LOGGER.log(Level.INFO, "Delete dataset with id: {0}", publicId);
    return Response.ok().build();
  }
}
