package pt.isec.pa.deepsea.model.state.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.model.data.Jogo;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

import static org.junit.jupiter.api.Assertions.*;

class GameOverStateTest {
    private Jogo jogo;
    private DeepSeaContext context;

    @BeforeEach
    void setUp() {
        jogo = new Jogo();
        context = new DeepSeaContext();

    }

    @Test
    void testeTransicaoGameOver() {
        GameOverState estadoTerminal = new GameOverState(context, jogo);

        assertFalse(estadoTerminal.sobeFosso());
        assertFalse(estadoTerminal.moverDrone(1, 0));
        assertFalse(estadoTerminal.iniciarDescida(1));

        assertEquals(DeepSeaState.GAME_OVER, estadoTerminal.getState());
    }
}