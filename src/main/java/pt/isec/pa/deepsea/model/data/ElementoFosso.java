package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;

/**
 * Interface  que define um elemento possível de existir na grelha do Fosso.
 * <p>
 * Ao estender {@link Serializable}, garante que todos os elementos que a
 * implementam (como Correntes ou Obstáculos) podem ser gravados no estado
 * atual do jogo.
 * </p>
 * * @author João Barão,Ivo Gomes,Inês Moreira
 * @version 1.0
 * @see Serializable
 */

public interface ElementoFosso extends Serializable {
    // Esta interface não contem métodos, servindo apenas para polimorfismo
    // nas diferentes classes que representam o Fosso.
}
