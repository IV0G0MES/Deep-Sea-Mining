package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.data.Jogo;
import pt.isec.pa.deepsea.model.data.Puzzle;
import pt.isec.pa.deepsea.model.state.states.IDeepSeaState;

import java.io.Serializable;


/**
 * Classe abstrata que atua como um adaptador para a interface {@link IDeepSeaState}.
 * <p>
 * O seu principal objetivo é fornecer implementações padrão para todos os métodos
 * de ação da Máquina de Estados, que por defeito (retornam, {@code false}
 * ou {@code null}). Desta forma, os estados concretos que herdam desta classe apenas
 * necessitam de fazer override aos métodos que são efetivamente válidos para esse estado,
 * reduzindo a duplicação de código.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public abstract class DeepSeaStateAdapter implements IDeepSeaState{

    private static final long serialVersionUID = 1L;

    protected DeepSeaContext context;
    protected Jogo data;

    /**
     * Construtor base para os estados do jogo.
     * * @param context O contexto atual da máquina de estados.
     * @param data A instância principal que contém os dados do jogo.
     */
    protected DeepSeaStateAdapter(DeepSeaContext context, Jogo data) {
        this.context = context;
        this.data = data;
    }


    /**
     * Método utilitário protegido para facilitar a transição de estados nas subclasses.
     * Invoca a mudança de estado no contexto instanciando o novo estado desejado.
     * * @param newState A constante do enum {@link DeepSeaState} que representa o estado destino.
     */
    protected void changeState(DeepSeaState newState){
        context.changeState(newState.getInstance(context,data));
    }

    @Override
    public boolean moveNavio(int direcaoX, int direcaoY) { return false; }

    @Override
    public boolean abastecerDrone(int idDrone, double qnt) { return false; }

    @Override
    public boolean iniciarDescida(int idDrone) { return false; }

    @Override
    public boolean moverDrone(int direcaoX, int direcaoY) { return false; }

    @Override
    public boolean apanhaObjeto() {return false;}

    @Override
    public boolean jogarPuzzle(int valorPecaAmover){ return false;}

    @Override
    public Puzzle getPuzzle(){return null;}

    @Override
    public boolean reparaDrone(int idDrone, double qntCascoRecuperar) { return false; }

    @Override
    public boolean melhoraDrone(int idDrone,int tipoUpgrade) { return false; }

    @Override
    public boolean sobeFosso(){return false;}
}
