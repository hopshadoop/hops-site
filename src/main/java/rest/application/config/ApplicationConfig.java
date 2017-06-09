package rest.application.config;

import org.glassfish.jersey.server.ResourceConfig;
import io.hops.site.rest.RegisterAndPingService;

@javax.ws.rs.ApplicationPath("webapi")
public class ApplicationConfig extends ResourceConfig {

  public ApplicationConfig() {
    register(RegisterAndPingService.class);
  }
}
