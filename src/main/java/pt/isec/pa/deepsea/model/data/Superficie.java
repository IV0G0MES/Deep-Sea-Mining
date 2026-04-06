package pt.isec.pa.deepsea.model.data;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;

public class Superficie {
    private ElementoSuperficie[][] superficie;
    public  Superficie() {
        this.superficie=new ElementoSuperficie[SUPERFICIE][SUPERFICIE];
        for (int i = 0; i < SUPERFICIE; i++) {
            for (int j = 0; j < SUPERFICIE; j++) {
                superficie[i][j] = new ElementoSuperficie();
            }
        }
        sorteiaArtefacto();
    }

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

    public boolean eArtefacto(int x, int y) {
        if (x < 0 || x >= SUPERFICIE || y < 0 || y >= SUPERFICIE) {
            return false;
        }
        return superficie[x][y].getFundo().eArtefacto();
    }

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------SUPERFICIE--------\n");
        for(int i = 0; i< SUPERFICIE; i++){
            for(int j = 0; j < SUPERFICIE; j++){
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