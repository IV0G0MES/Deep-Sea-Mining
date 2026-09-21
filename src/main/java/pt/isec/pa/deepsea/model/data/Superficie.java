package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;

/**
 * Classe responsável pela gestão da grelha de superfície do mundo de jogo.
 * <p>
 * Gere uma matriz bidimensional de {@link ElementoSuperficie}, controlando a
 * inicialização do mundo e a distribuição estratégica ou aleatória de artefactos
 * nas camadas profundas (Fundo). Implementa também a lógica de "radar" para
 * deteção de proximidade de artefactos.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class Superficie implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Matriz bidimensional que representa o mapa da superfície. */
    private ElementoSuperficie[][] superficie;

    /**
     * Construtor da classe Superficie.
     * Inicializa a grelha com as dimensões definidas nas Settings,
     * instancia cada célula e despoleta a distribuição inicial de artefactos.
     */
    public Superficie() {
        this.superficie = new ElementoSuperficie[SUPERFICIE][SUPERFICIE];
        for (int i = 0; i < SUPERFICIE; i++) {
            for (int j = 0; j < SUPERFICIE; j++) {
                superficie[i][j] = new ElementoSuperficie();
            }
        }
        sorteiaArtefacto();
    }

    /**
     * Devolve a grelha completa de elementos da superfície.
     * @return Matriz de {@link ElementoSuperficie}.
     */
    public ElementoSuperficie[][] getElementoSuperficie() {
        return superficie;
    }

    /**
     * Realiza a distribuição dos artefactos no fundo do mar associado a cada célula.
     * <p>
     * No <b>MODO_DEFESA</b>, os artefactos são colocados em zonas específicas e fixas
     * no centro do mapa. No modo normal, a distribuição é puramente aleatória através
     * da grelha, respeitando a quantidade definida nas Settings.
     * </p>
     */
    public void sorteiaArtefacto() {
        if (MODO_DEFESA) {
            int zona1X = SUPERFICIE / 2;
            int zona1Y = SUPERFICIE / 2;
            int zona2X = zona1X;
            int zona2Y = zona1Y - 1;
            for (int i = 0; i < N_ARTEFACTOS; i++) {
                if (i % 2 == 0) {
                    superficie[zona1X][zona1Y].getFundo().colocarArtefacto();
                } else {
                    superficie[zona2X][zona2Y].getFundo().colocarArtefacto();
                }
            }
        } else {
            for (int i = 0; i < N_ARTEFACTOS; i++) {
                int randX = (int) (Math.random() * SUPERFICIE);
                int randY = (int) (Math.random() * SUPERFICIE);
                superficie[randX][randY].getFundo().colocarArtefacto();
            }
        }
    }

    public List<Obstaculo> getTodosObstaculos(){
        List<Obstaculo> obstaculos=new ArrayList<>();
        for(int i=0;i<SUPERFICIE;i++){
            for(int j=0;j<SUPERFICIE;j++) {
                obstaculos.addAll(superficie[i][j].getFundo().getObstaculos());
                obstaculos.addAll(superficie[i][j].getFosso().getObstaculos());
            }
        }
        return List.copyOf(obstaculos);
    }

    /**
     * Verifica se existe um artefacto depositado no fundo da coordenada especificada.
     * @param x Coordenada da linha.
     * @param y Coordenada da coluna.
     * @return {@code true} se existir um artefacto na posição (x,y); {@code false} caso contrário.
     */
    public boolean eArtefacto(int x, int y) {
        if (x < 0 || x >= SUPERFICIE || y < 0 || y >= SUPERFICIE) {
            return false;
        }
        return superficie[x][y].getFundo().eArtefacto();
    }

    /**
     * Implementa a lógica de proximidade (radar).
     * Verifica se alguma das 8 células adjacentes à coordenada atual contém um artefacto.
     * @param x Coordenada da linha a verificar.
     * @param y Coordenada da coluna a verificar.
     * @return {@code true} se houver pelo menos um artefacto na vizinhança; {@code false} caso contrário.
     */
    public boolean eArtefactoVizinho(int x, int y) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                int vizinhoX = x + i;
                int vizinhoY = y + j;
                if (eArtefacto(vizinhoX, vizinhoY)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gera uma representação visual da superfície em modo texto.
     * <p>
     * Utiliza símbolos para distinguir células vazias [ ], artefactos detetados [A]
     * e zonas de proximidade detectadas pelo radar [?].
     * </p>
     * @return String formatada com o estado visual da grelha.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------SUPERFICIE--------\n");
        for(int i = 0; i < SUPERFICIE; i++) {
            for(int j = 0; j < SUPERFICIE; j++) {
                if (eArtefactoVizinho(i, j) && !eArtefacto(i, j)) {
                    sb.append("[?] ");
                } else if (eArtefacto(i, j)) {
                    sb.append("[A] ");
                } else {
                    sb.append("[ ] ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}