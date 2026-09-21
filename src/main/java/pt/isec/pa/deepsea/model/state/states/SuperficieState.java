package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.data.Drone;
import pt.isec.pa.deepsea.model.data.EstadoDrone;
import pt.isec.pa.deepsea.model.data.Jogo;
import pt.isec.pa.deepsea.model.data.Navio;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;
import pt.isec.pa.deepsea.model.data.Oficina;
import pt.isec.pa.deepsea.model.utils.DeepSeaLog;
import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;


/**
 * Classe que representa o estado principal de gestão na Superfície.
 * <p>
 * Este estado atua como o centro de operações do jogo. Aqui, o jogador pode
 * movimentar o Navio pelo mapa, gerir os recursos extraídos e interagir com a Oficina
 * para reparar, reabastecer e melhorar os drones. É a partir deste estado que se
 * inicia a exploração, efetuando a transição para o {@link DeepSeaState#DESCE_FOSSO}.
 * Caso o navio fique sem combustível durante as viagens, passa para o estado de Game Over.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class SuperficieState extends DeepSeaStateAdapter {

    private static final long serialVersionUID = 1L;

    public SuperficieState(DeepSeaContext context, Jogo data){
        super(context, data);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.SUPERFICIE;
    }

    @Override
    public boolean moveNavio(int direcaoX, int direcaoY) {
        Navio navio = data.getNavio();
        int nPosX = navio.getPosX() + direcaoX;
        int nPosY = navio.getPosY() + direcaoY;
        if (nPosX < 0 || nPosX >= SUPERFICIE || nPosY < 0 || nPosY >= SUPERFICIE) {
            return false;
        }
        navio.setPosX(nPosX);
        navio.setPosY(nPosY);
        if(!navio.gastaCombustivel(CUSTO_COMB_NAV)) {
            DeepSeaLog.getInstance().log("[SUPERFICIE] O Navio ficou sem Combustivel :( ");
            changeState(DeepSeaState.GAME_OVER);
        }
        DeepSeaLog.getInstance().log("[NAVIO] Moveu-se para a posição [" + nPosX + ", " + nPosY + "]");
        return true;
    }

    @Override
    public boolean abastecerDrone(int idDrone, double qnt) {
        Navio navio = data.getNavio();
        Oficina oficina = navio.getOficina();
        return oficina.abastecerDrone(idDrone,qnt);
    }

    @Override
    public boolean iniciarDescida(int idDrone) {
        Drone drone = data.getNavio().getDrone(idDrone);
        if (drone == null || drone.getCasco() <= 0 || drone.getCombustivel() <= 0) {
            return false;
        }
        drone.setEstado(EstadoDrone.FOSSO);
        drone.setPosX(0);
        drone.setPosY(FOSSO / 2);
        drone.resetDanoCasco();
        changeState(DeepSeaState.DESCE_FOSSO);
        DeepSeaLog.getInstance().log("[NAVIO] - A Enviar o Drone " + drone.getId() + "para as profundezas");
        return true;
    }

    @Override
    public boolean reparaDrone(int idDrone, double qntCascoRecuperar) {
        return data.getNavio().getOficina().reparaCasco(idDrone, qntCascoRecuperar);
    }

    @Override
    public boolean melhoraDrone(int idDrone, int tipoUpgrade) {
        Oficina oficina = data.getNavio().getOficina();
        if (!oficina.selecionarDrone(idDrone)) {
            return false;
        }
        if (tipoUpgrade == 1) {
            return oficina.melhorarCapacidadeCombustivel(idDrone);
        } else if (tipoUpgrade == 2) {
            return oficina.melhorarCasco(idDrone);
        }
        return false;
    }
}
