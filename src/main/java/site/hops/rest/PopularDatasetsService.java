/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.rest;

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
import site.hops.beans.PopularDatasetFacade;
import site.hops.entities.DatasetStructure;
import site.hops.entities.Partner;
import site.hops.entities.PopularDataset;
import site.hops.io.failure.FailJson;
import site.hops.io.identity.IdentificationJson;
import site.hops.io.popularDatasets.DatasetStructureJson;
import site.hops.io.popularDatasets.PopularDatasetJson;
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

    @PUT
    @Path("populardatasets")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public Response PopularDatasets(IdentificationJson identificatiob) {

        if (!helperFunctions.ClusterRegisteredWithId(identificatiob.getClusterId())) {
            return Response.status(403).entity(new FailJson("invalid cluster id")).build();
        } else {
            
            List<PopularDatasetJson> popularDatasetsJsons = new LinkedList<>();
            for(PopularDataset pd : helperFunctions.getTopTenDatasets()){
                DatasetStructureJson datasetStructureJson = new DatasetStructureJson(pd.getDatasetStructure().getDatasetName(), pd.getDatasetStructure().getDatasetDescription(), pd.getDatasetStructure().getManifestJson());
                List<String> gvodEndpoints = new ArrayList<>();
                for(Partner p : pd.getPartnerCollection()){
                    gvodEndpoints.add(p.getGvodUdpEndpoint());
                } 
                popularDatasetsJsons.add(new PopularDatasetJson(datasetStructureJson, pd.getDatasetId(), pd.getLeeches(), pd.getSeeds(), gvodEndpoints));

            }
            
            GenericEntity<List<PopularDatasetJson>> searchResults = new GenericEntity<List<PopularDatasetJson>>(popularDatasetsJsons) {};
            return Response.status(200).entity(searchResults).build();
        }

    }

    @POST
    @Path("populardatasets")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public void PopularDatasetsAdd(PopularDatasetJson popularDatasetsJson) {

        if (popularDatasetsJson.getIdentification().getClusterId() != null && helperFunctions.ClusterRegisteredWithId(popularDatasetsJson.getIdentification().getClusterId())) {

            DatasetStructure datasetStructure = new DatasetStructure();

            datasetStructure.setDatasetName(popularDatasetsJson.getStructure().getName());
            datasetStructure.setDatasetDescription(popularDatasetsJson.getStructure().getDescription());
            datasetStructure.setManifestJson(popularDatasetsJson.getStructure().getManifestJson());

            List<Partner> partners = new LinkedList<>();

            for (String s : popularDatasetsJson.getGvodEndpoints()) {
                Partner p = new Partner();
                p.setGvodUdpEndpoint(s);
            }
            
            PopularDataset popularDataset = new PopularDataset(popularDatasetsJson.getIdentification().getClusterId(), popularDatasetsJson.getLeeches(), popularDatasetsJson.getSeeds());
            
            popularDataset.setDatasetStructure(datasetStructure);
            popularDataset.setPartnerCollection(partners);
            
            this.popularDatasetsFacade.create(popularDataset);
        }

    }
}
