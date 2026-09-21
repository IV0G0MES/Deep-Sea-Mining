package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;
import java.util.*;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;

/**
 * Classe que representa o Navio, a base principal de operações na superfície.
 * <p>
 * O Navio é responsável por gerir a frota de Drones, armazenar o combustível global,
 * guardar os minérios e artefactos recuperados das profundezas e abrigar a {@link Oficina}
 * para melhorias e reparações. Gere também o seu próprio posicionamento na grelha da superfície.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class Navio implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Lista de artefactos únicos que foram recuperados com sucesso para o navio. */
    private ArrayList<Artefacto> artefactos;

    /** Conjunto de drones pertencentes à frota do navio. */
    private Set<Drone> drones;

    /** * Instância da oficina para gestão de upgrades.
     * É marcada como transient para evitar problemas de serialização (dependência cíclica).
     */
    transient private Oficina oficina;

    /** Coordenada X (linha) atual do navio na grelha de superfície. */
    private int posX;

    /** Coordenada Y (coluna) atual do navio na grelha de superfície. */
    private int posY;

    /** Quantidade de combustível disponível no reservatório do navio. */
    private double combustivel;

    /** Quantidade total de minérios armazenados a bordo. */
    private int qntMinerios;

    /**
     * Construtor da classe Navio.
     * Inicializa as coleções, cria uma frota padrão de 3 drones e define a posição
     * inicial no centro do mapa. O tanque de combustível é atestado no máximo.
     */
    public Navio(){
        artefactos = new ArrayList<>();
        drones = new HashSet<>();
        for(int i = 0; i < 3; i++){
            drones.add(new Drone());
        }
        this.posX = SUPERFICIE / 2 - 1;
        this.posY = SUPERFICIE / 2 - 1;
        this.oficina = new Oficina(this);
        this.combustivel = MAX_COMB_NAVIO;
        this.qntMinerios = 0;
    }

    /** @return O combustível atual do navio. */
    public double getCombustivel() {
        return combustivel;
    }

    /**
     * Tenta consumir uma determinada quantidade de combustível do reservatório.
     * @param quantidade O valor de combustível a subtrair.
     * @return {@code true} se havia combustível suficiente e foi gasto;
     * {@code false} se o combustível chegou a zero durante a operação.
     */
    public boolean gastaCombustivel(double quantidade) {
        if (this.combustivel - quantidade < 0) {
            this.combustivel = 0;
            return false;
        } else {
            this.combustivel -= quantidade;
            return true;
        }
    }

    /**
     * Ajusta (adiciona ou remove) combustível ao reservatório do navio.
     * O valor final é limitado entre 0 e MAX_COMB_NAVIO.
     * @param qnt Quantidade de combustível a adicionar (pode ser negativo).
     * @return Sempre {@code true}, indicando que a operação foi concluída (mesmo com limites aplicados).
     */
    public boolean setCombustivel(double qnt) {
        if (this.combustivel + qnt >= MAX_COMB_NAVIO) {
            this.combustivel = MAX_COMB_NAVIO;
            return true;
        } else if (this.combustivel + qnt <= 0) {
            this.combustivel = 0;
            return true;
        } else {
            this.combustivel += qnt;
            return true;
        }
    }

    /** @return Lista de artefactos armazenados no navio. */
    public ArrayList<Artefacto> getArtefactos() {
        return artefactos;
    }

    /** @return O total de minérios acumulados no navio. */
    public int getQntMinerios(){
        return this.qntMinerios;
    }

    /**
     * Adiciona uma quantidade de minérios ao armazém do navio.
     * @param qnt Quantidade a somar ao total atual.
     */
    public void setQntMinerios(int qnt){
        this.qntMinerios += qnt;
    }

    /**
     * Processa o regresso de um drone ao navio.
     * Transfere todos os itens (Artefactos e Minérios) do inventário do drone
     * para os cofres do navio, limpa a carga do drone e altera o seu estado para REPOUSO.
     * @param drone O drone que está a ser recolhido.
     */
    public void recolheDrone(Drone drone) {
        if (drone == null || drone.getInventario() == null) {
            return;
        }

        for (Item item : drone.getInventario()) {
            if (item instanceof Artefacto) {
                this.artefactos.add((Artefacto) item);
            } else if (item instanceof Minerio) {
                int quantidadeExtraida = ((Minerio) item).getQnt();
                this.setQntMinerios(quantidadeExtraida);
            }
        }

        drone.getInventario().clear();
        drone.setEstado(EstadoDrone.REPOUSO);
    }

    /**
     * Obtém uma lista de todos os drones ordenados de forma decrescente pelo combustível.
     * Retorna cópias dos objetos para evitar modificações externas à coleção original.
     * @return Lista ordenada de drones baseada na capacidade atual de combustível.
     */
    public List<Drone> getDronesOrdenadosCombustivel() {
        return getDrones(Comparator.comparingDouble(Drone::getCombustivel));
    }

    /**
     * Obtém uma lista de todos os drones ordenados de forma decrescente pela integridade do casco.
     * Retorna cópias dos objetos para evitar modificações externas à coleção original.
     * @return Lista ordenada de drones baseada na saúde atual do casco.
     */
    public List<Drone> getDronesOrdenadosCasco() {
        return getDrones(Comparator.comparingDouble(Drone::getCasco));
    }

    /**
     * Método auxiliar genérico para obter a lista de drones ordenada mediante um comparador.
     * @param droneComparator O comparador que dita a regra de ordenação.
     * @return Lista de cópias (clones) dos drones ordenadas de forma decrescente.
     */
    private List<Drone> getDrones(Comparator<Drone> droneComparator) {
        List<Drone> dronesOrdenados = new ArrayList<>();
        for (Drone d : this.drones) {
            dronesOrdenados.add(new Drone(d));
        }
        dronesOrdenados.sort(droneComparator.reversed());
        return dronesOrdenados;
    }

    /**
     * Procura um drone específico na frota através do seu ID.
     * @param id Identificador do drone.
     * @return A referência para o Drone, ou null se não for encontrado.
     */
    public Drone getDrone(int id){
        for(Drone drone:drones){
            if(drone.getId() == id){
                return drone;
            }
        }
        return null;
    }

    /**
     * Verifica se existe algum drone ativo (em exploração ou trânsito) e devolve o seu ID.
     * Considera-se ativo um drone que não esteja em REPOUSO nem DESTRUIDO.
     * @return O ID do drone ativo, ou -1 se todos estiverem no navio ou destruídos.
     */
    public int getDroneAtivo(){
        for(Drone drone : drones){
            if(!drone.getEstado().equals(EstadoDrone.REPOUSO) && !drone.getEstado().equals(EstadoDrone.DESTRUIDO)){
                return drone.getId();
            }
        }
        return -1;
    }

    public int getPosDroneAtivoX(){
        return getDrone(getDroneAtivo()).getPosX();
    }

    public int getPosDroneAtivoY(){
        return getDrone(getDroneAtivo()).getPosY();
    }

    /**
     * Procura o drone que se encontra atualmente no Fosso.
     * @return O Drone correspondente, ou null se nenhum estiver nesse estado.
     */
    public Drone getDroneFosso(){
        for (Drone drone : drones) {
            if (drone.getEstado().equals(EstadoDrone.FOSSO)) {
                return drone;
            }
        }
        return null;
    }

    public List<Artefacto> getCopiaArtefactos(){
        return List.copyOf(artefactos);
    }

    /**
     * Procura o drone que se encontra atualmente no Fundo do Mar.
     * @return O Drone correspondente, ou null se nenhum estiver nesse estado.
     */
    public Drone getDroneFundo(){
        for (Drone drone : drones) {
            if (drone.getEstado().equals(EstadoDrone.FUNDO)) {
                return drone;
            }
        }
        return null;
    }

    /**
     * Verifica se o navio ainda possui pelo menos um drone funcional.
     * Útil para validar condições de Game Over.
     * @return {@code true} se houver pelo menos um drone que não esteja destruído.
     */
    public boolean verificaDroneVivo(){
        for(Drone drone:drones){
            if(!drone.getEstado().equals(EstadoDrone.DESTRUIDO)){
                return true;
            }
        }
        return false;
    }

    /** @return A coordenada X atual do navio. */
    public int getPosX(){
        return posX;
    }

    /** @return A coordenada Y atual do navio. */
    public int getPosY(){
        return posY;
    }

    /** @param posX A nova coordenada X a atribuir ao navio. */
    public void setPosX(int posX){
        this.posX = posX;
    }

    /** @param posY A nova coordenada Y a atribuir ao navio. */
    public void setPosY(int posY){
        this.posY = posY;
    }

    /** @return A instância da Oficina pertencente a este navio. */
    public Oficina getOficina() {
        return oficina;
    }

    /**
     * Procura um artefacto específico armazenado no navio pelo seu ID.
     * @param id Identificador do artefacto.
     * @return O Artefacto correspondente, ou null se não o possuir.
     */
    public Artefacto getArtefacto(int id){
        for(Artefacto artefacto : artefactos){
            if(artefacto.getId() == id){
                return artefacto;
            }
        }
        return null;
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();

        this.oficina = new Oficina(this);
    }

    /**
     * Devolve uma representação textual do inventário a bordo do navio.
     * Lista os artefactos guardados e a quantidade total de minérios.
     * @return String formatada com os recursos a bordo.
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Artefactos Recolhidos:").append("\n");
        for(Artefacto artefacto : artefactos){
            sb.append(artefacto.toString());
        }
        sb.append("Quantidade de Minerios -> ").append(qntMinerios).append("\n");
        return sb.toString();
    }
}