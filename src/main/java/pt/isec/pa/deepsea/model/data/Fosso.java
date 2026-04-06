package pt.isec.pa.deepsea.model.data;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;

public class Fosso{
    private ElementoFosso[][] fosso;
    public Fosso(){
        this.fosso=new ElementoFosso[FOSSO][FOSSO];
        geraRochas();
        geraAnimalCorrente();
    }

    public ElementoFosso[][] getFosso(){
        return fosso;
    }

    public ElementoFosso getElementoFosso(int x,int y){
        return fosso[x][y];
    }

    public void geraRochas(){
        if(MODO_DEFESA){
            for (int i = 0; i < FOSSO; i++) {
                fosso[i][0] = new Rocha(i, 0);
                fosso[i][FOSSO - 1] = new Rocha(i, FOSSO - 1);
            }
        }else {
            int rochasEsquerdaAux = 0;
            int rochasDireitaAux = 0;
            int rochasLimite = FOSSO / 2;
            for (int i = 0; i < FOSSO; i++) {
                int rochasEsquerda;
                int rochasDireita;
                if (i == 0) {
                    rochasEsquerda = (int) (Math.random() * (rochasLimite + 1));
                    rochasDireita = (int) (Math.random() * (rochasLimite - rochasEsquerda + 1));
                } else {
                    do {
                        rochasEsquerda = rochasEsquerdaAux;
                        rochasDireita = rochasDireitaAux;
                        int oscilacaoEsquerda = (int) (Math.random() * 3) - 1;
                        int oscilacaoDireita = (int) (Math.random() * 3) - 1;
                        if (oscilacaoEsquerda < 0) {
                            rochasEsquerda--;
                        } else if (oscilacaoEsquerda > 0) {
                            rochasEsquerda++;
                        }
                        if (oscilacaoDireita < 0) {
                            rochasDireita--;
                        } else if (oscilacaoDireita > 0) {
                            rochasDireita++;
                        }
                        if (rochasEsquerda < 0) {
                            rochasEsquerda = 0;
                        }
                        if (rochasDireita < 0) {
                            rochasDireita = FOSSO;
                        }
                    } while ((rochasEsquerda + rochasDireita) > rochasLimite);
                }

                for (int j = 0; j < rochasEsquerda; j++) {
                    fosso[i][j] = new Rocha(i, j);
                }
                for (int j = FOSSO - 1; j >= (FOSSO - rochasDireita); j--) {
                    fosso[i][j] = new Rocha(i, j);
                }
                rochasEsquerdaAux = rochasEsquerda;
                rochasDireitaAux = rochasDireita;
            }
        }
    }

    public void geraAnimalCorrente() {
        if(MODO_DEFESA) {
            int meioFosso=FOSSO/2;
            fosso[meioFosso][meioFosso] = new AnimalMarinho(meioFosso, meioFosso);
            fosso[meioFosso + 1][meioFosso] = new CorrenteMaritima(meioFosso + 1, meioFosso);
        }else {
            for (int i = 0; i < FOSSO; i++) {
                for (int j = 0; j < FOSSO; j++) {
                    if (fosso[i][j] == null) {
                        double sorteio = Math.random() * 100; // [0,100]
                        if (sorteio < 50) {
                            double probAnimal=Math.random()*100;
                            if(probAnimal<=PROB_ANIMAL) {
                                fosso[i][j] = new AnimalMarinho(i, j);
                            }
                        } else if (sorteio >=50) {
                            double probCorrente=Math.random()*100;
                            if(probCorrente<=PROB_CORRENTE) {
                                fosso[i][j] = new CorrenteMaritima(i, j);
                            }
                        }
                    }
                }
            }
        }
    }

    public void limpaAnimaisCorrentes(){
        for (int i = 0; i < FOSSO; i++) {
            for (int j = 0; j < FOSSO; j++) {
                if (fosso[i][j] != null && !(fosso[i][j] instanceof Rocha)) {
                    fosso[i][j] = null;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-------FOSSO-------\n");

        for (int i = 0; i < FOSSO; i++) {
            for (int j = 0; j < FOSSO; j++) {
                ElementoFosso elementoFosso = fosso[i][j];
                if(elementoFosso == null) {
                    sb.append("[ ] ");
                    continue;
                }

                switch (elementoFosso){
                    case Rocha rocha -> sb.append("[R] ");
                    case AnimalMarinho animalMarinho -> sb.append("[A] ");
                    case CorrenteMaritima correnteMarinho -> sb.append("[C] ");
                    default ->  sb.append("[?] ");
                }
            }
            sb.append("\n");
        }
        return  sb.toString();
    }
}
