package pt.isec.pa.deepsea.model.state.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.*;
import pt.isec.pa.deepsea.model.data.Settings.Settings;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.utils.DeepSeaLog;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SobeFossoStateTest {
    private DeepSeaContext context;
    private Jogo jogo;

    @BeforeEach
    void setUp() {
        jogo = new Jogo();
        context = new DeepSeaContext();

        int idDrone = 1;
        while (!context.iniciarDescida(idDrone)) {
            idDrone++;
        }

        Drone drone =context.getJogo().getNavio().getDroneFosso();
        drone.setPosX(Settings.FOSSO  - 1);
        context.moverDrone(1,0);

    }

    @Test
    void testTrasicaoSobeFosso() {
        boolean res = context.sobeFosso();
        assertTrue(res);
        assertEquals(DeepSeaState.SOBE_FOSSO, context.getState());
    }

    @Test
    void testTransicaoSuperficie() {
        context.sobeFosso();
        Drone d =context.getJogo().getNavio().getDroneFosso();
        d.setPosX(0);
        context.moverDrone(-1,0);

        assertEquals(DeepSeaState.SUPERFICIE, context.getState());
    }

    @Test
    void testeGetLog() {
        DeepSeaLog.getInstance().log("Log OG");
        List<String> copio = DeepSeaLog.getInstance().getLog();

    }
}