
package com.mycompany.customerrelations.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class was generated by the CXF 2.0-incubator
 * Sat Oct 23 21:42:10 CEST 2010
 * Generated source version: 2.0-incubator
 * 
 */

@XmlRootElement(name = "getCustomer", namespace = "http://customerrelations.mycompany.com/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCustomer", namespace = "http://customerrelations.mycompany.com/")

public class GetCustomer {

@XmlElement(name = "customerNummber", namespace = "")
    private java.lang.String customerNummber;

    public java.lang.String getCustomerNummber ()     {
	           return this.customerNummber;
        }

    public void setCustomerNummber (   java.lang.String newCustomerNummber  )     {
	           this.customerNummber = newCustomerNummber;
        }

}
