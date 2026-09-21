package pt.isec.pa.deepsea.model.state;

import pt.isec.pa.deepsea.model.data.Jogo;
import pt.isec.pa.deepsea.model.data.Puzzle;
import pt.isec.pa.deepsea.model.state.states.IDeepSeaState;
import pt.isec.pa.deepsea.model.utils.DeepSeaLog;

import java.io.Serializable;

/**
 * Classe de Contexto da Máquina de Estados (Padrão State).
 * <p>
 * O {@code DeepSeaContext} atua como o ponto de contacto central para a lógica de jogo.
 * Mantém a referência para os dados globais ({@link Jogo}) e para o estado atual
 * ({@link IDeepSeaState}). O seu principal papel é delegar todas as ações solicitadas
 * para o estado que se encontra ativo no momento, garantindo que as regras de cada fase
 * são respeitadas.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class DeepSeaContext implements Serializable {

    private static final long serialVersionUID = 1L;

    private Jogo data;
    private IDeepSeaState currentState;


    /**
     * Construtor do Contexto.
     * Inicializa a máquina de estados definindo os dados do jogo e estabelecendo
     * a {@link DeepSeaState#SUPERFICIE} como o estado inicial padrão.
     * * @param data A instância de dados do jogo a ser partilhada entre os estados.
     */
    public  DeepSeaContext() {
        this.data = new Jogo();
        this.currentState =DeepSeaState.SUPERFICIE.getInstance(this,data);
    }

    /**
     * Efetua a transição da máquina para um novo estado.
     * Este método é chamado internamente pelos próprios estados quando as condições
     * de mudança são atingidas. Regista também a transição no sistema de logs.
     * * @param newState A nova instância de {@link IDeepSeaState} a assumir o controlo.
     */
    void changeState(IDeepSeaState newState) {
        DeepSeaLog.getInstance().log("[STATE-CHANGED] " + this.currentState.getClass().getSimpleName() + " => " + newState.getClass().getSimpleName());
        this.currentState = newState;
    }


    /**
     * O conjunto de métodos que se segue atua como a interface de comunicação
     * entre a UI e a Máquina de Estados.
     * <p>
     * O propósito exclusivo destes métodos é receber as intenções do jogador
     * (movimentos, gestão de drones na oficina, transições no fosso e interações
     * com o puzzle) e delegá-las diretamente para a instância do estado que se
     * encontra ativo no momento ({@code currentState}).
     * </p>
     * <p>
     * A validação de cada ação recai sobre o estado atual: se a ação não for
     * permitida nessa fase do jogo, o estado simplesmente ignora o pedido.
     * </p>
     */
    public DeepSeaState getState() {
        return currentState.getState();
    }
    public boolean moveNavio(int direcaoX, int direcaoY){return currentState.moveNavio(direcaoX,direcaoY);}
    public boolean abastecerDrone(int idDrone, double qnt){return currentState.abastecerDrone(idDrone,qnt);}
    public boolean iniciarDescida(int idDrone){return currentState.iniciarDescida(idDrone);}
    public boolean moverDrone(int direcaoX, int direcaoY){return currentState.moverDrone(direcaoX,direcaoY);}
    public boolean apanhaObjeto(){return currentState.apanhaObjeto();}
    public boolean jogarPuzzle(int valorPecaAmover){return currentState.jogarPuzzle(valorPecaAmover);}
    public Puzzle getPuzzle(){return currentState.getPuzzle();}
    public boolean reparaDrone(int idDrone, double qntVidaParaRecuperar){
        return currentState.reparaDrone(idDrone, qntVidaParaRecuperar);
    }
    public boolean melhoraDrone(int idDrone,int tipoUpgrade){return currentState.melhoraDrone(idDrone,tipoUpgrade);}
    public boolean sobeFosso(){return currentState.sobeFosso();}


    //getters
    public Jogo getJogo() {
        return this.data;
    }
}

