package com.ap3x.hitchhikers.service.impl;

import com.ap3x.hitchhikers.model.PaginatedResponse;
import com.ap3x.hitchhikers.model.Planet;
import com.ap3x.hitchhikers.model.SWPlanet;
import com.ap3x.hitchhikers.model.SWResponse;
import com.ap3x.hitchhikers.service.SWService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.ap3x.hitchhikers.HitchhikersGuideConstants.SWAPI_URL;



@Service
public class SWServiceImpl implements SWService {

    @Autowired
    private Environment env;

    @Autowired
    private RestTemplate restTemplate;

    public PaginatedResponse<Planet> listAll(Integer page){
        final String url = SWAPI_URL + "?page=" + page;

        final SWResponse response = restTemplate
                                .exchange(url, HttpMethod.GET, getHeaders(), SWResponse.class)
                                .getBody();

        final PaginatedResponse<Planet> paginatedResponse = new PaginatedResponse<>();
        if (response != null) {
            paginatedResponse.setCount(response.getCount());
            if (response.getNext() != null) {
                paginatedResponse.setNextPage(
                        String.format("http://%s:%s/api/v1/planets?sw=true&page=%d",
                                env.getProperty("server.host"), env.getProperty("server.port"), page + 1));
            }

            if (response.getPrevious() != null) {
                paginatedResponse.setPreviousPage(
                        String.format("http://%s:%s/api/v1/planets?sw=true&page=%d",
                                env.getProperty("server.host"), env.getProperty("server.port"), page - 1));
            }

            final List<Planet> planets = response.getResults().stream().map(swPlanet -> {
                Planet planet = new Planet();
                planet.setName(swPlanet.getName());
                planet.setClimate(swPlanet.getClimate());
                planet.setTerrain(swPlanet.getTerrain());
                planet.setNumberOfFilmsApparitions(swPlanet.getFilms().size());
                return planet;
            }).collect(Collectors.toList());
            paginatedResponse.setData(planets);
        }
        return paginatedResponse;
    }

    public SWPlanet getByName(String name) {
        final String url = SWAPI_URL + "?search=" + name;

        final SWResponse response = restTemplate
                .exchange(url, HttpMethod.GET, getHeaders(), SWResponse.class)
                .getBody();

        if (response != null && response.getCount() == 1) {
            return response.getResults().get(0);
        }
        return null;
    }

    private HttpEntity<String> getHeaders(){
        final HttpHeaders headers = new HttpHeaders();

        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user-agent", "Mozilla/5.0 Firefox/26.0");
        return new HttpEntity<>("parameters", headers);
    }
}
