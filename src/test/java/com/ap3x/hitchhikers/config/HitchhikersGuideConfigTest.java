package com.ap3x.hitchhikers.config;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

import static org.junit.Assert.*;

public class HitchhikersGuideConfigTest {

    private HitchhikersGuideConfig config;

    @Before
    public void setUp() {
        config = new HitchhikersGuideConfig();
    }

    @Test
    public void restTemplate() {
        assertNotNull(config.restTemplate(new RestTemplateBuilder()));
    }
}