package com.github.ScipioAM.scipio_utils_net.api_lib.virus_total.bean;

import java.util.Map;

/**
 * VirusTotal的顶层javaBean
 * @author Alan Scipio
 * @since 2021/7/14
 */
public class VTData {

    private String type;

    private String id;

    private Map<String,String> links;

    private Map<String,String> attributes;

    private Map<String,VTData> relationships;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public Map<String, VTData> getRelationships() {
        return relationships;
    }

    public void setRelationships(Map<String, VTData> relationships) {
        this.relationships = relationships;
    }

    @Override
    public String toString() {
        return "VTData{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", links=" + links +
                ", attributes=" + attributes +
                ", relationships=" + relationships +
                '}';
    }
}
