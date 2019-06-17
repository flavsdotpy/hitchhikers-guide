package com.ap3x.hitchhikers.controller;

import com.ap3x.hitchhikers.model.HitchhikersGuideError;
import javassist.NotFoundException;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityExistsException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

public class ExceptionControllerTest {

    private ExceptionController controller;

    @Before
    public void setUp() {
        controller = new ExceptionController();
    }

    @Test
    public void handleGenericException() {
        final Exception ex = new RuntimeException("test");

        final HitchhikersGuideError error = controller.handleGenericException(ex);

        assertEquals("test", error.getMessage());
        assertEquals(LocalDateTime.now().getDayOfYear(), error.getTimestamp().getDayOfYear());
    }

    @Test
    public void handleNotFoundException() {
        final NotFoundException ex = new NotFoundException("notfound");

        final HitchhikersGuideError error = controller.handleNotFoundException(ex);

        assertEquals("notfound", error.getMessage());
        assertEquals(LocalDateTime.now().getDayOfYear(), error.getTimestamp().getDayOfYear());
    }

    @Test
    public void handleEntityExistsException() {
        final EntityExistsException ex = new EntityExistsException("entityexists");

        final HitchhikersGuideError error = controller.handleEntityExistsException(ex);

        assertEquals("entityexists", error.getMessage());
        assertEquals(LocalDateTime.now().getDayOfYear(), error.getTimestamp().getDayOfYear());
    }
}