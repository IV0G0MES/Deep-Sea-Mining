package pt.isec.pa.deepsea.model;

import pt.isec.pa.deepsea.model.data.*;
import pt.isec.pa.deepsea.model.state.DeepSeaContext;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.utils.DeepSeaLog;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.util.List;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;

/**
 * Fachada (Facade) principal do sistema DeepSea.
 * <p>
 * Centraliza todas as operações do jogo, servindo de ponte única entre a interface
 * gráfica e a lógica de negócio (Máquina de Estados e Dados). Implementa o padrão
 * Observer através de {@link PropertyChangeSupport} para notificação de eventos à UI.
 * </p>
 *
 * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class DeepSeaManager implements Serializable {

    /**
     * Suporte para o padrão Observer/PropertyChange.
     */
    private PropertyChangeSupport pcs;

    /**
     * Contexto da Máquina de Estados que gere a lógica do jogo.
     */
    private DeepSeaContext context;

    /**
     * Nome da propriedade disparada em atualizações gerais do modelo.
     */
    public static final String PROP = "_update_";

    /**
     * Construtor do Manager. Inicializa o contexto de jogo e o suporte de notificações.
     */
    public DeepSeaManager() {
        this.context = new DeepSeaContext();
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     * Gera uma representação visual da grelha de superfície.
     *
     * @return Matriz de Strings com símbolos: "A" (Artefacto), "?" (Vizinho), " " (Vazio).
     */
    public String[][] getGrelhaSuperficie() {
        int tamanho = getTamanhoGrelhaSuperficie();
        String[][] grelhaVisual = new String[tamanho][tamanho];

        for (int i = 0; i < tamanho; i++) {
            for (int j = 0; j < tamanho; j++) {
                if (context.getJogo().getSuperficie().eArtefacto(i, j)) {
                    grelhaVisual[i][j] = "A";
                } else if (context.getJogo().getSuperficie().eArtefactoVizinho(i, j)) {
                    grelhaVisual[i][j] = "?";
                } else {
                    grelhaVisual[i][j] = " ";
                }
            }
        }
        return grelhaVisual;
    }

    /**
     * @return Dimensão da grelha de superfície definida nas configurações.
     */
    public int getTamanhoGrelhaSuperficie() {
        return SUPERFICIE;
    }

    /**
     * Gera uma representação visual da grelha do fosso atual.
     * Inclui a posição do drone com o símbolo "D" caso este esteja no fosso.
     *
     * @return Matriz de Strings com símbolos: "R" (Rocha), "A" (Animal), "C" (Corrente), "D" (Drone).
     */
    public String[][] getGrelhaFosso() {
        return context.getJogo().getGrelhaFosso(context.getJogo().getNavio().getPosX(), context.getJogo().getNavio().getPosY());
    }

    /**
     * Gera uma representação visual da grelha do fundo do mar atual.
     *
     * @return Matriz de Strings com símbolos: "A" (Artefacto), "M" (Monstro), "m" (Minério), "~" (Água).
     */
    public String[][] getGrelhaFundo() {
        return context.getJogo().getGrelhaFundo(context.getJogo().getNavio().getPosX(), context.getJogo().getNavio().getPosY());
    }

    /**
     * @return Quantidade total de artefactos espalhados pelo mundo no início do jogo.
     */
    public int getQntArtefactosJogo() {
        return N_ARTEFACTOS;
    }

    /**
     * @return Quantidade de artefactos presentes na coordenada atual do navio.
     */
    public int getQntArtefactos() {
        int navioX = context.getJogo().getNavio().getPosX();
        int navioY = context.getJogo().getNavio().getPosY();
        return context.getJogo().getSuperficie().getElementoSuperficie()[navioX][navioY].getFundo().getQntArtefactos();
    }

    /**
     * @return O tabuleiro atual do puzzle 4x4 ou null se não houver puzzle ativo.
     */
    public int[][] getPuzzle() {
        if (context.getPuzzle() != null) {
            return context.getPuzzle().getTabuleiro();
        }
        return null;
    }

    /**
     * @return Número de movimentos restantes para resolver o puzzle.
     */
    public int getMovesLeft() {
        return context.getPuzzle().getMovesLeft();
    }

    /**
     * @return Coordenada X atual do navio na superfície.
     */
    public int getNavioPosX() {
        return this.context.getJogo().getNavio().getPosX();
    }

    /**
     * @return Coordenada Y atual do navio na superfície.
     */
    public int getNavioPosY() {
        return this.context.getJogo().getNavio().getPosY();
    }

    /**
     * @return Nível de combustível atual do navio.
     */
    public double getCombustivelNavio() {
        return context.getJogo().getNavio().getCombustivel();
    }

    /**
     * @return Número de artefactos já recuperados para o navio.
     */
    public int getArtefacto() {
        return context.getJogo().getNavio().getArtefactos().size();
    }

    /**
     * @return Lista de objetos artefacto recuperados (cópia de segurança).
     */
    public List<Artefacto> getArtefactos() {
        return context.getJogo().getNavio().getCopiaArtefactos();
    }

    /**
     * @return Quantidade total de minérios acumulada no navio.
     */
    public int getQtdMinerios() {
        return context.getJogo().getNavio().getQntMinerios();
    }

    /**
     * @return Lista de representações textuais dos drones ordenados por combustível.
     */
    public List<String> getDronesPorCombustivel() {
        return context.getJogo().getDronesPorCombustivel();
    }

    /**
     * @return Lista de representações textuais dos drones ordenados pela saúde do casco.
     */
    public List<String> getDronesPorCasco() {
        return context.getJogo().getDronesPorCasco();
    }

    /**
     * @return Histórico completo de mensagens do log de jogo.
     */
    public List<String> getLog() {
        return DeepSeaLog.getInstance().getLog();
    }

    /**
     * Limpa o histórico de logs e notifica a interface.
     */
    public void resetLog() {
        DeepSeaLog.getInstance().reset();
        pcs.firePropertyChange(PROP, 0, 1);
    }

    /**
     * @param msg Mensagem a adicionar ao log do sistema.
     */
    public void addLog(String msg) {
        DeepSeaLog.getInstance().log(msg);
    }

    /**
     * @param nomeFicheiro Caminho para exportar o log atual para ficheiro de texto.
     */
    public void guardarLog(String nomeFicheiro) {
        DeepSeaLog.getInstance().guardarLog(nomeFicheiro);
    }

    /**
     * Guarda o estado atual do contexto de jogo em ficheiro binário.
     *
     * @param nomeFicheiro Caminho do ficheiro destino.
     * @return true se a operação teve sucesso.
     */
    public boolean guardarJogo(String nomeFicheiro) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeFicheiro))) {
            oos.writeObject(this.context);
            DeepSeaLog.getInstance().log("[Sistema] Jogo guardado com sucesso!");
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Reinicia o jogo para o estado inicial e notifica a interface.
     */
    public void novoJogo() {
        context.getJogo().novoJogo();
        context = new DeepSeaContext();
        pcs.firePropertyChange(PROP, null, null);
    }

    /**
     * Carrega um estado de jogo previamente gravado.
     *
     * @param nomeFicheiro Caminho do ficheiro a carregar.
     * @return true se o carregamento foi bem sucedido.
     */
    public boolean carregarJogo(String nomeFicheiro) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeFicheiro))) {
            DeepSeaContext recuperado = (DeepSeaContext) ois.readObject();
            if (recuperado != null) {
                this.context = recuperado;
                pcs.firePropertyChange(PROP, null, null);
                return true;
            }
            return false;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * @return true se houver um artefacto depositado na posição atual do navio.
     */
    public boolean isArtefactoNaPosAtual() {
        return context.getJogo().getSuperficie().eArtefacto(context.getJogo().getNavio().getPosX(), context.getJogo().getNavio().getPosY());
    }

    /**
     * @return true se existir pelo menos um artefacto nas células adjacentes ao navio.
     */
    public boolean isVizinhoArtefactoDaPosAtual() {
        return context.getJogo().getSuperficie().eArtefactoVizinho(context.getJogo().getNavio().getPosX(), context.getJogo().getNavio().getPosY());
    }

    /**
     * @return Coordenada X do drone que está ativo em missão.
     */
    public int getDroneAtivoPosX() {
        return context.getJogo().getNavio().getPosDroneAtivoX();
    }

    /**
     * @return Coordenada Y do drone que está ativo em missão.
     */
    public int getDroneAtivoPosY() {
        return context.getJogo().getNavio().getPosDroneAtivoY();
    }

    /**
     * @return ID do drone que se encontra atualmente em operação.
     */
    public int getDroneAtivo() {
        return context.getJogo().getNavio().getDroneAtivo();
    }

    /**
     * @return Nível de combustível do drone atualmente ativo.
     */
    public double getDroneAtivoCombustivel() {
        int id = context.getJogo().getNavio().getDroneAtivo();
        if (id != -1) {
            return context.getJogo().getNavio().getDrone(id).getCombustivel();
        }
        return 0;
    }

    /**
     * @return Integridade atual do casco do drone em missão.
     */
    public double getIntegridadeDrone() {
        int id = context.getJogo().getNavio().getDroneAtivo();
        if (id != -1) {
            return context.getJogo().getNavio().getDrone(id).getCasco();
        }
        return 0;
    }

    /**
     * @return Lista de tipos de itens presentes no inventário do drone ativo.
     */
    public List<String> getDroneInventario() {
        return context.getJogo().getDroneInventario();
    }

    /**
     * @return Capacidade máxima de combustível do drone ativo.
     */
    public double getDroneCombustivelMaximo() {
        return context.getJogo().getNavio().getDrone(context.getJogo().getNavio().getDroneAtivo()).getCapacidadeMax();
    }

    /**
     * @param id ID do drone.
     * @return Resistência máxima do casco do drone especificado.
     */
    public double getDroneCascoMax(int id) {
        return context.getJogo().getNavio().getDrone(id).getCascoMax();
    }

    /**
     * @return Estado atual da máquina de estados do jogo.
     */
    public DeepSeaState getState() {
        return context.getState();
    }

    /**
     * Tenta mover o navio na superfície.
     *
     * @param direcaoX Deslocamento horizontal.
     * @param direcaoY Deslocamento vertical.
     * @return true se o movimento foi realizado.
     */
    public boolean moveNavio(int direcaoX, int direcaoY) {
        boolean res = context.moveNavio(direcaoX, direcaoY);
        if (res) pcs.firePropertyChange(PROP, null, null);
        return res;
    }

    /**
     * Abastece um drone na oficina.
     *
     * @param idDrone ID do drone.
     * @param qtd     Quantidade de combustível.
     * @return true se a operação foi concluída.
     */
    public boolean abastecerDrone(int idDrone, double qtd) {
        if (context.abastecerDrone(idDrone, qtd)) {
            pcs.firePropertyChange(PROP, null, null);
            return true;
        }
        return false;
    }

    /**
     * Inicia o lançamento de um drone para o fosso.
     *
     * @param idDrone ID do drone a lançar.
     * @return true se o lançamento foi iniciado.
     */
    public boolean iniciarDescida(int idDrone) {
        boolean res = context.iniciarDescida(idDrone);
        if (res) pcs.firePropertyChange(PROP, null, null);
        return res;
    }

    /**
     * Move o drone ativo no fosso ou no fundo.
     *
     * @param direcaoX Deslocamento horizontal.
     * @param direcaoY Deslocamento vertical.
     * @return true se a tentativa de movimento resultou em alteração de estado/dados.
     */
    public boolean moverDrone(int direcaoX, int direcaoY) {
        boolean res = context.moverDrone(direcaoX, direcaoY);
        if (res) pcs.firePropertyChange(PROP, null, null);
        return res;
    }

    /**
     * Tenta realizar um movimento no mini-jogo do puzzle.
     *
     * @param valorPecaAMover Valor da peça (número) que o utilizador clicou.
     * @return true se a peça foi movida.
     */
    public boolean jogarPuzzle(int valorPecaAMover) {
        boolean res = context.jogarPuzzle(valorPecaAMover);
        pcs.firePropertyChange(PROP, null, null);
        return res;
    }

    /**
     * Repara o casco de um drone na oficina.
     *
     * @param idDrone          ID do drone.
     * @param qtdVidaRecuperar Valor de integridade a recuperar.
     * @return true se a reparação foi efetuada.
     */
    public boolean reparaDrone(int idDrone, double qtdVidaRecuperar) {
        if (context.reparaDrone(idDrone, qtdVidaRecuperar)) {
            pcs.firePropertyChange(PROP, null, null);
            return true;
        }
        return false;
    }

    /**
     * Aplica uma melhoria permanente a um drone.
     *
     * @param idDrone     ID do drone.
     * @param tipoUpgrade Tipo (1 para Casco, 2 para Combustível).
     * @return true se o upgrade foi aplicado.
     */
    public boolean melhoraDrone(int idDrone, int tipoUpgrade) {
        if (context.melhoraDrone(idDrone, tipoUpgrade)) {
            pcs.firePropertyChange(PROP, null, null);
            return true;
        }
        return false;
    }

    /**
     * Tenta mudar o estado do drone para modo de subida do fosso. @return true se a ordem foi aceite.
     */
    public boolean sobeFosso() {
        boolean res = context.sobeFosso();
        if (res) pcs.firePropertyChange(PROP, null, null);
        return res;
    }

    /**
     * Tenta recolher um objeto (artefacto/minério) na posição atual do drone. @return true se recolheu com sucesso.
     */
    public boolean apanhaObjeto() {
        boolean res = context.apanhaObjeto();
        if (res) pcs.firePropertyChange(PROP, null, null);
        return res;
    }

    /**
     * Método especial Java para reconstruir objetos não serializáveis e recalibrar IDs
     * após o carregamento de um ficheiro.
     */
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        Drone.resetContador();
        for (Drone d : context.getJogo().getNavio().getDronesOrdenadosCasco()) {
            Drone.recalibrarContador(d.getId());
        }
        Obstaculo.resetContador();
        for (Obstaculo o : context.getJogo().getSuperficie().getTodosObstaculos()) {
            Obstaculo.recalibrarContador(o.getId());
        }
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     * @param prop Nome da propriedade. @param listener Ouvinte a registar.
     */
    public void addPropertyChangeListener(String prop, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(prop, listener);
    }

    /**
     * @param listener Ouvinte a registar para todas as mudanças.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * @param listener Ouvinte a remover da lista de notificações.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}