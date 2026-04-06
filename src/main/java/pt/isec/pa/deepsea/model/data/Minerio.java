package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.data.Settings.Settings;

public class Minerio extends Item implements ElementoFundo{
    private int id;
    private static int ProxID = 0;
    int posx;
    int posy;
    private int qnt;
    private boolean visivel;

    public Minerio(int qnt,int posx,int posy){
        this.posx = posx;
        this.posy = posy;
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
        return Settings.CUSTO_RECOLHA_MINERIO;
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
