package pt.isec.pa.deepsea.model.data;

public class AnimalMarinho extends Obstaculo implements ElementoFosso{
    public AnimalMarinho(int x,int y){
        super(x,y);
    }

    @Override
    public double getPenalizacao() {
        return 0;
    }
}
