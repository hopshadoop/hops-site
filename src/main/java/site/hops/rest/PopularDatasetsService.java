/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.rest;

import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import site.hops.beans.PopularDatasetsFacade;
import site.hops.entities.PopularDatasets;
import site.hops.model.PopularDatasetsJson;
import site.hops.tools.HelperFunctions;

/**
 *
 * @author jsvhqr
 */
@Path("myresource")
public class PopularDatasetsService {
    
    
    
    @EJB 
    PopularDatasetsFacade popularDatasetsFacade;
    @EJB
    HelperFunctions helperFunctions;
    
    
    @GET
    @Path("populardatasets/{cluster_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response PopularDatasets(@PathParam("cluster_id") String cluster_id) {
        
        if(!helperFunctions.ClusterRegisteredWithId(cluster_id)){
            return Response.status(403).entity(null).build();
        }else{
            List<PopularDatasets> popularDatasets = this.popularDatasetsFacade.findAll();
            GenericEntity<List<PopularDatasets>> to_return = new GenericEntity<List<PopularDatasets>>(popularDatasets) {
                };
                return Response.status(200).entity(to_return).build();
        }
        
    }
    
    @POST
    @Path("populardatasets/{cluster_id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response PopularDatasetsAdd(@PathParam("cluster_id") String cluster_id,String json) {
        
        if(!helperFunctions.ClusterRegisteredWithId(cluster_id)){
            return Response.status(403).entity(null).build();
        }else{
            PopularDatasetsJson popularDatasetsJson = new PopularDatasetsJson(json);
            popularDatasetsFacade.create(new PopularDatasets(popularDatasetsJson.getDatasetName(), popularDatasetsJson.getDatasetId(),popularDatasetsJson.getFiles(),popularDatasetsJson.getLeeches(),popularDatasetsJson.getSeeds(),popularDatasetsJson.getStructure()));
            return Response.status(200).build();
        }
                
                
    }
    
    
}
