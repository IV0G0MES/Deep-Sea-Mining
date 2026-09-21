package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;

/**
 * Representa a unidade principal de exploração do jogo: o Drone.
 * <p>
 * O Drone é responsável por navegar pelas várias camadas do oceano (Fosso e Fundo),
 * recolher recursos (Minérios e Artefactos) e gerir os seus próprios sistemas vitais,
 * nomeadamente a integridade do casco e o nível de combustível. Implementa a interface
 * {@link Cloneable} possui um construtor de cópia
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class Drone implements Cloneable, Serializable {

    private static final long serialVersionUID = 1L;

    /** Gerador de IDs sequenciais para garantir a identidade única de cada drone na frota. */
    private static int Proxid = 0;
    /** Identificador único do drone. */
    private int id;
    /** Lista de itens (Artefactos ou Minérios) atualmente carregados pelo drone. */
    private List<Item> Inventario;
    /** Integridade atual do casco do drone. Se chegar a 0, o drone é destruído. */
    private double casco;
    /** Quantidade atual de combustível disponível no tanque do drone. */
    private double combustivel;
    /** Estado operacional e de localização atual do drone. */
    private EstadoDrone estado;
    /** Coordenada X (linha) atual do drone no mapa. */
    private int posX;
    /** Coordenada Y (coluna/profundidade) atual do drone no mapa. */
    private int posY;
    /** Valor do próximo dano contínuo a ser aplicado ao casco (segue uma sequência de números primos). */
    private int proxDanoCasco;
    /** Capacidade máxima atual do tanque de combustível (pode ser aumentada na Oficina). */
    private double capacidadeMax;
    /** Valor máximo possível para a integridade do casco (pode ser aumentado na Oficina). */
    private double cascoMax;
    /** Nível atual de melhoria aplicada ao tanque de combustível. */
    private int nivelUpgradeCombustivel;
    /** Nível atual de melhoria aplicada ao casco. */
    private int nivelUpgradeCasco;
    /** Contagem total de upgrades realizados neste drone. */
    private int NUpgrades;


    /**
     * Construtor padrão da classe Drone.
     * Inicializa o drone com estatísticas base definidas nas configurações,
     * atribui-lhe um ID único e coloca-o no estado de {@link EstadoDrone#REPOUSO}.
     */
    public Drone(){
        Inventario = new ArrayList<>();
        this.id = ++Proxid;
        this.casco = MAX_CASCO;
        this.combustivel = MAX_COMB_DRONE;
        this.estado = EstadoDrone.REPOUSO;
        this.proxDanoCasco = 1;
        this.capacidadeMax = MAX_COMB_DRONE;
        this.cascoMax = MAX_CASCO;
        this.nivelUpgradeCombustivel = 0;
        this.nivelUpgradeCasco = 0;
        this.NUpgrades = 0;

    }

    /**
     * Construtor de cópia (Copy Constructor).
     * Cria uma nova instância de Drone baseada nos atributos de outro drone existente.
     * Garante a cópia profunda (deep copy) da lista de inventário para evitar referências partilhadas.
     * * @param outro A instância de Drone a ser copiada.
     */
    public Drone(Drone outro){
        this.id = outro.id;
        this.casco = outro.casco;
        this.combustivel = outro.combustivel;
        this.capacidadeMax = outro.capacidadeMax;
        this.cascoMax = outro.cascoMax;
        this.estado = outro.estado;
        this.posX = outro.posX;
        this.posY = outro.posY;
        this.Inventario = new ArrayList<>(outro.Inventario);
        this.nivelUpgradeCombustivel = outro.nivelUpgradeCombustivel;
        this.nivelUpgradeCasco = outro.nivelUpgradeCasco;
        this.NUpgrades = outro.NUpgrades;

    }

    /** @return O identificador único do drone. */
    public int getId(){
        return this.id;
    }

    /** @return O estado operacional atual do drone. */
    public EstadoDrone getEstado(){
        return estado;
    }

    /**
     * Altera o estado operacional do drone, caso seja diferente do estado atual.
     * @param newEstado O novo {@link EstadoDrone} a aplicar.
     */
    public void setEstado(EstadoDrone newEstado){
        if(this.estado.equals(newEstado)) return;
        this.estado = newEstado;
    }

    /** @return Uma coleção contendo os itens atualmente no inventário do drone. */
    public Collection<Item> getInventario(){
        return this.Inventario;
    }

    /**
     * Adiciona ou remove combustível do tanque do drone, respeitando os limites
     * de 0 e da capacidade máxima atual.
     * * @param qnt Quantidade de combustível a somar (ou subtrair, se negativo).
     */
    public void setCombustivel(double qnt){
        if(qnt + this.combustivel >= this.capacidadeMax){
            System.out.println("Quantidade de combustivel acima do permitido, O restante desaguou no mar :(");
            this.combustivel = this.capacidadeMax;
        } else if(this.combustivel + qnt <= 0){
            this.combustivel = 0;
        } else {
            this.combustivel += qnt;
        }
    }

    /**
     * Tenta consumir uma determinada quantidade de combustível para realizar uma ação.
     * * @param quantidade O valor de combustível a subtrair.
     * @return {@code true} se o gasto foi efetuado com sucesso; {@code false} se
     * o combustível não foi suficiente (ficando a 0).
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

    /** @return A quantidade atual de combustível. */
    public double getCombustivel(){
        return this.combustivel;
    }

    /**
     * Força a definição do valor do casco, garantindo que não ultrapassa
     * a integridade máxima nem desce abaixo de 0.
     * * @param casco O novo valor de integridade do casco.
     */
    public void setCasco(double casco){
        if (casco < 0) {
            this.casco = 0;
        }
        else if(casco > this.cascoMax){
            this.casco = cascoMax;
        }
        else this.casco = casco;
    }

    /** @return A integridade atual do casco. */
    public double getCasco(){
        return this.casco;
    }

    /** @return O valor máximo possível para o casco com os upgrades atuais. */
    public double getCascoMax(){ return this.cascoMax; }

    /** @param posX A nova coordenada X do drone. */
    public void setPosX(int posX) { this.posX = posX; }

    /** @param posY A nova coordenada Y do drone. */
    public void setPosY(int posY) { this.posY = posY; }

    /** @return A coordenada X atual do drone. */
    public int getPosX() { return this.posX; }

    /** @return A coordenada Y atual do drone. */
    public int getPosY(){ return this.posY; }

    /**
     * Reinicia o ciclo de progressão do dano contínuo,
     * voltando ao valor base de 1.
     */
    public void resetDanoCasco(){
        proxDanoCasco = 1;
    }

    /**
     * Aplica o dano contínuo atual ao casco e calcula imediatamente
     * o valor do próximo dano da sequência.
     * * @return {@code true} se o drone sobreviveu ao dano; {@code false} se foi destruído (casco a 0).
     */
    public boolean danificaCasco() {
        casco -= proxDanoCasco;
        if (casco <= 0) {
            casco = 0;
            return false;
        }
        calculaProxDano();
        return true;
    }

    /**
     * Aplica uma quantidade específica de dano direto ao casco, contornando a
     * mecânica de dano progressivo. Utilizado para ataques de monstros ou perigos ambientais.
     * * @param quantidade O valor do dano a subtrair.
     */
    public void sofreDanoFixo(double quantidade) {
        casco -= quantidade;
        if (casco < 0) {
            casco = 0;
        }
    }

    /**
     * Algoritmo responsável por calcular a progressão do dano contínuo sofrido pelo drone.
     * <p>
     * Esta mecânica utiliza a sequência matemática de números primos. Começa em 1,
     * e a cada invocação avança para o número primo seguinte (2, 3, 5, 7, 11, etc.),
     * aumentando a letalidade do ambiente ao longo do tempo.
     * </p>
     */
    private void calculaProxDano() {
        int verificaPrimo;
        if (proxDanoCasco == 1) {
            proxDanoCasco = 2;
            return;
        }
        verificaPrimo = proxDanoCasco + 1;
        boolean encontrouPrimo = false;
        while (!encontrouPrimo) {
            boolean ePrimo = true;
            for (int i = 2; i <= Math.sqrt(verificaPrimo); i++) {
                if (verificaPrimo % i == 0) {
                    ePrimo = false;
                    break;
                }
            }
            if (ePrimo) {
                encontrouPrimo = true;
                proxDanoCasco = verificaPrimo;
            } else {
                verificaPrimo++;
            }
        }
    }

    /** @return A capacidade máxima atual do tanque de combustível. */
    public double getCapacidadeMax(){
        return this.capacidadeMax;
    }

    /** @return O nível atual de upgrades efetuados no combustível. */
    public int getNivelUpgradeCombustivel() { return nivelUpgradeCombustivel; }

    /** @return O nível atual de upgrades efetuados no casco. */
    public int getNivelUpgradeCasco() { return nivelUpgradeCasco; }

    /**
     * Aumenta a capacidade máxima do tanque de combustível e incrementa o nível do upgrade.
     * @param extra O valor de capacidade a adicionar ao tanque.
     */
    public void aumentarCapacidadeCombustivel(double extra) {
        this.capacidadeMax += extra;
        this.nivelUpgradeCombustivel++;
    }

    /**
     * Aumenta a resistência máxima do casco, cura o casco atual na mesma proporção
     * e incrementa o nível do upgrade.
     * @param extra O valor de integridade a adicionar ao limite máximo.
     */
    public void aumentarCascoMaximo(double extra) {
        this.cascoMax += extra;
        this.nivelUpgradeCasco++;
        this.casco += extra;
    }

    /** @return O número total de upgrades já realizados neste drone. */
    public int getNUpgrades(){
        return this.NUpgrades;
    }

    /**
     * Incrementa o contador global de upgrades do drone.
     */
    public void aumentarNUpgrades(){
        this.NUpgrades++;
    }

    public static void recalibrarContador(int idExistente){
        if(idExistente >= Proxid)Proxid = idExistente ;
    }

    public static void resetContador(){
        Proxid = 0;
    }

    /**
     * Gera uma representação textual detalhada dos sistemas vitais do drone,
     * ideal para relatórios de log e depuração.
     * * @return String formatada com os dados de ID, estado, casco, combustível, carga e posição.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Drone [ID: ").append(this.id).append("]");
        sb.append(" | Estado: ").append(this.estado);
        sb.append(" | Casco: ").append(this.casco).append("/").append(cascoMax);
        sb.append(" | Combustível: ").append(this.combustivel);
        sb.append(" | Carga: ").append(this.Inventario.size()).append(" itens");
        sb.append(" | Posição: (").append(this.posX).append(", ").append(this.posY).append(")");

        return sb.toString();
    }

    /**
     * Cria e devolve uma cópia profunda (clone) deste drone.
     * A lista de inventário é clonada para garantir que as alterações no clone
     * não afetam o inventário original.
     * * @return Uma nova instância de Drone com o mesmo estado deste objeto.
     * @throws CloneNotSupportedException Se a clonagem falhar.
     */
    @Override
    public Drone clone() throws CloneNotSupportedException {
        Drone copia = (Drone) super.clone();
        copia.Inventario = new ArrayList<>(this.Inventario);
        return copia;
    }
}
