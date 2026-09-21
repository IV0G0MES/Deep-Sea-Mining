package pt.isec.pa.deepsea.model.data.Settings;


/**
 * Interface que armazena todas as configurações e constantes globais do jogo.
 * Esta Interface engloba todas as configurações tanto a nivel das dimensões do jogo
 * como tambem configurações a nivel do Navio, Drone e de Criacao de Superficie,Fosso e fundo.
 * * @author João Barão,Ivo Gomes,Inês Moreira
 * @version 1.0
 */
public interface Settings{

    /** Define se o modo de defesa está ativo.*/
    boolean MODO_DEFESA = false;

    // --- Configurações do Navio ---

    /** Custo de combustível para as operações do Navio na superfície. */
    double CUSTO_COMB_NAV = 10;
    /** Capacidade máxima do tanque de combustível do Navio. */
    double MAX_COMB_NAVIO = 1000.0;


    // --- Configurações do Drone ---

    /** Valor máximo de integridade do casco de um drone padrão. */
    double MAX_CASCO = 100.0;

    /** Capacidade máxima de combustível de um drone padrão. */
    double MAX_COMB_DRONE = 100.0;

    /** Número máximo de itens que o inventário de um drone pode carregar. */
    int TAM_MAX_INVENTARIO_DRONE = 100;

    /** Custo de combustível por cada movimento ou ação realizada pelo drone. */
    double CUSTO_COMB_DRONE = 0.03;


    // --- Configurações das Grelhas (Grids) ---

    /** Dimensão da grelha do mapa da Superfície.(ex: 15x15) */
    int SUPERFICIE = 15;

    /** Dimensão da grelha do mapa do Fundo do Mar. */
    int FUNDO = 15;

    /** Dimensão da grelha que representa o Fosso (descida/subida). */
    int FOSSO = 15;

    /** Limite máximo de artefactos que podem existir no mundo. */
    int N_MAX_ARTEFACTOS = 10;

    /** Quantidade de artefactos gerada aleatoriamente para a sessão atual. */
    int N_ARTEFACTOS = (int) (Math.random() * N_MAX_ARTEFACTOS) + 1;


    // --- Probabilidades de Geração (Grelhas) ---

    /** Probabilidade (em percentagem) de gerar um animal marinho no Fosso */
    int PROB_ANIMAL = 5;

    /** Probabilidade (em percentagem) de gerar uma corrente marítima no Fosso. */
    int PROB_CORRENTE = 5;


    // --- Configurações de Minério ---

    /** Número máximo de celulas de minério por Fundo. */
    int NMAX_MINERIO = 4;

    /** Quantidade máxima da quantiddade de minério por celula de Minerio. */
    int QNT_MAX_MINERIO = 10;

    /** Custo de combustível necessário para realizar a extração de minério. */
    double CUSTO_RECOLHA_MINERIO = 5.0;

    // --- Configurações de Inimigos ---

    /** Número máximo de monstros que podem aparecer no mapa do fundo. */
    int MAXNUM_MONSTROS = 4;

    // --- Configurações do Puzzle (Mini-jogo) ---

    /** Número máximo de movimentos permitidos para resolver o puzzle. */
    int MAX_MOVES_PUZZLE = 60;

    /** Número de trocas (shuffles) realizadas para baralhar o puzzle inicial. */
    int MAX_SUFFLE_PUZZLE = 5;

    // --- Configurações de Geração de Terreno ---

    /** * Probabilidade do fundo encontrado nao ser um Fundo Rico
     * Fundo Rico: Representa um Fundo com a Maxima quantidade de celulas de Minerio Possivel
     */
    double FUNDORICO = 0.92;


    /** Dano Monstro*/
    double DANO_MONSTRO = 10;
}
