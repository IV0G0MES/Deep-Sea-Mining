package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static pt.isec.pa.deepsea.model.data.Settings.Settings.FOSSO;
import static pt.isec.pa.deepsea.model.data.Settings.Settings.SUPERFICIE;

class NavioTest {
    private Navio navio;
    @BeforeEach
    void setUp() {
        navio=new Navio();
    }

    @Test
    void testInit3Drones() {
        assertEquals(3, navio.getDrones().size());
        for(Drone dro : navio.getDrones()){
            assertNotNull(dro);
        }
    }

    @Test
    void testEntraFosso() {
        Drone drone = navio.getDrones().iterator().next(); //agarra 1 da lista
        System.out.println(drone);
        boolean sucesso = navio.EntraFosso(drone.getId());
        assertTrue(sucesso);
        assertEquals(0,drone.getPosX());
        assertEquals(FOSSO / 2, drone.getPosY());
    }

    @Test
    void testPosInit() {
        int posicaoinicial = SUPERFICIE/2-1;
        assertEquals(posicaoinicial, navio.getposX());
        assertEquals(posicaoinicial, navio.getposY());
    }

}