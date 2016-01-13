
package cz.hel.aos.service.webservice.artifacts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * wsgen -keep -s ~/temp -cp WEB-INF/classes/ cz.hel.aos.service.webservice.EmailServiceImpl
 * 
 */
@XmlRootElement(name = "sendEmailReservation", namespace = "http://webservice.service.aos.hel.cz/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendEmailReservation", namespace = "http://webservice.service.aos.hel.cz/", propOrder = {
    "arg0",
    "arg1"
})
public class SendEmailReservation {

    @XmlElement(name = "arg0", namespace = "")
    private String arg0;
    @XmlElement(name = "arg1", namespace = "")
    private cz.hel.aos.entity.Reservation arg1;

    /**
     * 
     * @return
     *     returns String
     */
    public String getArg0() {
        return this.arg0;
    }

    /**
     * 
     * @param arg0
     *     the value for the arg0 property
     */
    public void setArg0(String arg0) {
        this.arg0 = arg0;
    }

    /**
     * 
     * @return
     *     returns Reservation
     */
    public cz.hel.aos.entity.Reservation getArg1() {
        return this.arg1;
    }

    /**
     * 
     * @param arg1
     *     the value for the arg1 property
     */
    public void setArg1(cz.hel.aos.entity.Reservation arg1) {
        this.arg1 = arg1;
    }

}
