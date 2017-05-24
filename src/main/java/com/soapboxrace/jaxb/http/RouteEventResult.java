
package com.soapboxrace.jaxb.http;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RouteEventResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RouteEventResult">
 *   &lt;complexContent>
 *     &lt;extension base="{}EventResult">
 *       &lt;sequence>
 *         &lt;element name="Entrants" type="{}ArrayOfRouteEntrantResult" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RouteEventResult", propOrder = {
    "entrants"
})
public class RouteEventResult
    extends EventResult
{

    @XmlElement(name = "Entrants")
    protected ArrayOfRouteEntrantResult entrants;

    /**
     * Gets the value of the entrants property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfRouteEntrantResult }
     *     
     */
    public ArrayOfRouteEntrantResult getEntrants() {
        return entrants;
    }

    /**
     * Sets the value of the entrants property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfRouteEntrantResult }
     *     
     */
    public void setEntrants(ArrayOfRouteEntrantResult value) {
        this.entrants = value;
    }

}
