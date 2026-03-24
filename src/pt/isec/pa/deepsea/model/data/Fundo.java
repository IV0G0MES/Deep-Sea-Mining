package pt.isec.pa.deepsea.model.data;

import java.util.HashSet;
import java.util.Set;

//Representa APENAS 1 celula do array bidimensional

public class Fundo {
    private Set<IObstaculo> obstaculos; //Array com a lista dos obstaculos existentes
    private Obstaculo obstaculo;
    private boolean visivel;

    public Fundo() {
        this.obstaculos = new HashSet<IObstaculo>();
        this.visivel = false;
    }


}
