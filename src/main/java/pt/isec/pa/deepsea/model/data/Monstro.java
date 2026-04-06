package pt.isec.pa.deepsea.model.data;

public class Monstro extends Obstaculo implements ElementoFundo{
    boolean visivel;


    public Monstro(int x,int y){
        super(x,y);
        this.visivel = false;
    }

    @Override
    public double getPenalizacao() {
        return 0;
    }

    @Override
    public boolean isVisivel(){
        return this.visivel;
    }

    @Override
    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }
}
