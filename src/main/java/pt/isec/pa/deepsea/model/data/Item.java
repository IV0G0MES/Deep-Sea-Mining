package pt.isec.pa.deepsea.model.data;

import java.io.Serializable;


/**
 * Classe abstrata que representa um objeto recolhível no mundo submarino.
 * <p>
 * Serve como base para todos os recursos (ex: Minérios) e objetos especiais (ex: Artefactos)
 * que o drone pode encontrar e armazenar. Define a interface necessária para identificar
 * o tipo de item e o custo energético associado à sua extração.
 * </p>
 * * @author João Barão,Ivo Gomes,Inês Moreira
 * @version 1.0
 */
public abstract class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    public Item(){}

    /**
     * Devolve a categoria ou designação do item.
     * Este método deve ser implementado pelas subclasses para identificar se
     * se trata de um "Minério" ou "Artefacto".
     * * @return String contendo o tipo do item.
     */
    public abstract String getTipo();

    /**
     * Define o custo de combustível necessário para que o drone consiga
     * realizar a recolha deste item do fundo do mar.
     * * @return O valor decimal (double) correspondente ao custo de recolha.
     */
    public abstract double getCustoRecolha();

    /**
     * Devolve uma representação textual detalhada do item.
     * Obriga as subclasses a implementar uma descrição que facilite a
     * visualização no log e na interface do utilizador.
     * * @return String descritiva do item.
     */
    public abstract String toString();
}
