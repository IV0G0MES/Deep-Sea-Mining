package pt.isec.pa.deepsea.model.data.Settings;

public interface Settings{
    boolean MODO_DEFESA = false;

    //Drone
    double MAX_CASCO = 100.0;
    double MaxCombDrone = 100.0;
    String ESTADO_DEFAULT = "Repouso";
    int TAM_MAX_INVENTARIO_DRONE = 100;

    //settings grids
    int SUPERFICIE=15;
    int FUNDO=15;
    int FOSSO=15;
    int N_MAX_ARTEFACTOS = 10;
    int N_ARTEFACTOS = (int) (Math.random() * N_MAX_ARTEFACTOS) + 1;

    //settings para gerar componentes das grids
    int PROB_ANIMAL=5;
    int PROB_CORRENTE=5;
    int PROB_OBSTACULO=10;

    //minerio
    int NMAX_MINERIO = 4;
    int QNT_MAX_MINERIO = 10;
    double CUSTO_RECOLHA_MINERIO = 5;

    //Monstro
    int MAXNUM_MONSTROS = 4;

    //puzzle
    int MAX_MOVES_PUZZLE = 60;
    int MAX_SUFFLE_PUZZLE = 50;

    //SettingsFundo
    double FundoRico = 0.92;
}
