package rest.application.config;

import io.hops.site.rest.CommentService;
import io.hops.site.rest.DatasetService;
import io.hops.site.rest.PopularDatasetsService;
import io.hops.site.rest.RatingService;
import org.glassfish.jersey.server.ResourceConfig;
import io.hops.site.rest.RegisterAndPingService;
import io.swagger.annotations.Api;

@Api
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends ResourceConfig {

  public ApplicationConfig() {
    register(RegisterAndPingService.class);
    register(PopularDatasetsService.class);
    register(DatasetService.class);
    register(CommentService.class);
    register(RatingService.class);
    
    //swagger
    register(io.swagger.jaxrs.listing.ApiListingResource.class);
    register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
  }
}
