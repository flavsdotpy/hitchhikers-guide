package com.ap3x.hitchhikers.controllers;

import com.ap3x.hitchhikers.models.Planet;
import com.ap3x.hitchhikers.models.ResponseBody;
import com.ap3x.hitchhikers.services.StarWarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/planets")
public class PlanetController {

    @Autowired
    private StarWarsService swService;

    @GetMapping()
    public ResponseEntity<ResponseBody<Planet>> getAPIplanets(
            @RequestParam(defaultValue = "false") Boolean sw,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam String name) {
        if (sw)
            return listFromSW(page);
        else {
            return listFromBase(page, name);
        }
    }

    private ResponseEntity<ResponseBody<Planet>> listFromSW(Integer page) {
        ResponseBody<Planet> body = swService.listAll(page);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    private ResponseEntity<ResponseBody<Planet>> listFromBase(Integer page, String name) {
        ResponseBody<Planet> body = swService.listAll(page);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }


    private ResponseBody<Planet> getResponseBody(){
        return null;
    }
}
