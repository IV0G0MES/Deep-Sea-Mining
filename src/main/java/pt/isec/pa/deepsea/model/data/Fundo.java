package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.data.Settings.Settings;

public class Fundo{
    private ElementoFundo[][] fundo;
    private int qntArtefactos;

    public Fundo(){
        fundo = new ElementoFundo[Settings.FUNDO][Settings.FUNDO];
        qntArtefactos=0;
        geraAgua();
        geraElementosFundo();
    }

    public ElementoFundo[][] getFundo(){return fundo;}

    public ElementoFundo getElementoFundo(int x,int y){return fundo[x][y];}

    public void geraElementosFundo(){
        int qntMinerios = 0;
        int nMonstros = (int) (Math.random() * (Settings.MAXNUM_MONSTROS)) + 1;
        for(int i = 0; i < nMonstros;i++){
            geraMonstros();
        }
        double FundoRico = (Math.random() * 1);
        if(FundoRico > Settings.FundoRico){
            qntMinerios = Settings.NMAX_MINERIO;
        }else {
            qntMinerios = (int) (Math.random() * Settings.NMAX_MINERIO) + 1;
        }
        for(int i = 0;i < qntMinerios;i++){
            int randQuant = (int)(Math.random() * Settings.QNT_MAX_MINERIO) + 1;
            geraMinerios(randQuant);
        }
        if(Settings.MODO_DEFESA){
            geraMonstros();
            geraMinerios(Settings.QNT_MAX_MINERIO);
            geraMinerios(1);
            FundoVisivel();
        }
    }

    public boolean eArtefacto(){
        return qntArtefactos>0;
    }

    private void FundoVisivel(){
        for (int i = 0; i < Settings.FUNDO; i++) {
            for (int j = 0; j < Settings.FUNDO; j++) {
                fundo[i][j].setVisivel(true);
            }
        }
    }

    private void geraAgua(){
        for (int i = 0; i < Settings.FUNDO; i++) {
            for (int j = 0; j < Settings.FUNDO; j++) {
                if(!(fundo[i][j] instanceof Artefacto || fundo[i][j] instanceof Minerio || fundo[i][j] instanceof Monstro)){
                    fundo[i][j] = new Agua();
                }
            }
        }
    }

    public void geraMinerios(int qnt){
        int posXMin;
        int posYMin;
        do {
            posXMin = (int) (Math.random() * Settings.FUNDO);
            posYMin = (int) (Math.random() * Settings.FUNDO);
        }while(!(fundo[posXMin][posYMin] instanceof Agua));
        fundo[posXMin][posYMin] = new Minerio(qnt,posXMin, posYMin);
    }

    public void geraMonstros(){
        int posXMon;
        int posYMon;

        do {
            posXMon = (int) (Math.random() * Settings.FUNDO);
            posYMon = (int) (Math.random() * Settings.FUNDO);
        } while (!(fundo[posXMon][posYMon] instanceof Agua));
        fundo[posXMon][posYMon] = new Monstro(posXMon, posYMon);
    }

    public void colocarArtefacto(){
        int posXArt;
        int posYArt;

        do {
            posXArt = (int) (Math.random() * Settings.FUNDO);
            posYArt = (int) (Math.random() * Settings.FUNDO);
        } while (!(fundo[posXArt][posYArt] instanceof Agua));

        fundo[posXArt][posYArt] = new Artefacto(posXArt, posYArt);
        qntArtefactos++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < Settings.FUNDO; i++) {
            for (int j = 0; j < Settings.FUNDO; j++) {

                ElementoFundo elemento = fundo[i][j];
                if (!elemento.isVisivel() && !Settings.MODO_DEFESA) {
                    sb.append("[?] ");
                    continue;
                }
                switch (elemento) {
                    case Agua agua -> sb.append("[ ]");
                    case Monstro monstro -> sb.append("[M] ");
                    case Minerio minerio -> sb.append("[m] ");
                    case Artefacto artefacto -> sb.append("[*] ");
                    default -> {
                    }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
