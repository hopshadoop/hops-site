/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.rest;

import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import site.hops.beans.PopularDatasetsFacade;
import site.hops.entities.PopularDatasets;
import site.hops.model.popular_datasets.FailureAddDatasetJson;
import site.hops.model.popular_datasets.FailureGetPopularDatasetsJson;
import site.hops.model.popular_datasets.GetPopularDatasetsJson;
import site.hops.model.popular_datasets.PopularDatasetsJson;
import site.hops.model.popular_datasets.SuccessAddDatasetJson;
import site.hops.model.popular_datasets.SuccessGetPopularDatasetsJson;
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
    
    
    @PUT
    @Path("populardatasets")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response PopularDatasets(GetPopularDatasetsJson getPopularDatasetsJson) {
        
        if(!helperFunctions.ClusterRegisteredWithId(getPopularDatasetsJson.getClusterId())){
            return Response.status(403).entity(new FailureGetPopularDatasetsJson("invalid cluster id")).build();
        }else{
            List<PopularDatasets> popularDatasets = this.popularDatasetsFacade.findAll();
                return Response.status(200).entity(new SuccessGetPopularDatasetsJson(popularDatasets)).build();
        }
        
    }
    
    @POST
    @Path("populardatasets")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response PopularDatasetsAdd(PopularDatasetsJson popularDatasetsJson) {
        
        if(!helperFunctions.ClusterRegisteredWithId(popularDatasetsJson.getClusterId())){
            return Response.status(403).entity(new FailureAddDatasetJson("invalid id")).build();
        }else{
            popularDatasetsFacade.create(new PopularDatasets(popularDatasetsJson.getDatasetName(), popularDatasetsJson.getDatasetId(),popularDatasetsJson.getFiles(),popularDatasetsJson.getLeeches(),popularDatasetsJson.getSeeds(),popularDatasetsJson.getStructure()));
            return Response.status(200).entity(new SuccessAddDatasetJson("Added your dataset")).build();
        }
                
                
    }
    
    
}
