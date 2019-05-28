package com.ap3x.hitchhikers.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SWResponse {
    private Long count;
    private String next;
    private String previous;
    private List<SWPlanet> results;
}
