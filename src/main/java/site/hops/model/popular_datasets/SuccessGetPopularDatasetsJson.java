/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.model.popular_datasets;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import site.hops.entities.PopularDatasets;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class SuccessGetPopularDatasetsJson {
    
    private List<PopularDatasets> popularDatasets;

    public SuccessGetPopularDatasetsJson() {
    }

    public SuccessGetPopularDatasetsJson(List<PopularDatasets> popularDatasets) {
        this.popularDatasets = popularDatasets;
    }

    @XmlElement(name = "popular_datasets")
    public List<PopularDatasets> getPopularDatasets() {
        return popularDatasets;
    }

    public void setPopularDatasets(List<PopularDatasets> popularDatasets) {
        this.popularDatasets = popularDatasets;
    }
    
    
    
}
