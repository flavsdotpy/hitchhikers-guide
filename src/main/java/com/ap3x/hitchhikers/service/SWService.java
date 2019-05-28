package com.ap3x.hitchhikers.service;

import com.ap3x.hitchhikers.model.PaginatedResponse;
import com.ap3x.hitchhikers.model.Planet;
import com.ap3x.hitchhikers.model.SWPlanet;

public interface SWService {

    public PaginatedResponse<Planet> listAll(Integer page);
    public SWPlanet getByName(String name);

}
