package rest.application.config;

import io.hops.site.admin.rest.AdminClusterService;
import io.hops.site.admin.rest.AdminDatasetService;
import io.hops.site.rest.ClusterService;
import io.hops.site.rest.CommentService;
import io.hops.site.rest.DatasetService;
import io.hops.site.rest.RatingService;
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
    register(ClusterService.class);
    register(DatasetService.class);
    register(CommentService.class);
    register(RatingService.class);
    register(UserService.class);
    
    //Admin
    register(AdminClusterService.class);
    register(AdminDatasetService.class);
    
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
