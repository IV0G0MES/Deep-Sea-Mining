package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static pt.isec.pa.deepsea.model.data.Settings.Settings.MAX_COMB_DRONE;

class DroneTest {
    private Drone drone;
    @BeforeEach
    void setUp() {
        drone = new Drone();
    }


    @Test
    void testeSetCombustivel() {
        drone.setCombustivel(-(MAX_COMB_DRONE /2)); // combustivel a 0
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