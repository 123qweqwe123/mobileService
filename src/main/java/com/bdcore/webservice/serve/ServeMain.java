package com.bdcore.webservice.serve;

import java.io.IOException;
import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Main class.
 *
 */
@Component
public class ServeMain {
	
	@Value("${server.ip}")
	private String serverIp;
	
	@Value("${server.port}")
	private String serverPort;
	
	@Value("${server.context}")
	private String serverContext;
	
	
    // Base URI the Grizzly HTTP server will listen on
    public String BASE_URI = null;

    
    private HttpServer server = null;
    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in $package package
        final ResourceConfig rc = new ResourceConfig().packages("com.bdcore.webservice");
        if(BASE_URI==null){
        	BASE_URI = "http://"+this.serverIp+":"+this.serverPort+"/"+this.serverContext+"/";
        }
        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        return server;
    }
    
    
    public void stopServer(){
           
            server.stop();
        
    }


}

