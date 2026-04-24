package org.jamup.test;

import org.jamup.bean.VenueBean;
import org.jamup.controller.ReserveVenueController;
import org.jamup.dao.factory.DAOFactory;
import org.jamup.exception.InvalidFieldException;
import org.jamup.exception.NoVenuesFoundException;
import org.jamup.model.enums.MusicGenre;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestReserveVenueController {

    private final ReserveVenueController controller = new ReserveVenueController();

    @BeforeClass
    public static void setup() {
        DAOFactory.getInstance("DEMO");
    }

    @Test
    public void testSearchValidGenreReturnsResults() throws InvalidFieldException {
        boolean hasResults;
        try {
            VenueBean searchBean = new VenueBean(null, null, List.of(MusicGenre.JAZZ));
            List<VenueBean> results = controller.search(searchBean);
            hasResults = !results.isEmpty();
        } catch (NoVenuesFoundException e) {
            hasResults = false;
        }
        
        assertTrue(hasResults);
    }

    @Test
    public void testSearchNonExistentNameThrowsException() throws InvalidFieldException {
        boolean exceptionThrown = false;
        try {
            VenueBean searchBean = new VenueBean("LocaleCheNonEsiste", null, null);
            controller.search(searchBean);
        } catch (NoVenuesFoundException e) {
            exceptionThrown = true;
        }
        
        assertTrue(exceptionThrown);
    }

    @Test
    public void testSearchNullBeanReturnsResults() {
        boolean hasResults;
        try {
            List<VenueBean> results = controller.search(null);
            hasResults = !results.isEmpty();
        } catch (NoVenuesFoundException e) {
            hasResults = false;
        }
        
        assertTrue(hasResults);
    }

}