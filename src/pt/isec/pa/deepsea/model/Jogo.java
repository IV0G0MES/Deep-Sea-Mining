package pt.isec.pa.deepsea.model;

//COMO SABEMOS O TAMANHO DE CADA UMA DAS 3 GRELHAS?



public class Jogo {
    private int supWidth;
    private int supHeight;
    private Superficie[][] superficie;
    public Jogo(){}

    public void setSupHeight(int supHeight) {
        this.supHeight = supHeight;
    }
    public void setSupWidth(int supWidth) {
        this.supWidth = supWidth;
    }

    public void createSuperficie(){
        superficie[supWidth][supHeight]=new Superficie();
    }
}
