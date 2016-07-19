/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package site.hops.io.popularDatasets;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jsvhqr
 */
@XmlRootElement
public class ManifestJson {
    
    private List<FileInfo> fileInfos;
    
    private List<String> metaDataJsons;

    public ManifestJson() {
    }

    public ManifestJson(List<FileInfo> fileInfos, List<String> metaDataJsons) {
        this.fileInfos = fileInfos;
        this.metaDataJsons = metaDataJsons;
    }
    

    public List<FileInfo> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<FileInfo> fileInfos) {
        this.fileInfos = fileInfos;
    }

    public List<String> getMetaDataJsons() {
        return metaDataJsons;
    }

    public void setMetaDataJsons(List<String> metaDataJsons) {
        this.metaDataJsons = metaDataJsons;
    }
    
    
    
    
    
    
}
