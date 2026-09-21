package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;

/**
 * Classe principal de dados que agrega todos os componentes de uma sessão de jogo.
 * <p>
 * Atua como a raiz dos objetos do modelo, mantendo as instâncias do
 * {@link Navio} e da {@link Superficie}. É responsável por inicializar o mundo
 * e fornecer uma representação visual (em texto) do estado da superfície.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class Jogo implements Serializable {

    private static final long serialVersionUID = 1L;


    /** Instância do navio que o jogador controla na superfície. */
    private Navio navio;

    /** Instância da superfície que contém a grelha de exploração e os fossos/fundos. */
    private Superficie superficie;



    /**
     * Construtor da classe Jogo.
     * Inicializa uma nova instância do {@link Navio} e gera uma nova {@link Superficie},
     * criando assim um mundo de jogo pronto a ser explorado.
     */
    public Jogo() {
        this.navio = new Navio();
        this.superficie=new Superficie();
    }


    /**
     * Devolve o objeto que gere a camada da superfície.
     * @return A instância de {@link Superficie} atual.
     */
    public Superficie getSuperficie(){
        return superficie;
    }

    public String[][] getGrelhaFosso(int posNavioX, int posNavioY){
        ElementoSuperficie elemento = superficie.getElementoSuperficie()[posNavioX][posNavioY];
        String[][] matriz = new String[FOSSO][FOSSO];
        for(int i = 0; i < FOSSO; i++){
            for(int j = 0; j < FOSSO; j++){
                if(elemento.getFosso().getElementoFosso(i,j) instanceof Rocha){
                    matriz[i][j] = "R";
                }else if(elemento.getFosso().getElementoFosso(i,j) instanceof AnimalMarinho){
                    matriz[i][j] = "A";
                } else if (elemento.getFosso().getElementoFosso(i, j) instanceof CorrenteMaritima) {
                    matriz[i][j] = "C";
                } else
                    matriz[i][j] = "~";
            }
        }
        int idAtivo = navio.getDroneAtivo();
        if (idAtivo != -1) {
            Drone d = navio.getDrone(idAtivo);
            if (d.getEstado() == EstadoDrone.FOSSO) {
                matriz[d.getPosX()][d.getPosY()] = "D";
            }
        }
        return  matriz;
    }

    public String[][] getGrelhaFundo(int posNavioX, int posNavioY){
        ElementoSuperficie elemento = superficie.getElementoSuperficie()[posNavioX][posNavioY];
        String[][] matriz = new String[FUNDO][FUNDO];
        for (int i = 0; i < FUNDO; i++) {
            for (int j = 0; j < FUNDO; j++) {
                ElementoFundo ef = elemento.getFundo().getElementoFundo(i, j);

                if (!ef.isVisivel()) {
                    matriz[i][j] = "?";
                }
                else {
                    if(ef instanceof Agua) {
                        matriz[i][j] = " ";
                    } else if (ef instanceof Artefacto) {
                        matriz[i][j] = "A";
                    } else if (ef instanceof Monstro) {
                        matriz[i][j] = "M";
                    } else if (ef instanceof Minerio) {
                        matriz[i][j] = "m";
                    } else {
                        matriz[i][j] = " ";
                    }
                }
            }
        }
        return matriz;
    }

    public List<String> getDronesPorCombustivel(){
        List<String> drones = new ArrayList<>();
        for (Drone d : navio.getDronesOrdenadosCombustivel()){
            drones.add(d.toString());
        }
        return drones;
    }

    public List<String> getDronesPorCasco(){
        List<String> drones = new ArrayList<>();
        for (Drone d : navio.getDronesOrdenadosCasco()){
            drones.add(d.toString());
        }
        return  drones;
    }

    public void novoJogo(){ Drone.resetContador();}

    public List<String> getDroneInventario(){
        List<String> res = new ArrayList<>();
        int id = navio.getDroneAtivo();
        if(id != -1){
            Collection<Item> inventario = navio.getDrone(id).getInventario();
            for(Item item : inventario){
                res.add(item.getTipo());
            }
        }
        return res;
    }

    public void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        Drone.resetContador();
        for (Drone d : navio.getDronesOrdenadosCasco()) {
            Drone.recalibrarContador(d.getId());
        }
        Obstaculo.resetContador();
        for (Obstaculo o : superficie.getTodosObstaculos()){
            Obstaculo.recalibrarContador(o.getId());
        }
    }

    /**
     * Devolve o objeto que representa o navio e o seu inventário de drones.
     * @return A instância de {@link Navio} atual.
     */
    public Navio getNavio(){
        return navio;
    }


    /**
     * Gera uma representação visual da grelha de superfície em formato de texto.
     * <p>
     * A representação utiliza os seguintes símbolos:
     * <ul>
     * <li><b>[N]</b> - Posição atual do Navio.</li>
     * <li><b>[A]</b> - Artefacto detetado.</li>
     * <li><b>[?]</b> - Zona de interesse (vizinha de um artefacto).</li>
     * <li><b>[ ]</b> - Mar aberto.</li>
     * </ul>
     * </p>
     * @return String formatada com a visualização do mapa da superfície.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------SUPERFICIE--------\n");
        for(int i = 0; i< SUPERFICIE; i++){
            for(int j = 0; j < SUPERFICIE; j++){
                if(i==navio.getPosX() && j==navio.getPosY()){
                    sb.append("[N] ");
                } else if (superficie.eArtefactoVizinho(i, j) && !superficie.eArtefacto(i, j)) {
                    sb.append("[?] ");
                } else if (superficie.eArtefacto(i, j)) {
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
