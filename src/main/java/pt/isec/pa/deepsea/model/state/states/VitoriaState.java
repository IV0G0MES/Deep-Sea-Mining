package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.data.Jogo;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.state.DeepSeaStateAdapter;


/**
 * Classe que representa o estado de Vitória na Máquina de Estados.
 * <p>
 * Este é um estado terminal atingido quando o jogador conclui com sucesso a
 * missão principal, que geralmente consiste na recuperação de todos os
 * artefactos necessários para completar o jogo.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class VitoriaState extends DeepSeaStateAdapter{
    private static final long serialVersionUID = 1L;

    public VitoriaState(DeepSeaContext context, Jogo data){
        super(context, data);
    }

    @Override
    public DeepSeaState getState() {
        return DeepSeaState.VITORIA;
    }
}
