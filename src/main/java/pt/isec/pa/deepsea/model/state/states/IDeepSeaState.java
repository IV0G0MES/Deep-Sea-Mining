package pt.isec.pa.deepsea.model.state.states;

import pt.isec.pa.deepsea.model.data.Puzzle;
import pt.isec.pa.deepsea.model.state.DeepSeaState;


import java.io.Serializable;


/**
 * Interface base da Máquina de Estados (Padrão State).
 * <p>
 * Define o contrato obrigatório para todos os estados do jogo. Declara a totalidade
 * das ações e eventos que podem ser desencadeados durante a partida (movimentação,
 * gestão de drones, transições de área e mini-jogos). A sua implementação nas classes
 * concretas dita quais as ações permitidas em cada fase específica.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public interface IDeepSeaState extends Serializable {

    DeepSeaState getState();
    boolean moveNavio(int direcaoX, int direcaoY);
    boolean abastecerDrone(int idDrone, double qnt);
    boolean iniciarDescida(int idDrone);
    boolean moverDrone(int direcaoX, int direcaoY);
    boolean apanhaObjeto();
    boolean jogarPuzzle(int valorPecaAmover);
    Puzzle getPuzzle();
    boolean reparaDrone(int idDrone,double qntCascoRecuperar);
    boolean melhoraDrone(int idDrone,int tipoUpgrade);
    boolean sobeFosso();
}
