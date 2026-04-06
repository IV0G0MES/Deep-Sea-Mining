package pt.isec.pa.deepsea.model.data;

import java.util.*;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.FOSSO;
import static pt.isec.pa.deepsea.model.data.Settings.Settings.SUPERFICIE;

public class Navio {
    private ArrayList<Artefacto> artefactos;
    private Set<Drone> drones;
    private int posX;
    private int posY;
    private int qntMinerios;

    public Navio(){
        artefactos = new ArrayList<>();
        drones=new HashSet<>();
        for(int i=0;i<3;i++){
            drones.add(new Drone());
        }
        this.posX = SUPERFICIE/2-1;
        this.posY = SUPERFICIE/2-1;
    }
    public ArrayList<Artefacto> getArtefactos() {
        return artefactos;
    }


    public Set<Drone> getDrones(){
        return drones;
    }

    public List<Drone> getDronesOrdenadosCombustivel(){
        List<Drone> dronesOrdenados = new ArrayList<>(this.drones);
        dronesOrdenados.sort(Comparator.comparingDouble(Drone::getCombustivel).reversed());
        return dronesOrdenados;
    }

    public List<Drone> getDronesOrdenadosCasco(){
        List<Drone> dronesOrdenados = new ArrayList<>(this.drones);
        dronesOrdenados.sort(Comparator.comparingDouble(Drone::getCasco).reversed());
        return dronesOrdenados;
    }

    public Drone getDrone(int id){
        for(Drone drone:drones){
            if(drone.getId()==id){
                return drone;
            }
        }
        return null;
    }

    public int getposX(){
        return posX;
    }

    public int getposY(){
        return posY;
    }

    public boolean EntraFosso(int id){
        for(Drone drone:drones){
                if (drone.getId() == id && (!drone.getEstado().equals("Ativo")) && (!drone.getEstado().equals("Perdido"))){
                drone.setPosX(0);
                drone.setPosY(FOSSO/2);
                return true;
            }
        }
        return false;
    }

    public boolean recolheDrone(int id){
        for(Drone drone:drones){
            if(drone.getId()==id && drone.getEstado().equals("Ativo")) {
                for(Item item : drone.getInventario()){
                    if(item instanceof Artefacto){
                        this.artefactos.add((Artefacto) item);
                    }
                    else if(item instanceof Minerio){
                        Minerio min = (Minerio) item;
                        this.qntMinerios += min.getQnt();
                    }
                }

                drone.recolheItems();

                drone.setPosX(-1);
                drone.setPosY(-1);

                drone.setEstado("Repouso");

                return true;
            }
        }
        return false;
    }

    public Artefacto getArtefacto(int id){
        for(Artefacto artefacto : artefactos){
            if(artefacto.getId() == id){
                return artefacto;
            }
        }
        return null;
    }

    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("Artefactos Recolhidos:").append("\n");
        for(Artefacto artefacto:artefactos){
            sb.append(artefacto.toString());
        }
        sb.append("Quantidade de Minerios->").append(qntMinerios).append("\n");
        return sb.toString();
    }
}