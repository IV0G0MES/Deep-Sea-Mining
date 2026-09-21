package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.data.*;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;
import pt.isec.pa.deepsea.model.utils.DeepSeaLog;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;

/**
 * Classe que representa o estado de subida pelo Fosso na Máquina de Estados.
 * <p>
 * Este estado gere o trajeto de regresso do drone do Fundo do Mar em direção
 * ao Navio na superfície. Durante a ascensão, aplica as mecânicas de colisão e perigos
 * (Rochas, Correntes Marítimas e Animais Marinhos), consumo de recursos e validação
 * de condições de término de jogo (Vitória ao recolher todos os artefactos ou Game Over
 * caso seja o último drone a ser destruído).
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class SobeFossoState extends DeepSeaStateAdapter {

    private static final long serialVersionUID = 1L;

    public SobeFossoState(DeepSeaContext context, Jogo data){
        super(context, data);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.SOBE_FOSSO;
    }

    @Override
    public boolean moverDrone(int direcaoX, int direcaoY) {
        Drone drone = data.getNavio().getDroneFosso();
        if (drone == null) return false;

        int PosNavioX = data.getNavio().getPosX();
        int PosNavioY = data.getNavio().getPosY();
        Fosso fosso = data.getSuperficie().getElementoSuperficie()[PosNavioX][PosNavioY].getFosso();

        int nPosDroneX = drone.getPosX() + direcaoX;
        int nPosDroneY = drone.getPosY() + direcaoY;
        if (nPosDroneX < 0) {
            data.getNavio().recolheDrone(drone);
            if(data.getNavio().getArtefactos().size() >= N_ARTEFACTOS) {
                DeepSeaLog.getInstance().log("[FOSSO] Todos os Artefactos foram Coletados: A voltar para o Navio e a ir para a Costa");
                changeState(DeepSeaState.VITORIA);
                return true;
            }
            if (!data.getNavio().verificaDroneVivo()) {
                DeepSeaLog.getInstance().log("[FOSSO] Todos os Drones foram Destruidos");
                changeState(DeepSeaState.GAME_OVER);
                return true;
            }
            DeepSeaLog.getInstance().log("[FOSSO] A Iniciar Volta ao navio");
            changeState(DeepSeaState.SUPERFICIE);
            return true;
        }


        if (nPosDroneY < 0 || nPosDroneY >= FOSSO || nPosDroneX >= FOSSO) {
            DeepSeaLog.getInstance().log("[SISTEMA]: Drone atingiu os Limites máximos do Fosso");
            return false;
        }

        ElementoFosso novoElemento = fosso.getElementoFosso(nPosDroneX, nPosDroneY);
        double custoCombustivel = CUSTO_COMB_DRONE;
        boolean bateuRocha=false;
        if (novoElemento instanceof CorrenteMaritima) {
            DeepSeaLog.getInstance().log("[FOSSO] O Drone n-" + drone.getId() + " foi Apanhado numa corrente");
            custoCombustivel *= 2;
        } else if (novoElemento instanceof AnimalMarinho){
            DeepSeaLog.getInstance().log("[FOSSO] O Drone n-" + drone.getId() + " foi atacado por um Animal Marinho Faminto");
            drone.danificaCasco();
        }else if(novoElemento instanceof Rocha){
            DeepSeaLog.getInstance().log("[FOSSO] O Drone n-" + drone.getId() + " foi contra Rochas");
            drone.danificaCasco();
            bateuRocha=true;
        }
        if (!drone.gastaCombustivel(custoCombustivel) || drone.getCasco() <= 0) {
            String causa = (drone.getCasco() <= 0) ? "O Drone nao resistiu aos danos no seu casco" : "Falta De Combustivel no Drone MAYDAY";
            destroiDrone();
            DeepSeaLog.getInstance().log("[DRONE-CRITICO]: Drone destruido no FOSSO por " + causa);
        }
        if(!bateuRocha) {
            drone.setPosX(nPosDroneX);
            drone.setPosY(nPosDroneY);
            DeepSeaLog.getInstance().log("[DRONE]: Moveu-se para a posição [" + nPosDroneX + ", " + nPosDroneY + "] no Fosso do Mar");
        }
        return true;
    }

    public void destroiDrone() {
        Navio navio = data.getNavio();
        Drone drone = navio.getDroneFosso();
        if (drone == null) return;
        Fundo fundo = data.getSuperficie().getElementoSuperficie()[navio.getPosX()][navio.getPosY()].getFundo();
        for (Item item : drone.getInventario()) {
            if (item instanceof Artefacto || item instanceof Minerio) {
                fundo.adicionarElementoAleatorio((ElementoFundo) item);
            }
        }
        drone.getInventario().clear();
        drone.setEstado(EstadoDrone.DESTRUIDO);
        drone.setCasco(0);
        if (navio.verificaDroneVivo()) {
            changeState(DeepSeaState.SUPERFICIE);
        } else {
            changeState(DeepSeaState.GAME_OVER);
        }
    }
}
