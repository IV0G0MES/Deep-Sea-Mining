package pt.isec.pa.deepsea.model.data;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;

public class Minerio extends Item implements ElementoFundo{

    private static final long serialVersionUID = 1L;

    private int id;
    private static int ProxID = 0;
    private int qnt;
    private boolean visivel;

    public Minerio(int qnt){
        this.id = ++ProxID;
        this.qnt = qnt;
    }

    public int getQnt(){
        return this.qnt;
    }

    @Override
    public String getTipo() {
        return "Minerio";
    }

    @Override
    public double getCustoRecolha() {
        return CUSTO_RECOLHA_MINERIO;
    }

    @Override
    public String toString() {
        StringBuilder sb =new StringBuilder();
        sb.append(String.format("Minerio- %d\n Qnt: %d",id,qnt));
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
