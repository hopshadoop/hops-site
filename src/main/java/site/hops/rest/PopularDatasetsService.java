/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import site.hops.beans.PopularDatasetFacade;
import site.hops.entities.PopularDataset;
import site.hops.io.failure.FailJSON;
import site.hops.io.identity.IdentificationJSON;
import site.hops.io.popularDatasets.ManifestJSON;
import site.hops.io.popularDatasets.PopularDatasetJSON;
import site.hops.io.register.AddressJSON;
import site.hops.tools.HelperFunctions;

/**
 *
 * @author jsvhqr
 */
@Path("myresource")
public class PopularDatasetsService {

    @EJB
    PopularDatasetFacade popularDatasetsFacade;
    @EJB
    HelperFunctions helperFunctions;
    
    private final ObjectMapper mapper = new ObjectMapper();

    @PUT
    @Path("populardatasets")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response PopularDatasets(IdentificationJSON identification) throws IOException {

        if (!helperFunctions.ClusterRegisteredWithId(identification.getClusterId())) {
            return Response.status(403).entity(new FailJSON("invalid cluster id")).build();
        } else {
            
            List<PopularDatasetJSON> popularDatasetsJsons = new LinkedList<>();
            for(PopularDataset pd : helperFunctions.getTopTenDatasets()){
                ManifestJSON manifestJson = mapper.readValue(pd.getManifest(), ManifestJSON.class);
                
                List<AddressJSON> gvodEndpoints = mapper.readValue(pd.getPartners(), new TypeReference<List<AddressJSON>>(){});
                
                popularDatasetsJsons.add(new PopularDatasetJSON(manifestJson, pd.getDatasetId(), pd.getLeeches(), pd.getSeeds(), gvodEndpoints));

            }
            
            GenericEntity<List<PopularDatasetJSON>> searchResults = new GenericEntity<List<PopularDatasetJSON>>(popularDatasetsJsons) {};
            return Response.status(200).entity(searchResults).build();
        }

    }

    @POST
    @Path("populardatasets")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public void PopularDatasetsAdd(PopularDatasetJSON popularDatasetsJson) throws JsonProcessingException {

        if (popularDatasetsJson.getIdentification().getClusterId() != null && helperFunctions.ClusterRegisteredWithId(popularDatasetsJson.getIdentification().getClusterId())) {

            
            PopularDataset popularDataset = new PopularDataset(
                    popularDatasetsJson.getDatasetId(), 
                    mapper.writeValueAsString(popularDatasetsJson.getManifestJson()), 
                    mapper.writeValueAsString(popularDatasetsJson.getGvodEndpoints()), 
                    popularDatasetsJson.getLeeches(),popularDatasetsJson.getSeeds());
           
            this.popularDatasetsFacade.create(popularDataset);
        }

    }
    
    
    
}
