package pt.isec.pa.deepsea.model.state.states;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.*;
import pt.isec.pa.deepsea.model.data.Settings.Settings;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;

import static org.junit.jupiter.api.Assertions.*;
import static pt.isec.pa.deepsea.model.data.Settings.Settings.DANO_MONSTRO;

class FundoMarStateTest {
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

                assertEquals(DeepSeaState.FUNDO_MAR, context.getState());
        }

        @Test
        void testeTransicaoParaSobreFosso() {
                Drone drone =context.getJogo().getNavio().getDroneFundo();
                boolean sucesso = context.sobeFosso();
                assertTrue(sucesso);
                assertEquals(DeepSeaState.SOBE_FOSSO, context.getState());
                assertEquals(EstadoDrone.FOSSO, drone.getEstado());

        }

        @Test
        void testeMostroTiraVida() {
                Drone drone =context.getJogo().getNavio().getDroneFundo();
                double incioCasco = drone.getCasco();

                Fundo fundo =context.getJogo().getSuperficie().getElementoSuperficie()[jogo.getNavio().getPosX()][jogo.getNavio().getPosY()].getFundo();
                fundo.getFundo()[drone.getPosX()][drone.getPosY() + 1] = new Monstro(drone.getPosX(), drone.getPosY() + 1);

                context.moverDrone(0, 1);

                assertEquals(incioCasco - DANO_MONSTRO, drone.getCasco());
        }
        @Test
        void testeGuardaIventario() {
                Drone drone =context.getJogo().getNavio().getDroneFundo();
                int inicioInventario = drone.getInventario().size();

                Fundo fundo =context.getJogo().getSuperficie().getElementoSuperficie()[jogo.getNavio().getPosX()][jogo.getNavio().getPosY()].getFundo();
                fundo.getFundo()[drone.getPosX()][drone.getPosY() + 1] = new Minerio(1);
                context.moverDrone(0, 1);
                context.apanhaObjeto();

                assertEquals(inicioInventario + 1,  drone.getInventario().size());
        }

        @Test
        void testeTransicaoMiniJogo() {
                Drone drone =context.getJogo().getNavio().getDroneFundo();
                Fundo fundo =context.getJogo().getSuperficie().getElementoSuperficie()[jogo.getNavio().getPosX()][jogo.getNavio().getPosY()].getFundo();
                fundo.getFundo()[drone.getPosX()][drone.getPosY() + 1] = new Artefacto();

                context.moverDrone(0, 1);
                context.apanhaObjeto();

                assertEquals(DeepSeaState.MINI_JOGO, context.getState());
        }

}