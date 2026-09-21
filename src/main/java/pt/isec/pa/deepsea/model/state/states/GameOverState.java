package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.data.Jogo;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;


/**
 * Classe que representa o estado de Game Over na Máquina de Estados.
 * <p>
 * Este estado terminal é ativado quando o jogador falha a missão principal,
 * seja por destruição de toda a frota de drones ou por esgotamento de recursos
 * críticos sem possibilidade de recuperação.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class GameOverState extends DeepSeaStateAdapter {

    private static final long serialVersionUID = 1L;

    public GameOverState(DeepSeaContext context, Jogo data){
        super(context, data);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.GAME_OVER;
    }
}
