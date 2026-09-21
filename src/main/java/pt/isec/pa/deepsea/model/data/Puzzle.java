package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.utils.DeepSeaLog;

import java.io.Serializable;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;


/**
 * Classe responsável pela lógica do mini-jogo de recuperação de artefactos.
 * <p>
 * Implementa um puzzle deslizante (Sliding Puzzle) de matriz 4x4, onde o jogador
 * deve ordenar os números de 1 a 15, movendo as peças para o espaço vazio.
 * O jogo inclui um limite de movimentos e garante a solubilidade através de um
 * baralhamento baseado em movimentos válidos.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class Puzzle implements Serializable{

    private static final long serialVersionUID = 1L;

    /** Matriz que representa o tabuleiro do puzzle (4x4). */
    private int[][] tab;
    /** Contador de movimentos restantes para o jogador. */
    private int movesLeft;

    /** Coordenada da linha onde se encontra o espaço vazio (valor 0). */
    private int linhaVazia;
    /** Coordenada da coluna onde se encontra o espaço vazio (valor 0). */
    private int colunaVazia;

    /**
     * Construtor da classe Puzzle.
     * Inicializa o tabuleiro no estado resolvido, define o limite de movimentos
     * e baralha as peças aleatoriamente.
     * @param maxMoves Número máximo de movimentos permitidos para resolver o puzzle.
     */
    public Puzzle (int maxMoves){
        this.movesLeft = maxMoves;
        this.tab = new int[4][4];
        initTab();
        shuffleTab();
    }

    /**
     * Inicializa o tabuleiro no estado final (ordenado de 1 a 15 com o vazio na última posição).
     * Define as coordenadas iniciais da peça vazia (3, 3).
     */
    private void initTab(){
        int num = 1;
        //linha
        for(int i = 0; i < 4 ; i++){
            //coluna
            for(int j = 0; j < 4; j++){
                if(i == 3 && j == 3){
                    tab[i][j] = 0;
                    linhaVazia = i;
                    colunaVazia = j;
                }else {tab[i][j] = num++;
                }
            }
        }
    }

    /** @return O número de movimentos que o jogador ainda pode realizar. */
    public int getMovesLeft(){
        return movesLeft;
    }
    /** @return A matriz 4x4 que representa o estado atual do tabuleiro. */
    public int[][] getTabuleiro() {
        return tab;
    }


    /**
     * Baralha o tabuleiro realizando movimentos aleatórios válidos.
     * <p>
     * Este método garante que o puzzle é solúvel, pois parte do estado resolvido
     * e executa um número de trocas definido nas Settings.
     * </p>
     */
    private void shuffleTab(){
        int movesToSuffle = MAX_SUFFLE_PUZZLE;
        int numAleatorio;
        while(movesToSuffle > 0){
            do{
                 numAleatorio = (int) (Math.random() * 15) + 1;
            }while(!movePiece(numAleatorio));
                movesLeft++;
                movesToSuffle--;
        }
    }

    /**
     * Tenta mover uma peça com um determinado número para o espaço vazio.
     * <p>
     * A peça só se move se for adjacente (horizontal ou verticalmente) ao
     * espaço vazio atual e se ainda restarem movimentos disponíveis.
     * </p>
     * @param numero O valor da peça que o utilizador pretende mover.
     * @return {@code true} se o movimento foi bem-sucedido; {@code false} caso contrário.
     */
    public boolean movePiece(int numero){
        int linhaPiece = -1;
        int colunaPiece = -1;

        if(movesLeft <= 0){
            return false;
        }

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                if(tab[i][j] == numero){
                    linhaPiece = i;
                    colunaPiece = j;
                }
            }
        }
        if(linhaPiece == -1 || colunaPiece == -1){
            return false;
        }
        //verifca vizinho
        boolean vertical = false;
        boolean horizontal = false;

        if(colunaPiece == colunaVazia){
            if(Math.abs(linhaPiece-linhaVazia) == 1){
                vertical = true;
            }
        }
        if(linhaPiece == linhaVazia){
            if(Math.abs(colunaPiece-colunaVazia) == 1){
                horizontal = true;
            }
        }

        if(vertical || horizontal){
            tab[linhaVazia][colunaVazia] = tab[linhaPiece][colunaPiece];
            tab[linhaPiece][colunaPiece] = 0;

            linhaVazia = linhaPiece;
            colunaVazia = colunaPiece;
            movesLeft--;
            DeepSeaLog.getInstance().log("[MINIJOGO] Movimento valido");
            return true;
        }

        return false;
    }
    /**
     * Verifica se o puzzle foi resolvido com sucesso.
     * O puzzle considera-se resolvido quando todos os números estão em ordem
     * crescente (1 a 15) e o espaço vazio está na última posição (3,3).
     * * @return {@code true} se o tabuleiro estiver ordenado; {@code false} caso contrário.
     */
    public boolean isResolved(){
       int solucao = 1;
       for (int i = 0; i < 4; i++) {
           for (int j = 0; j < 4; j++) {
               if(i == 3 && j == 3){
                   return tab[i][j] == 0;
               }
               if(tab[i][j] != solucao){
                   return false;
               }
               solucao++;
           }
       }
       return  true;
    }


    /**
     * Gera uma representação visual do puzzle em formato de texto,
     * incluindo os movimentos restantes.
     * * @return String formatada com o estado do tabuleiro.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Puzzle{\n movesLeft=" + movesLeft + "\n");
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                sb.append(tab[i][j]).append("\t");
            }
            sb.append("\n");
        }
        sb.append("}\n");
        return sb.toString();
    }
}
