package pt.isec.pa.deepsea.model.data;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;

public class Jogo {
    private Navio navio;
    private Superficie superficie;
    private Oficina oficina;
    public Jogo() {
        this.navio = new Navio();
        this.superficie=new Superficie();
        this.oficina=new Oficina();
    }

    public Superficie getSuperficie(){
        return superficie;
    }


    public void enviaNavioOficina(){
        oficina.recebeNavio(navio);
    }

    public boolean reparaCasco(int custo,int reparacao,int idDrone){
        navio.getDrone(idDrone).setCasco(MAX_CASCO);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("--------SUPERFICIE--------\n");
        for(int i = 0; i< SUPERFICIE; i++){
            for(int j = 0; j < SUPERFICIE; j++){
                if(i==navio.getposX() && j==navio.getposY()){
                    sb.append("[N] ");
                } else if (superficie.eArtefactoVizinho(i, j) && !superficie.eArtefacto(i, j)) {
                    sb.append("[?] ");
                } else if (superficie.eArtefacto(i, j)) {
                    sb.append("[A] ");
                } else {
                    sb.append("[ ] ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
