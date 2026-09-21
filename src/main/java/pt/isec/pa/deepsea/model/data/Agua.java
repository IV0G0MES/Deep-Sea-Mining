package pt.isec.pa.deepsea.model.data;

public class Agua implements ElementoFundo{

    private static final long serialVersionUID = 1L;

    private boolean visivel;

    public Agua(){
        this.visivel = false;
    }

    @Override
    public boolean isVisivel() {
        return this.visivel;
    }

    @Override
    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }

}
