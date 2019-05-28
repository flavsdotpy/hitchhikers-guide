package com.ap3x.hitchhikers.service;

import com.ap3x.hitchhikers.dto.PlanetDTO;
import com.ap3x.hitchhikers.model.PaginatedResponse;
import com.ap3x.hitchhikers.model.Planet;
import javassist.NotFoundException;

public interface PlanetService {

    public PaginatedResponse<Planet> listAll(Integer page, String name);
    public Planet getById(Integer id) throws NotFoundException;
    public Planet create(PlanetDTO planetDTO);
    public Planet update(Integer id, PlanetDTO planetDTO) throws NotFoundException;
    public void delete(Integer id) throws NotFoundException;

}
