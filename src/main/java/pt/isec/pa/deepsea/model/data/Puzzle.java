package pt.isec.pa.deepsea.model.data;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.MAX_SUFFLE_PUZZLE;

public class Puzzle {
    private int[][] tab;
    private int movesLeft;

    //coordenadas do bloco vazio
    private int linhaVazia;
    private int colunaVazia;

    //construtor
    public Puzzle (int maxMoves){
        this.movesLeft = maxMoves;
        this.tab = new int[4][4];
        initTab();
        shuffleTab();
    }

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

    //getters
    public int getMovesLeft(){
        return movesLeft;
    }
    public int[][] getTabuleiro() {
        return tab;
    }

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
            return true;
        }

        return false;
    }

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
