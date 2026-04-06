package pt.isec.pa.deepsea.model.data;

public class Oficina {
    private Drone drone;
    private Navio navio;
    public Oficina(){}

    public void recebeNavio(Navio navio){
        this.navio=navio;
    }

    public void selecionaDrone(int id){
        this.drone=navio.getDrone(id);
    }

    public void abasteceDrone(){

    }

    public void reparaDrone(){

    }

    public void melhoraDrone(){

    }
}
