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
        assertEquals(3, navio.getDronesOrdenadosCasco().size());
        for(Drone dro : navio.getDronesOrdenadosCasco()){
            assertNotNull(dro);
        }
    }


    @Test
    void testPosInit() {
        int posicaoinicial = SUPERFICIE/2-1;
        assertEquals(posicaoinicial, navio.getPosX());
        assertEquals(posicaoinicial, navio.getPosX());
    }

}