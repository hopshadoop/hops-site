package rest.application.config;

import io.hops.site.rest.CommentService;
import io.hops.site.rest.DatasetService;
import io.hops.site.rest.PopularDatasetsService;
import io.hops.site.rest.RatingService;
import org.glassfish.jersey.server.ResourceConfig;
import io.hops.site.rest.RegisterAndPingService;

@javax.ws.rs.ApplicationPath("webapi")
public class ApplicationConfig extends ResourceConfig {

  public ApplicationConfig() {
    register(RegisterAndPingService.class);
    register(PopularDatasetsService.class);
    register(DatasetService.class);
    register(CommentService.class);
    register(RatingService.class);
  }
}
