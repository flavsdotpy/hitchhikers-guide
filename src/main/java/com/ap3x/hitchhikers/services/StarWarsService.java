package com.ap3x.hitchhikers.services;

import com.ap3x.hitchhikers.models.Planet;
import com.ap3x.hitchhikers.models.ResponseBody;
import com.ap3x.hitchhikers.models.SWPlanet;
import com.ap3x.hitchhikers.models.SWResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.ap3x.hitchhikers.HitchhikersConstants.SWAPI_URL;



@Service
public class StarWarsService {

    @Autowired
    private Environment env;

    public ResponseBody<Planet> listAll(Integer page){
        String url = SWAPI_URL;
        if (page != null)
            url = url + "?page=" + page;

        RestTemplate restTemplate = new RestTemplate();
        SWResponse response = restTemplate.getForObject(url, SWResponse.class);

        ResponseBody<Planet> responseBody = new ResponseBody<>();
        if (response.getNext() != null)
            responseBody.setNextPage(
                    "http://" + env.getProperty("host") + "/api/v1/planets?sw=true&page=" + (page + 1));
        if (response.getPrevious() != null)
            responseBody.setPreviousPage(
                    "http://" + env.getProperty("host") + "/api/v1/planets?sw=true&page=" + (page - 1));
        List<Planet> planets = new ArrayList<>();
        for (SWPlanet swPlanet : response.getResults()) {
            Planet planet = new Planet();
            planet.setName(swPlanet.getName());
            planet.setClimate(swPlanet.getClimate());
            planet.setTerrain(swPlanet.getTerrain());
            planet.setNumberOfFilmsApparitions(swPlanet.getFilms().size());
            planets.add(planet);
        }
        responseBody.setData(planets);

        return responseBody;
    }
}
