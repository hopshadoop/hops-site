/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.model.popular_datasets;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class FailureGetPopularDatasetsJson {
    
    private String details;

    public FailureGetPopularDatasetsJson(String details) {
        this.details = details;
    }

    public FailureGetPopularDatasetsJson() {
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
    
    
    
}
