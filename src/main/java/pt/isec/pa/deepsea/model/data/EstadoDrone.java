package pt.isec.pa.deepsea.model.data;

/**
 * Enumeração que define os possíveis estados operacionais de um Drone.
 * <p>
 * Este enum é utilizado
 * para identificar rapidamente onde o drone se encontra no ciclo de exploração.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public enum EstadoDrone {
    REPOUSO,FOSSO,FUNDO,DESTRUIDO;
}
