package be.pxl.services;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Hello world!
 *
 */

@SpringBootApplication
@EnableDiscoveryClient
public class NotificationServiceApplication
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
