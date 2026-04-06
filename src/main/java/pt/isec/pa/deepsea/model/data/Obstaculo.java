package pt.isec.pa.deepsea.model.data;

public abstract class Obstaculo{
    private static int ProxID = 1001;
    private int id;
    private int posX;
    private int posY;


    public Obstaculo(int x, int y){
        this.id = ++ProxID;
        this.posX = x;
        this.posY = y;
    }

    public int getX() {
        return posX;
    }
    public void setX(int x) {
        this.posX = x;
    }

    public int getY(){
        return posY;
    }

    public void setY(int y){
        this.posY = y;
    }

    public int getId(){
        return this.id;
    }

    public abstract double getPenalizacao();

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Obstaculo:").append("Obstaculo");
        sb.append("ID :").append(id);
        sb.append("Pos: [").append(posX).append(posY).append("]");

        return sb.toString();
    }
}