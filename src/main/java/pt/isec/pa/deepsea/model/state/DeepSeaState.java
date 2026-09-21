package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.data.Jogo;
import pt.isec.pa.deepsea.model.state.states.*;

/**
 * Enumeração que define os estados possíveis da Máquina de Estados do jogo (Padrão State).
 * <p>
 * Além de identificar conceptualmente cada fase do jogo, esta enumeração atua como
 * uma Factory, sendo responsável por instanciar as classes concretas de
 * estado correspondentes através do método {@code getInstance}.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public enum DeepSeaState {
    SUPERFICIE,DESCE_FOSSO,SOBE_FOSSO,FUNDO_MAR,MINI_JOGO,VITORIA,GAME_OVER;


    /**
     * Factory Method que cria e devolve a instância concreta do estado correspondente.
     * @param context O contexto da máquina de estados, responsável por gerir as transições.
     * @param data A instância principal de {@link Jogo} que contém os dados a partilhar com o estado.
     * @return Uma nova instância concreta de {@link IDeepSeaState} pronta a ser utilizada.
     */
    public IDeepSeaState getInstance(DeepSeaContext context, Jogo data){
        return switch(this){
            case SUPERFICIE-> new SuperficieState(context,data);
            case DESCE_FOSSO -> new DesceFossoState(context,data);
            case SOBE_FOSSO -> new SobeFossoState(context,data);
            case FUNDO_MAR -> new FundoMarState(context,data);
            case MINI_JOGO -> new MiniJogoState(context,data);
            case VITORIA -> new VitoriaState(context,data);
            case GAME_OVER -> new GameOverState(context,data);
        };

    }
}
