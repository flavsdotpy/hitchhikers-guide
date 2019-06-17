package com.ap3x.hitchhikers.service.impl;

import com.ap3x.hitchhikers.model.PaginatedResponse;
import com.ap3x.hitchhikers.model.Planet;
import com.ap3x.hitchhikers.model.SWPlanet;
import com.ap3x.hitchhikers.model.SWResponse;
import io.swagger.models.auth.In;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.ap3x.hitchhikers.HitchhikersGuideConstants.SWAPI_URL;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SWServiceImplTest {

    @Mock
    private ResponseEntity responseMock;

    @Mock
    private Environment env;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private SWServiceImpl swService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(env.getProperty("server.host")).thenReturn("localhost");
        when(env.getProperty("server.port")).thenReturn("8080");
    }

    @Test
    public void listAll() {
        final String url = SWAPI_URL + "?page=2";
        final SWResponse response = new SWResponse();
        response.setCount(10L);
        response.setNext("next");
        response.setPrevious("previous");
        response.setResults(
                IntStream.range(1, 11).mapToObj(SWServiceImplTest::getSWPlanet).collect(Collectors.toList())
        );

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(SWResponse.class)))
                .thenReturn(responseMock);
        when(responseMock.getBody()).thenReturn(response);

        final PaginatedResponse<Planet> paginatedResponse = swService.listAll(2);

        assertEquals(10, paginatedResponse.getData().size());
        assertEquals("http://localhost:8080/api/v1/planets?sw=true&page=1", paginatedResponse.getPreviousPage());
        assertEquals("http://localhost:8080/api/v1/planets?sw=true&page=3", paginatedResponse.getNextPage());

        verify(restTemplate).exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(SWResponse.class));
        verify(responseMock).getBody();
    }

    @Test
    public void listAll_null() {
        final String url = SWAPI_URL + "?page=1";

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(SWResponse.class)))
                .thenReturn(responseMock);
        when(responseMock.getBody()).thenReturn(null);

        final PaginatedResponse<Planet> paginatedResponse = swService.listAll(1);

        assertNull(paginatedResponse.getData());

        verify(restTemplate).exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(SWResponse.class));
        verify(responseMock).getBody();
    }

    @Test
    public void getByName() {
        final String url = SWAPI_URL + "?search=test";
        final SWResponse response = new SWResponse();
        response.setResults(IntStream.range(1, 2).mapToObj(SWServiceImplTest::getSWPlanet).collect(Collectors.toList()));
        response.setCount(1L);

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(SWResponse.class)))
                .thenReturn(responseMock);
        when(responseMock.getBody()).thenReturn(response);

        final SWPlanet planet = swService.getByName("test");

        assertEquals("Planet 1", planet.getName());

        verify(restTemplate).exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(SWResponse.class));
        verify(responseMock).getBody();
    }

    @Test
    public void getByName_notFound() {
        final String url = SWAPI_URL + "?search=test";

        when(restTemplate.exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(SWResponse.class)))
                .thenReturn(responseMock);
        when(responseMock.getBody()).thenReturn(null);

        final SWPlanet planet = swService.getByName("test");
        assertNull(planet);

        verify(restTemplate).exchange(eq(url), eq(HttpMethod.GET), any(HttpEntity.class), eq(SWResponse.class));
        verify(responseMock).getBody();
    }

    private static SWPlanet getSWPlanet(int i) {
        final SWPlanet swPlanet = new SWPlanet();
        swPlanet.setName(String.format("Planet %d", i));
        swPlanet.setClimate(String.format("Climate %d", i));
        swPlanet.setTerrain(String.format("Terrain %d", i));
        swPlanet.setFilms(
                IntStream.range(1, i+1).mapToObj(n -> String.format("Planet %d", n)).collect(Collectors.toList())
        );
        return swPlanet;
    }
}