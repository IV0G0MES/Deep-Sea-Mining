package pt.isec.pa.deepsea.model.state.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.data.EstadoDrone;
import pt.isec.pa.deepsea.model.data.Jogo;
import pt.isec.pa.deepsea.model.data.Settings.Settings;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

import static org.junit.jupiter.api.Assertions.*;

class SuperficieStateTest {
    private Jogo jogo;
    private DeepSeaContext context;
    @BeforeEach
    void setUp() {
        context = new DeepSeaContext();
        jogo = context.getJogo();
    }

    @Test
    void testeMoveNavioSComb() {
        double comb = jogo.getNavio().getCombustivel();
        System.out.println("Combustivel: " + comb);
        jogo.getNavio().gastaCombustivel(comb - (Settings.CUSTO_COMB_NAV - 1));

        context.moveNavio(1, 0);
        System.out.println("Combustivel: " + jogo.getNavio().getCombustivel());
        assertEquals(DeepSeaState.GAME_OVER, context.getState());
    }

    @Test
    void testeDescida() {
        Drone drone = jogo.getNavio().getDrone(1);
        boolean res = context.iniciarDescida(1);
        assertTrue(res);
        assertEquals(DeepSeaState.DESCE_FOSSO, context.getState());
        assertEquals(EstadoDrone.FOSSO, drone.getEstado());
        assertEquals(0, drone.getPosX());
        assertEquals(Settings.FOSSO/2, drone.getPosY());
    }
}