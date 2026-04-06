package pt.isec.pa.deepsea.model.data;

public class Rocha extends Obstaculo implements ElementoFosso{
    private int posX;
    private int posY;

    public Rocha( int x,int y){
        super(x,y);
    }

    @Override
    public double getPenalizacao() {
        return 0;
    }

}
