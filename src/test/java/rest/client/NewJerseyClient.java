package rest.client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Scanner;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import org.eclipse.persistence.oxm.MediaType;

/**
 * Jersey REST client generated for REST resource:ClusterService
 * [myresource]<br>
 * USAGE:
 * <pre>
 *        NewJerseyClient client = new NewJerseyClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author gnomer_archer
 */
public class NewJerseyClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://bbc1.sics.se:14003/hops-site/webresources";

    public NewJerseyClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("myresource");
    }

    public String getIt() throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.TEXT_PLAIN).get(String.class);
    }

    public <T> T Ping(Class<T> responseType, String name, String restEndpoint, String email, String cert, String udpEndpoint) throws ClientErrorException {
        WebTarget resource = webTarget;
        restEndpoint = restEndpoint.replaceAll("/", "'");
        udpEndpoint = udpEndpoint.replaceAll("/", "'");
        resource = resource.path(java.text.MessageFormat.format("ping/{0}/{1}/{2}/{3}/{4}", new Object[]{name, restEndpoint, email, cert, udpEndpoint}));
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void close() {
        client.close();
    }
    
    public static void main(String [] args){
        
        NewJerseyClient jc = new NewJerseyClient();
        Scanner scan = new Scanner(System.in);
        String cluster_name, rest_endpoint, email, cert, udp_endpoint;
        System.out.println("Enter cluster name");
        cluster_name = scan.nextLine();
        System.out.println("Enter rest endpoint");
        rest_endpoint = scan.nextLine();
        System.out.println("Enter email");
        email = scan.nextLine();
        System.out.println("Enter cert");
        cert = scan.nextLine();
        System.out.println("Enter udp endpoint");
        udp_endpoint = scan.nextLine();
        
        System.out.println(jc.getIt());
        
        String ret = jc.Ping(String.class, cluster_name, rest_endpoint, email, cert, udp_endpoint);
        
        System.out.println(ret);
        
    }
    
}
