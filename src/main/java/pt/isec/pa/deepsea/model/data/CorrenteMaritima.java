package pt.isec.pa.deepsea.model.data;

public class CorrenteMaritima extends Obstaculo implements ElementoFosso{
    public CorrenteMaritima(int x,int y){
        super(x,y);
    }

    @Override
    public double getPenalizacao() {
        return 0;
    }

}
