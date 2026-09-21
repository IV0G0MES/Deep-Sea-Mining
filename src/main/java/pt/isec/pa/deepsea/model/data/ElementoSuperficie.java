package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;


/**
 * Representa um elemento individual na grelha da superfície do jogo.
 * <p>
 * Esta classe atua como um contentor que agrega os componentes verticais e
 * profundos do mundo: o {@link Fosso} (zona de transição) e o {@link Fundo}
 * (zona de exploração final). Cada coordenada da superfície possui a sua
 * própria instância destes elementos.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class ElementoSuperficie implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Instância do Fosso associada a esta posição da superfície. */
    private Fosso fosso;
    /** Instância do Fundo marinho associada a esta posição da superfície. */
    private Fundo fundo;

    /**
     * Construtor da classe ElementoSuperficie.
     * Inicializa automaticamente novas instâncias de {@link Fosso} e {@link Fundo},
     * despoletando os seus respetivos processos de geração de terreno e elementos.
     */
    public  ElementoSuperficie() {
        fosso=new Fosso();
        fundo=new Fundo();
    }

    /**
     * Devolve o objeto Fosso contido neste elemento.
     * * @return A instância de {@link Fosso} associada a esta coordenada.
     */
    public Fosso getFosso(){
        return fosso;
    }

    /**
     * Devolve o objeto Fundo contido neste elemento.
     * * @return A instância de {@link Fundo} associada a esta coordenada.
     */
    public Fundo getFundo(){
        return fundo;
    }
}
