/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest.application.config;

import org.glassfish.jersey.server.ResourceConfig;
import site.hops.rest.ClusterService;

/**
 *
 * @author jsvhqr
 */
@javax.ws.rs.ApplicationPath("webapi")
public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig() {
        register(ClusterService.class);
    }
}
