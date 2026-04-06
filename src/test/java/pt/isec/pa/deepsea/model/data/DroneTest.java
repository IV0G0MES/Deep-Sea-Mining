package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static pt.isec.pa.deepsea.model.data.Settings.Settings.MaxCombDrone;

class DroneTest {
    private Drone drone;
    @BeforeEach
    void setUp() {
        drone = new Drone();
    }

    @Test
    void testrecolheitem() {
        assertEquals(0, drone.getInventario().size());

        drone.apanhaItem(new Artefacto(3, 3));
        drone.apanhaItem(new Minerio(1, 4, 4));
        assertEquals(2, drone.getInventario().size());
        drone.recolheItems();

        assertEquals(0, drone.getInventario().size());
    }
    @Test
    void testeSetCombustivel() {
        drone.setCombustivel(-(MaxCombDrone/2)); // combustivel a 0
        drone.setCombustivel(10);
        assertEquals(60, drone.getCombustivel());
    }

    @Test
    void testSetCasco() {
        drone.setCasco(-50);
        assertEquals(0, drone.getCasco());

        drone.setCasco(150);
        assertEquals(100, drone.getCasco());

        drone.setCasco(75);
        assertEquals(75, drone.getCasco());
    }
}