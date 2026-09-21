package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.data.*;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;
import pt.isec.pa.deepsea.model.utils.DeepSeaLog;
import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;


/**
 * Classe que representa o estado de exploração no Fundo do Mar na Máquina de Estados.
 * <p>
 * Este estado controla a movimentação do drone pelo Fundo marinho, gerindo interações
 * cruciais como a extração de minérios, o combate com monstros, a perda de combustível/casco
 * e a transição para o estado de {@link DeepSeaState#MINI_JOGO} ao detetar um artefacto.
 * Também lida com as mecânicas de destruição e perda de carga caso o drone não sobreviva.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class FundoMarState extends DeepSeaStateAdapter {

    private static final long serialVersionUID = 1L;

    public FundoMarState(DeepSeaContext context, Jogo data) {
        super(context, data);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.FUNDO_MAR;
    }

    @Override
    public boolean moverDrone(int direcaoX, int direcaoY) {
        Drone drone = data.getNavio().getDroneFundo();
        if (drone == null) return false;
        int PosNavioX = data.getNavio().getPosX();
        int PosNavioY = data.getNavio().getPosY();
        Fundo fundo = data.getSuperficie().getElementoSuperficie()[PosNavioX][PosNavioY].getFundo();
        int NPosDroneX = drone.getPosX() + direcaoX;
        int NPosDroneY = drone.getPosY() + direcaoY;

        if (NPosDroneX < 0 || NPosDroneY >= FUNDO || NPosDroneX >= FUNDO || NPosDroneY < 0) {
            DeepSeaLog.getInstance().log("[AVISO]: Drone bloqueado pelos limites do setor abissal!");
            return false;
        }
        drone.setPosX(NPosDroneX);
        drone.setPosY(NPosDroneY);
        DeepSeaLog.getInstance().log("[DRONE]: Moveu-se para a posição [" + NPosDroneX + ", " + NPosDroneY + "] no Fundo do Mar");

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i != 0 && j != 0) {
                    continue;
                }
                int vizinhoX = NPosDroneX + i;
                int vizinhoY = NPosDroneY + j;
                if (vizinhoX >= 0 && vizinhoX < FUNDO && vizinhoY >= 0 && vizinhoY < FUNDO) {
                    ElementoFundo elementoVizinho = fundo.getElementoFundo(vizinhoX, vizinhoY);
                    if (elementoVizinho != null) {
                        elementoVizinho.setVisivel(true);
                    }
                }
            }
        }

        ElementoFundo elementoFundo = fundo.getElementoFundo(NPosDroneX, NPosDroneY);

        if (elementoFundo instanceof Monstro) {
            drone.sofreDanoFixo(DANO_MONSTRO);
            DeepSeaLog.getInstance().log("[DRONE-PERIGO]: Drone atacado por Monstro do abismo");
        }
        if (!drone.gastaCombustivel(CUSTO_COMB_DRONE) || drone.getCasco() <= 0) {
            String causa = (drone.getCasco() <= 0) ? "O Drone nao resistiu aos danos no seu casco" : "Falta De Combustivel no Drone MAYDAY";
            destroiDrone();
            DeepSeaLog.getInstance().log("[DRONE-CRITICO]: Drone destruido por " + causa);
            return true;
        }
        return true;
    }

    @Override
    public boolean apanhaObjeto(){
        Drone drone = data.getNavio().getDroneFundo();
        int PosNavioX = data.getNavio().getPosX();
        int PosNavioY = data.getNavio().getPosY();
        int NPosDroneX = drone.getPosX();
        int NPosDroneY = drone.getPosY();
        Fundo fundo = data.getSuperficie().getElementoSuperficie()[PosNavioX][PosNavioY].getFundo();
        ElementoFundo elementoFundo = fundo.getElementoFundo(NPosDroneX, NPosDroneY);
        if(elementoFundo instanceof Minerio){
            if (drone.getInventario().size() < TAM_MAX_INVENTARIO_DRONE && ((Minerio) elementoFundo).getCustoRecolha() < drone.getCombustivel()) {
                drone.getInventario().add(((Minerio) elementoFundo));
                drone.gastaCombustivel(((Minerio) elementoFundo).getCustoRecolha());
                fundo.removeElemento(NPosDroneX, NPosDroneY);
                DeepSeaLog.getInstance().log("[DRONE]: Minerio Extraido com sucesso e guardado no Inventario");
                return true;
            }
            else{
                DeepSeaLog.getInstance().log("[DRONE-FALHOU]: Inventario CHEIO! O Drone nao conseguiu extrair o Minerio");
                return false;
            }
        }else if(elementoFundo instanceof Artefacto) {
            DeepSeaLog.getInstance().log("[DRONE]: Artefacto Encontrado! A inicar Mini-jogo");
            changeState(DeepSeaState.MINI_JOGO);
        }else{
            return false;
        }
        return true;
    }

    @Override
    public boolean sobeFosso(){
        Drone drone=data.getNavio().getDroneFundo();
        if(drone==null){return false;}
        DeepSeaLog.getInstance().log("[DRONE]: A sair do fundo do mar. A entrar no Fosso.");
        drone.setPosX(FOSSO-1);
        drone.setPosY(FOSSO/2);
        drone.setEstado(EstadoDrone.FOSSO);
        changeState(DeepSeaState.SOBE_FOSSO);
        return true;
    }

    public void destroiDrone() {
        Navio navio = data.getNavio();
        Drone drone = navio.getDroneFundo();
        if (drone == null) return;
        Fundo fundo = data.getSuperficie().getElementoSuperficie()[navio.getPosX()][navio.getPosY()].getFundo();
        for (Item item : drone.getInventario()) {
            if (item instanceof Artefacto || item instanceof Minerio) {
                fundo.adicionarElementoAleatorio((ElementoFundo) item);
            }
        }
        DeepSeaLog.getInstance().log("[DRONE]: O seu Inventario foi destruido");
        drone.getInventario().clear();
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
