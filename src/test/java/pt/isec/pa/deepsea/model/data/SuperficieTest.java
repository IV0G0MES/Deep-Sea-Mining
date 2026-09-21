package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;

class SuperficieTest {
    private  Superficie superficie;
    @BeforeEach
    void setUp() {
        superficie = new Superficie();
    }

    @Test
    void testNArtefacto() {
        int aux = 0;
        superficie.sorteiaArtefacto();
        for(int i = 0; i < SUPERFICIE; i++){
            for(int j = 0; j < SUPERFICIE; j++){
                if(superficie.getElementoSuperficie()[i][j].getFundo().getElementoFundo(i,j) instanceof Artefacto)aux++;
            }
        }
        assertTrue(aux < N_MAX_ARTEFACTOS);
    }
    @Test
    void testLimMap() {
        assertFalse(superficie.eArtefacto(-1, 5));
        assertFalse(superficie.eArtefacto(5, -1));

        assertFalse(superficie.eArtefacto(SUPERFICIE, 5));
        assertFalse(superficie.eArtefacto(5, SUPERFICIE));
    }



}