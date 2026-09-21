package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.data.*;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;
import pt.isec.pa.deepsea.model.utils.DeepSeaLog;
import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;


/**
 * Classe que representa o estado de descida pelo Fosso na Máquina de Estados.
 * <p>
 * Este estado gere a navegação vertical do drone a caminho do Fundo do Mar.
 * Implementa as mecânicas de colisão com rochas, encontros com animais marinhos
 * e perturbações por correntes marítimas, além de gerir o consumo de combustível
 * e os danos no casco. Conclui com a transição para o {@link DeepSeaState#FUNDO_MAR}
 * ao atingir a profundidade máxima.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class DesceFossoState extends DeepSeaStateAdapter {
    private static final long serialVersionUID = 1L;

    public DesceFossoState(DeepSeaContext context, Jogo data){
        super(context, data);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.DESCE_FOSSO;
    }

    @Override
    public boolean moverDrone(int direcaoX, int direcaoY) {
        Drone drone = data.getNavio().getDroneFosso();
        if (drone == null) return false;
        int PosNavioX = data.getNavio().getPosX();
        int PosNavioY = data.getNavio().getPosY();
        Fosso fosso = data.getSuperficie().getElementoSuperficie()[PosNavioX][PosNavioY].getFosso();
        int NPosDroneX = drone.getPosX()+direcaoX;
        int NPosDroneY = drone.getPosY()+direcaoY;
        Fundo fundo= data.getSuperficie().getElementoSuperficie()[PosNavioX][PosNavioY].getFundo();

        if (NPosDroneX >= FOSSO) {
            DeepSeaLog.getInstance().log("[DRONE]: Drone a Entrar no Fundo do Oceano");
            drone.setPosX(FUNDO/2);
            drone.setPosY(FUNDO/2);
            drone.setEstado(EstadoDrone.FUNDO);
            fundo.limpaFundo();
            fundo.geraElementosFundo();
            changeState(DeepSeaState.FUNDO_MAR);
            return true;
        }
        else if(NPosDroneX<0){
            drone.setEstado(EstadoDrone.REPOUSO);
            changeState(DeepSeaState.SUPERFICIE);
            return true;
        }
        if (NPosDroneY < 0 || NPosDroneY >= FOSSO || NPosDroneX < 0) {
            DeepSeaLog.getInstance().log("[SISTEMA]: Drone atingiu os Limites máximos do Fosso");
            return false;
        }
        ElementoFosso novoElementoFosso = fosso.getElementoFosso(NPosDroneX, NPosDroneY);
        double custoCombustivel = CUSTO_COMB_DRONE;
        boolean bateuRocha=false;
        if (novoElementoFosso instanceof CorrenteMaritima) {
            DeepSeaLog.getInstance().log("[FOSSO] O Drone n-" + drone.getId() + " foi Apanhado numa corrente");
            custoCombustivel *= 2;
        } else if (novoElementoFosso instanceof AnimalMarinho) {
            DeepSeaLog.getInstance().log("[FOSSO] O Drone n-" + drone.getId() + " foi atacado por um Animal Marinho Faminto");
            drone.danificaCasco();
        }else if(novoElementoFosso instanceof Rocha){
            DeepSeaLog.getInstance().log("[FOSSO] O Drone n-" + drone.getId() + " foi contra Rochas");
            drone.danificaCasco();
            bateuRocha=true;
        }
        if(!drone.gastaCombustivel(custoCombustivel) || drone.getCasco() <= 0) {
            String causa = (drone.getCasco() <= 0) ? "O Drone nao resistiu aos danos no seu casco" : "Falta De Combustivel no Drone MAYDAY";
            destroiDrone();
            DeepSeaLog.getInstance().log("[DRONE-CRITICO]: Drone destruido no FOSSO por " + causa);
        }
        if(!bateuRocha){
            drone.setPosX(NPosDroneX);
            drone.setPosY(NPosDroneY);
            DeepSeaLog.getInstance().log("[DRONE]: Moveu-se para a posição [" + NPosDroneX + ", " + NPosDroneY + "] no Fosso do Mar");
        }
        return true;
    }

    public void destroiDrone() {
        Navio navio=data.getNavio();
        Drone drone=data.getNavio().getDroneFosso();
        if(drone==null) return;
        drone.setEstado(EstadoDrone.DESTRUIDO);
        drone.setCasco(0);
        drone.setCombustivel(-1 * drone.getCombustivel());
        if (navio.verificaDroneVivo()) {
            changeState(DeepSeaState.SUPERFICIE);
        } else {
            changeState(DeepSeaState.GAME_OVER);
        }
    }
}
