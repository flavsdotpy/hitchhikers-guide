package com.ap3x.hitchhikers.config;

import org.junit.Before;
import org.junit.Test;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static org.junit.Assert.*;

public class SwaggerConfigTest {

    private SwaggerConfig config;

    @Before
    public void setUp() {
        config = new SwaggerConfig();
    }

    @Test
    public void api() {
        final Docket docket = config.api();

        assertEquals(DocumentationType.SWAGGER_2, docket.getDocumentationType());
    }
}