package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;


/**
 * Classe abstrata que serve como base para todos os obstáculos presentes no ambiente do jogo
 * (Fosso).
 * <p>
 * Gere a identificação única e o posicionamento de cada obstáculo, definindo
 * o cálculo de penalizações através do método abstrato(getPenalizacao()).
 * </p>
 * * @author João Barão,Ivo Gomes,Inês Moreira
 * @version 1.0
 */
public abstract class Obstaculo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Gerador de IDs sequenciais para garantir que cada obstáculo é único. */
    private static int ProxID = 1000;
    private int id;
    private int posX;
    private int posY;

    /**
     * Construtor base para a criação de um obstáculo.
     * Atribui automaticamente um ID único(A comecar no 1001) e define a posição inicial.
     * @param x Posição inicial no eixo X.
     * @param y Posição inicial no eixo Y.
     */
    public Obstaculo(int x, int y){
        this.id = ++ProxID;
        this.posX = x;
        this.posY = y;
    }

    /** @return A coordenada X atual do obstáculo. */
    public int getX() {
        return posX;
    }
    /** @param x A nova coordenada X a definir. */
    public void setX(int x) {
        this.posX = x;
    }
    /** @return A coordenada Y atual do obstáculo. */
    public int getY(){
        return posY;
    }
    /** @param y A nova coordenada Y a definir. */
    public void setY(int y){
        this.posY = y;
    }
    /** @return O identificador único atribuído a este obstáculo. */
    public int getId(){
        return this.id;
    }

    public static void recalibrarContador(int idExistente){
        if(idExistente >= ProxID)ProxID = idExistente;
    }

    public static void resetContador(){
        ProxID = 0;
    }
    /**
     * Gera uma representação textual do obstáculo.
     * * @return String contendo o ID e a posição [X, Y].
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Obstaculo:").append("Obstaculo");
        sb.append("ID :").append(id);
        sb.append("Pos: [").append(posX).append(posY).append("]");

        return sb.toString();
    }
}