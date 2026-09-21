package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;


/**
 * Classe responsável pela gestão e geração do Fosso, a zona de transição vertical do jogo.
 * <p>
 * Esta classe gere uma grelha bidimensional de {@link ElementoFosso}, controlando a
 * geração de obstáculos (Rochas), fauna (Animais Marinhos) e perigos
 * ambientais (Correntes Marítimas). Inclui algoritmos distintos para o modo normal
 * e para o modo de defesa.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class Fosso implements Serializable{

    private static final long serialVersionUID = 1L;

    /** Grelha bidimensional que armazena os elementos presentes no Fosso. */
    private ElementoFosso[][] fosso;

    /**
     * Construtor da classe Fosso.
     * Inicializa a grelha com as dimensões definidas nas Settings
     * e despoleta os métodos de geração automática de terreno e elementos.
     */
    public Fosso(){
        this.fosso=new ElementoFosso[FOSSO][FOSSO];
        geraRochas();
        geraAnimalCorrente();
    }


    /**
     * Devolve a estrutura completa da grelha do fosso.
     * @return Matriz de {@link ElementoFosso}.
     */
    public ElementoFosso[][] getFosso(){
        return fosso;
    }

    /**
     * Obtém o elemento presente numa coordenada específica.
     * @param x Coordenada da linha (X).
     * @param y Coordenada da coluna (Y).
     * @return O {@link ElementoFosso} na posição indicada ou null se estiver vazia.
     */
    public ElementoFosso getElementoFosso(int x,int y){
        return fosso[x][y];
    }


    /**
     * Gera as paredes de rocha do fosso.
     * <p>
     * Se o MODO_DEFESA estiver ativo, gera paredes retas nas extremidades.
     * Caso contrário, utiliza um algoritmo de oscilação aleatória para criar um percurso
     * irregular, garantindo que a soma das rochas à esquerda e à direita não bloqueia
     * totalmente a passagem do drone.
     * </p>
     */
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

    public List<Obstaculo> getObstaculos(){
        List<Obstaculo> obstaculos=new ArrayList<>();
        for(int i=0;i<FOSSO;i++){
            for(int j=0;j<FOSSO;j++){
                ElementoFosso elemento=fosso[i][j];
                    if (elemento instanceof Obstaculo) {
                        obstaculos.add((Obstaculo) elemento);
                    }
            }
        }
        return obstaculos;
    }


    /**
     * Popula os espaços vazios do fosso com animais marinhos ou correntes marítimas.
     * A geração baseia-se nas probabilidades definidas nas Settings
     */
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

    /**
     * Remove todos os elementos dinâmicos (Animais e Correntes) da grelha,
     * preservando apenas as estruturas rochosas.
     */
    public void limpaAnimaisCorrentes(){
        for (int i = 0; i < FOSSO; i++) {
            for (int j = 0; j < FOSSO; j++) {
                if (fosso[i][j] != null && !(fosso[i][j] instanceof Rocha)) {
                    fosso[i][j] = null;
                }
            }
        }
    }


    /**
     * Gera uma representação visual do Fosso em modo texto.
     * @return String formatada representando a grelha: [R] Rocha, [A] Animal, [C] Corrente.
     */
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
