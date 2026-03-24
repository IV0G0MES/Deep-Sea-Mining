package pt.isec.pa.deepsea.model.data;

//Representa APENAS 1 celula do array bidimensional

public class Superficie {
    //Tamanho dos arrays bidimensionais dos fossos
    private int fossoWidth;
    private int fossoHeight;

    //Tamanho dos arrays bidimensionais do fundo do mar
    private int fundoWidth;
    private int fundoHeight;

    private Fosso[][] fosso; //Tem de ser um array bidimensional
    private Fundo[][] fundo; //Tem de ser um array bidimensional

    public  Superficie() {
    }

    public void createFosso(){
        fosso[fossoWidth][fossoHeight]=new Fosso();
    }

    public void createFundo(){
        fundo[fundoWidth][fundoHeight]=new Fundo();
    }
}
