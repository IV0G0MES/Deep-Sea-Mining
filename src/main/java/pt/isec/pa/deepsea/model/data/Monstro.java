package pt.isec.pa.deepsea.model.data;

public class Monstro extends Obstaculo implements ElementoFundo{

    private static final long serialVersionUID = 1L;

    private boolean visivel;


    public Monstro(int x,int y){
        super(x,y);
        this.visivel = false;
    }


    @Override
    public boolean isVisivel(){
        return this.visivel;
    }

    @Override
    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }

}
