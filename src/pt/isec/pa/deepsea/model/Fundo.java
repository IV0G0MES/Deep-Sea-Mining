package pt.isec.pa.deepsea.model;

import java.util.HashSet;
import java.util.Set;

//Representa APENAS 1 celula do array bidimensional

public class Fundo {
    private Set<IObstaculo> obstaculos; //Array com a lista dos obstaculos existentes
    private Obstaculo obstaculo;

    public Fundo() {
        this.obstaculos = new HashSet<IObstaculo>();
    }
}
