package pt.isec.pa.deepsea.model.state.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.Artefacto;
import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.data.Fundo;
import pt.isec.pa.deepsea.model.data.Jogo;
import pt.isec.pa.deepsea.model.data.Settings.Settings;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

import static org.junit.jupiter.api.Assertions.*;

class MiniJogoStateTest {
    private Jogo jogo;
    private DeepSeaContext context;

    @BeforeEach
    void setUp() {
        jogo = new Jogo();
        context = new DeepSeaContext();
        int idDrone = 1;
        while (!context.iniciarDescida(idDrone)){
            idDrone++;
        }

        context.getJogo().getNavio().getDroneFosso().setPosX(Settings.FOSSO - 1);
        context.moverDrone(1,0);
        
        Drone droneFundo =context.getJogo().getNavio().getDroneFundo();
        Fundo fundo =context.getJogo().getSuperficie().getElementoSuperficie()[jogo.getNavio().getPosX()][jogo.getNavio().getPosY()].getFundo();
        
        fundo.getFundo()[droneFundo.getPosX()][droneFundo.getPosY() + 1] = new Artefacto();
        context.moverDrone(0,1);
        context.apanhaObjeto();
        assertEquals(DeepSeaState.MINI_JOGO, context.getState());
    }

    @Test
    void testeJogadaInvalida() {
        boolean res = context.jogarPuzzle(-99);
        assertFalse(res);
        assertEquals(DeepSeaState.MINI_JOGO, context.getState());

    }
}