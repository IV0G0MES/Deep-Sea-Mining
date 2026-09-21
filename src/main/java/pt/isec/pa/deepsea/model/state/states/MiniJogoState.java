package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.data.*;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;
import pt.isec.pa.deepsea.model.utils.DeepSeaLog;
import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;


/**
 * Classe que representa o estado do Mini-jogo na Máquina de Estados.
 * <p>
 * Este estado é acionado quando um drone encontra um Artefacto no Fundo do Mar.
 * Gere a sessão do puzzle deslizante, processando as jogadas efetuadas. Caso o
 * jogador resolva o puzzle a tempo, o artefacto é guardado no inventário e a exploração
 * retoma no Fundo. Caso os movimentos se esgotem, o drone é forçado a abortar a missão
 * e iniciar a subida do Fosso de mãos vazias.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class MiniJogoState extends DeepSeaStateAdapter {

    private static final long serialVersionUID = 1L;

    private Puzzle puzzle;

    public MiniJogoState(DeepSeaContext context, Jogo data) {
        super(context, data);
        this.puzzle = new Puzzle(MAX_MOVES_PUZZLE);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.MINI_JOGO;
    }

    @Override
    public boolean jogarPuzzle(int valorPecaAmover) {
        if (puzzle == null) return false;
        Drone drone=data.getNavio().getDroneFundo();
        boolean movimentoValido = puzzle.movePiece(valorPecaAmover);
        if (puzzle.isResolved()) {
            Navio navio=data.getNavio();
            Fundo fundo = data.getSuperficie().getElementoSuperficie()[navio.getPosX()][navio.getPosY()].getFundo();
            int posDroneX = drone.getPosX();
            int posDroneY = drone.getPosY();
            ElementoFundo elemento = fundo.getElementoFundo(posDroneX, posDroneY);
            if (elemento instanceof Artefacto) {
                drone.getInventario().add((Artefacto) elemento);
                fundo.removeElemento(posDroneX, posDroneY);
            }
            DeepSeaLog.getInstance().log("[MINIJOGO] Vencido! Artefacto adicionado ao inventario e a voltar para o FUNDO");
            changeState(DeepSeaState.FUNDO_MAR);
            return true;

        }
        else if (puzzle.getMovesLeft() <= 0) {
            drone.setEstado(EstadoDrone.FOSSO);
            drone.setPosX(FOSSO-1);
            drone.setPosY(FOSSO/2);
            changeState(DeepSeaState.SOBE_FOSSO);
            DeepSeaLog.getInstance().log("[MINIJOGO] Acabaram os Movimentos a voltar ao Fosso");
            return false;
        }
        if(!movimentoValido)DeepSeaLog.getInstance().log("[MINIJOGO] Movimento Invalido");
        return movimentoValido;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }
}
