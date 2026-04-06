package pt.isec.pa.deepsea.model.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pt.isec.pa.deepsea.model.data.Settings.Settings;

public class Drone{
    private static int Proxid = 0;
    private int id;
    public List<Item> Inventario;
    private double casco;
    private double combustivel;
    private String estado;
    private int posX;
    private int posY;

    public Drone(){
        Inventario = new ArrayList<>();
        this.id = ++Proxid;
        this.casco = Settings.MAX_CASCO;
        this.combustivel = Settings.MaxCombDrone;
        this.estado = Settings.ESTADO_DEFAULT;
    }

    public int getId(){
        return this.id;
    }

    public String getEstado(){
        return estado;
    }

    public void setEstado(String newEstado){
        if(this.estado.equals(newEstado))return;
        this.estado = newEstado;
    }

    public Collection<Item> getInventario(){
        return this.Inventario;
    }

    public void recolheItems(){
        this.Inventario.clear();
    }

    public void apanhaItem(Item item){
        Inventario.add(item);
    }

    public void setCombustivel(double qnt){
        if(qnt + this.combustivel >= Settings.MaxCombDrone){
            System.out.println("Quantidade de combustivel acima do permitido,O restante desaguou no mar :(");
            this.combustivel = Settings.MaxCombDrone;
        }else {
            this.combustivel += qnt;
        }
    }

    public double getCombustivel(){
        return this.combustivel;
    }

    public void setCasco(double casco){
        if (casco < 0) {
            this.casco = 0;
        }
        else if(casco  > 100){
            this.casco = 100;
        }
        else this.casco = casco;
    }

    public double getCasco(){
        return this.casco;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY(){
        return this.posY;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Drone [ID: ").append(this.id).append("]");
        sb.append(" | Estado: ").append(this.estado);
        sb.append(" | Casco: ").append(this.casco).append("/").append(Settings.MAX_CASCO);
        sb.append(" | Combustível: ").append(this.combustivel);
        sb.append(" | Carga: ").append(this.Inventario.size()).append(" itens");
        sb.append(" | Posição: (").append(this.posX).append(", ").append(this.posY).append(")");

        return sb.toString();
    }
}
