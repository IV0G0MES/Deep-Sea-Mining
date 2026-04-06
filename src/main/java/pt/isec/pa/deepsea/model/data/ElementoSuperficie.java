package pt.isec.pa.deepsea.model.data;

public class ElementoSuperficie {
    private Fosso fosso;
    private Fundo fundo;

    public  ElementoSuperficie() {
        fosso=new Fosso();
        fundo=new Fundo();
    }

    public Fosso getFosso(){
        return fosso;
    }

    public Fundo getFundo(){
        return fundo;
    }
}
