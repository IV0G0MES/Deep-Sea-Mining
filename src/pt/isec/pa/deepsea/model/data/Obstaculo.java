package pt.isec.pa.deepsea.model.data;

public class Obstaculo implements IObstaculo {
    //coordenadas onde o obstaculo se encontra no array onde esta inserido
    private int x;
    private int y;
    public Obstaculo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getX(int x) {
        return x;
    }
}
