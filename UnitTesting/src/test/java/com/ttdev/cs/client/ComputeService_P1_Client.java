
package com.ttdev.cs.client;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.2.5
 * Mon Dec 28 13:07:02 HKT 2009
 * Generated source version: 2.2.5
 * 
 */

public final class ComputeService_P1_Client {

    private static final QName SERVICE_NAME = new QName("http://ttdev.com/cs", "ComputeService");

    private ComputeService_P1_Client() {
    }

    public static void main(String args[]) throws Exception {
        URL wsdlURL = ComputeService_Service.WSDL_LOCATION;
        if (args.length > 0) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        ComputeService_Service ss = new ComputeService_Service(wsdlURL, SERVICE_NAME);
        ComputeService port = ss.getP1();  
        
        {
        System.out.println("Invoking compute...");
        java.lang.String _compute_parameters = "";
        java.lang.String _compute__return = port.compute(_compute_parameters);
        System.out.println("compute.result=" + _compute__return);


        }

        System.exit(0);
    }

}