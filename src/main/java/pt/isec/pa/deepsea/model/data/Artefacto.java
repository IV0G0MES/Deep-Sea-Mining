package pt.isec.pa.deepsea.model.data;

import java.util.ArrayList;

public class Artefacto extends Item implements ElementoFundo{

    private static final long serialVersionUID = 1L;

    private int id;
    private static int Proxid = 0;
    private boolean visivel;

    public Artefacto(){
        this.visivel = false;
        this.id = ++Proxid;
    }

    public int getId(){
        return this.id;
    }

    @Override
    public String getTipo() {
        return "Artefacto";
    }

    @Override
    public double getCustoRecolha() {
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder sb =new StringBuilder();
        sb.append(String.format("Artefacto- %d\n",id));
        return sb.toString();
    }

    @Override
    public boolean isVisivel() {
        return this.visivel;
    }

    @Override
    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }

}
