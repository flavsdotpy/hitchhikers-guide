package com.ap3x.hitchhikers.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanetDTO {

    @NotNull
    @Column(unique = true)
    private String name;
    @NotNull
    private String climate;
    @NotNull
    private String terrain;

}
