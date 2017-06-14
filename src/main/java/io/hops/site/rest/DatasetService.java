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

import io.hops.site.dao.entity.Dataset;
import io.hops.site.dao.entity.DatasetIssue;
import io.hops.site.dao.entity.DatasetRating;
import io.hops.site.dao.facade.DatasetFacade;
import io.hops.site.dao.facade.DatasetIssueFacade;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
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
public class DatasetService {

  private final static Logger LOGGER = Logger.getLogger(DatasetService.class.getName());
  @EJB
  private DatasetFacade datasetFacade;
  @EJB
  private DatasetIssueFacade datasetIssueFacade;

  @GET
  public Response getAll() {
    List<Dataset> datasets = datasetFacade.findAll();
    GenericEntity<List<Dataset>> datasetList
            = new GenericEntity<List<Dataset>>(datasets) { };
    LOGGER.log(Level.INFO, "Get all datasets");
    return Response.ok().entity(datasetList).build();
  }

  @GET
  @Path("{datasetId}")
  public Response getADataset(@PathParam("datasetId") Integer datasetId) {
    Dataset dataset = datasetFacade.find(datasetId);
    LOGGER.log(Level.INFO, "Get a dataset:", dataset.getId());
    return Response.ok().entity(dataset).build();
  }

  @GET
  @Path("byPublicId/{publicId}")
  public Response getByPublicId(@PathParam("publicId") String publicId) {
    Dataset dataset = datasetFacade.findByPublicId(publicId);
    LOGGER.log(Level.INFO, "Get a dataset:", dataset.getId());
    return Response.ok().entity(dataset).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addDatase(Dataset dataset) {

    LOGGER.log(Level.INFO, "Add rating for dataset: {0}", dataset.getId());
    return Response.ok().build();
  }

  @POST
  @Path("datasetIssue")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addDatasetIssue(DatasetIssue datasetIssue) {

    LOGGER.log(Level.INFO, "Add dataset issue for dataset: {0}", datasetIssue.getDatasetId().getId());
    return Response.ok().build();
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  public Response updateRating(DatasetRating datasetRating) {

    LOGGER.log(Level.INFO, "Update rating with id: {0}", datasetRating.getId());
    return Response.ok().build();
  }

  @DELETE
  @Path("{commentId}")
  public Response deleteRating(@PathParam("datasetId") Integer datasetId) {

    LOGGER.log(Level.INFO, "Delete dataset with id: {0}", datasetId);
    return Response.ok().build();
  }
}
