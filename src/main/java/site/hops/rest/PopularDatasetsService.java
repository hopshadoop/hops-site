/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
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
import site.hops.beans.PartnerFacade;
import site.hops.beans.PopularDatasetFacade;
import site.hops.entities.Partner;
import site.hops.entities.PopularDataset;
import site.hops.io.failure.FailJson;
import site.hops.io.identity.IdentificationJson;
import site.hops.io.popularDatasets.ManifestJson;
import site.hops.io.popularDatasets.PopularDatasetJson;
import site.hops.io.leechseed.LeechSeedJson;
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
    PartnerFacade partnerFacade;
    @EJB
    HelperFunctions helperFunctions;
    
    private final ObjectMapper mapper = new ObjectMapper();

    @PUT
    @Path("populardatasets")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response PopularDatasets(IdentificationJson identificatiob) throws IOException {

        if (!helperFunctions.ClusterRegisteredWithId(identificatiob.getClusterId())) {
            return Response.status(403).entity(new FailJson("invalid cluster id")).build();
        } else {
            
            List<PopularDatasetJson> popularDatasetsJsons = new LinkedList<>();
            for(PopularDataset pd : helperFunctions.getTopTenDatasets()){
                ManifestJson manifestJson = mapper.readValue(pd.getManifest(), ManifestJson.class);
                List<String> gvodEndpoints = new ArrayList<>();
                for(Partner p : pd.getPartnerCollection()){
                    gvodEndpoints.add(p.getGvodUdpEndpoint());
                } 
                popularDatasetsJsons.add(new PopularDatasetJson(manifestJson, pd.getDatasetId(), pd.getLeeches(), pd.getSeeds(), gvodEndpoints));

            }
            
            GenericEntity<List<PopularDatasetJson>> searchResults = new GenericEntity<List<PopularDatasetJson>>(popularDatasetsJsons) {};
            return Response.status(200).entity(searchResults).build();
        }

    }

    @POST
    @Path("populardatasets")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public void PopularDatasetsAdd(PopularDatasetJson popularDatasetsJson) throws JsonProcessingException {

        if (popularDatasetsJson.getIdentification().getClusterId() != null && helperFunctions.ClusterRegisteredWithId(popularDatasetsJson.getIdentification().getClusterId())) {

            List<Partner> partners = new LinkedList<>();

            for (String s : popularDatasetsJson.getGvodEndpoints()) {
                Partner p = new Partner();
                p.setGvodUdpEndpoint(s);
            }
            
            PopularDataset popularDataset = new PopularDataset(popularDatasetsJson.getDatasetId(), mapper.writeValueAsString(popularDatasetsJson.getManifestJson()), popularDatasetsJson.getLeeches(),popularDatasetsJson.getSeeds());
            
            popularDataset.setPartnerCollection(partners);
            
            this.popularDatasetsFacade.create(popularDataset);
        }

    }
    
    @POST
    @Path("seed")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public void Seed(LeechSeedJson seedJson) throws JsonProcessingException {

        if(helperFunctions.ClusterRegisteredWithId(seedJson.getIdentity().getClusterId())){
            
            PopularDataset popularDataset = this.popularDatasetsFacade.find(seedJson.getDatasetId());
            
            popularDataset.setSeeds(popularDataset.getSeeds() + 1);
            
            Partner p = new Partner();
            p.setGvodUdpEndpoint(seedJson.getGvodUdpEndpoint());
            
            popularDataset.getPartnerCollection().add(p);
            
            this.popularDatasetsFacade.edit(popularDataset);
            
        }

    }
    
    @POST
    @Path("leech")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public void Leech(LeechSeedJson leechJson) throws JsonProcessingException {
        
        if(helperFunctions.ClusterRegisteredWithId(leechJson.getIdentity().getClusterId())){
            
            PopularDataset popularDataset = this.popularDatasetsFacade.find(leechJson.getDatasetId());
            
            popularDataset.setLeeches(popularDataset.getSeeds() + 1);
            
            this.popularDatasetsFacade.edit(popularDataset);
        }
    }
    
    @POST
    @Path("stopseed")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public void StopSeed(LeechSeedJson stopSeedJson) throws JsonProcessingException {
        
        if(helperFunctions.ClusterRegisteredWithId(stopSeedJson.getIdentity().getClusterId())){
            
            PopularDataset popularDataset = this.popularDatasetsFacade.find(stopSeedJson.getDatasetId());
            
            popularDataset.setSeeds(popularDataset.getSeeds() - 1);
            
            Partner p = partnerFacade.getPartnerByEndpoint(stopSeedJson.getGvodUdpEndpoint());
            
            popularDataset.getPartnerCollection().remove(p);
            
            this.popularDatasetsFacade.edit(popularDataset);
            
        }
    }
    
    @POST
    @Path("stopleech")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public void StopLeech(LeechSeedJson stopLeechJson) throws JsonProcessingException {

        if(helperFunctions.ClusterRegisteredWithId(stopLeechJson.getIdentity().getClusterId())){
            
            PopularDataset popularDataset = this.popularDatasetsFacade.find(stopLeechJson.getDatasetId());
            
            popularDataset.setLeeches(popularDataset.getSeeds() - 1);
            
            this.popularDatasetsFacade.edit(popularDataset);
            
        }
    }
    
    
    
}
