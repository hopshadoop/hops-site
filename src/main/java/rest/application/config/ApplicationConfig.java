package rest.application.config;

import io.hops.site.rest.CommentService;
import io.hops.site.rest.PrivateClusterService;
import io.hops.site.rest.PrivateDatasetService;
import io.hops.site.rest.PublicClusterService;
import io.hops.site.rest.PublicDatasetService;
import io.hops.site.rest.RatingService;
import io.hops.site.rest.ReportCleanupService;
import io.hops.site.rest.ReportService;
import io.hops.site.rest.UserService;
import io.hops.site.rest.exception.mapper.AppExceptionMapper;
import io.hops.site.rest.exception.mapper.EJBExceptionMapper;
import io.hops.site.rest.request.filter.AuthFilter;
import io.hops.site.rest.response.filter.CORSFilter;
import io.hops.site.rest.response.filter.CacheControlFilter;
import io.swagger.annotations.Api;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.server.ResourceConfig;

@Api
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends ResourceConfig {

  public ApplicationConfig() {
    register(PrivateClusterService.class);
    register(PublicClusterService.class);
    register(PrivateDatasetService.class);
    register(PublicDatasetService.class);
    register(CommentService.class);
    register(RatingService.class);
    register(UserService.class);
    register(ReportService.class);
    register(ReportCleanupService.class);
    
    //Exception mappers
    register(EJBExceptionMapper.class);
    register(AppExceptionMapper.class);
    
    //response filters
    register(CORSFilter.class);
    register(CacheControlFilter.class);
    //request filters
    register(AuthFilter.class);
    
    //swagger
    register(ApiListingResource.class);
    register(SwaggerSerializers.class);
  }
}
