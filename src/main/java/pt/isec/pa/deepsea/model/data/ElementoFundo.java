package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;

/**
 * Interface que define o comportamento base de todos os elementos que habitam
 * a grelha do Fundo do Mar (ex: Monstros, Minérios, Artefactos).
 * <p>
 * Estabelece as regras da visibilidade, permitindo que o jogo
 * oculte ou revele elementos à medida que o drone explora as profundezas.
 * </p>
 * * @author João Barão,Ivo Gomes,Inês Moreira
 * @version 1.0
 * @see Serializable
 */
public interface ElementoFundo extends Serializable {

    /**
     * Verifica se o elemento está atualmente visível para o jogador.
     * Esta informação é crucial para a renderização na interface e para
     * a lógica de interação do drone.
     * * @return {@code true} se o elemento estiver visível; {@code false} caso contrário.
     */
    boolean isVisivel();

    /**
     * Define o estado de visibilidade do elemento do fundo.
     * Utilizado quando o drone se move para uma posição adjacente
     * ou para a mesma célula que o elemento, "revelando-o".
     * * @param visivel {@code true} para tornar o elemento visível, {@code false} para ocultar.
     */
    void setVisivel(boolean visivel);
}
