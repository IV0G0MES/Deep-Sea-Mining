package pt.isec.pa.deepsea.model.state.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pt.isec.pa.deepsea.model.data.*;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

import static org.junit.jupiter.api.Assertions.*;
import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;

class DesceFossoStateTest {
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

        assertEquals(DeepSeaState.DESCE_FOSSO, context.getState(), "A FSM devia ter conseguido lançar um drone e ir para o FOSSO.");
    }
    @Test
    void testeDeTransicaoFundo() {
        Drone drone = context.getJogo().getNavio().getDroneFosso();
        drone.setPosX(FOSSO);
        boolean res = context.moverDrone(1, 0);
        assertTrue(res);
        assertEquals(DeepSeaState.FUNDO_MAR, context.getState());
        assertEquals(EstadoDrone.FUNDO, drone.getEstado());
    }

    @Test
    void testeBaterRochaGeraDano() {
        Drone drone = context.getJogo().getNavio().getDroneFosso();
        double inicioCasco = drone.getCasco();

        System.out.println("integridade casco antes = " + inicioCasco);

        Fosso fosso = context.getJogo().getSuperficie().getElementoSuperficie()[jogo.getNavio().getPosX()][jogo.getNavio().getPosY()].getFosso();
        fosso.getFosso()[drone.getPosX()][drone.getPosY() + 1] = new Rocha(drone.getPosX(), drone.getPosY() + 1);

        boolean res = context.moverDrone(0, 1);
        System.out.println("integridade casco dps = " + drone.getCasco());

        assertTrue(res);
        assertTrue(drone.getCasco() < inicioCasco);
    }

    @Test
    void testeCorrenteDobroComb() {
        Drone drone = context.getJogo().getNavio().getDroneFosso();
        double inicioComb = drone.getCombustivel();
        System.out.println("combustivel antes =" + inicioComb);

        Fosso fosso =context.getJogo().getSuperficie().getElementoSuperficie()[jogo.getNavio().getPosX()][jogo.getNavio().getPosY()].getFosso();
        fosso.getFosso()[drone.getPosX()][drone.getPosY() + 1] = new CorrenteMaritima(drone.getPosX(), drone.getPosY() + 1);
        boolean res = context.moverDrone(0, 1);
        double combEspeado = inicioComb - (CUSTO_COMB_DRONE *2);
        System.out.println("combustivel dps =" +combEspeado);

        assertTrue(res);
        assertEquals(drone.getCombustivel(), combEspeado);
    }

    @Test
    void testeDestruicaoMudaEstado() {
        Drone drone = context.getJogo().getNavio().getDroneFosso();

        drone.gastaCombustivel(drone.getCombustivel());

        context.moverDrone(0, 1);

        assertEquals(0, drone.getCasco());
        assertEquals(EstadoDrone.DESTRUIDO, drone.getEstado());
        assertEquals(DeepSeaState.SUPERFICIE, context.getState());
    }
}