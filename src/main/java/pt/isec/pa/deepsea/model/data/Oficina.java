package pt.isec.pa.deepsea.model.data;

import pt.isec.pa.deepsea.model.utils.DeepSeaLog;

/**
        * Classe responsável pela gestão de manutenção e melhorias dos drones.
        * <p>
 * A Oficina permite reparar a integridade do casco, reabastecer combustível e
 * aplicar melhorias aos drones (tanque e casco),
 * utilizando recursos armazenados no {@link Navio} (combustível e minérios).
        * </p>
        *
        * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
        */

public class Oficina {

    private static final long serialVersionUID = 1L;

    /** Referência para o navio, de onde serão retirados os recursos para as operações. */
    private final Navio navio;
    /** O drone que está atualmente a ser intervencionado na oficina. */
    private Drone droneEmArranjo;
    /**
     * Construtor da Oficina.
     * @param navio Instância do navio proprietário da oficina.
     */
    public Oficina(Navio navio) {
        this.navio = navio;
        this.droneEmArranjo = null;
    }

    /**
     * Tenta selecionar um drone para manutenção.
     * O drone só pode ser selecionado se estiver no estado {@link EstadoDrone#REPOUSO}.
     *
     * @param idDrone Identificador único do drone.
     * @return {@code true} se o drone foi selecionado com sucesso; {@code false} caso contrário.
     */
    public boolean selecionarDrone(int idDrone) {
        Drone d = navio.getDrone(idDrone);
        if (d != null && d.getEstado() == EstadoDrone.REPOUSO) {
            this.droneEmArranjo = d;
            return true;
        }
        return false;
    }

    /**
     * Realiza a reparação do casco de um drone.
     * <p>
     * A reparação consome combustível do navio numa proporção de 0.2 unidades de combustível
     * por cada unidade de casco recuperada.
     * </p>
     *
     * @param idDrone Identificador do drone a reparar.
     * @param qntCascoRecuperar Quantidade desejada de integridade a recuperar.
     * @return {@code true} se a reparação foi efetuada; {@code false} se o drone não puder
     *         ser selecionado, já tiver o casco no máximo ou o navio não tiver combustível suficiente.
     */
    public boolean reparaCasco(int idDrone, double qntCascoRecuperar) {
        if (!selecionarDrone(idDrone)) return false;

        if (droneEmArranjo.getCasco() >= droneEmArranjo.getCascoMax()) {
            DeepSeaLog.getInstance().log("[OFICINA]: Drone " + idDrone + " já tem o casco no máximo.");
            return false;
        }

        double reparacao = Math.min(droneEmArranjo.getCascoMax() - droneEmArranjo.getCasco(), qntCascoRecuperar);
        double custoReparacao = reparacao * 0.2;

        if (navio.getCombustivel() < custoReparacao) {
            DeepSeaLog.getInstance().log("[OFICINA]: Falha na reparação do Drone " + idDrone + " (Combustível insuficiente).");
            return false;
        }

        navio.gastaCombustivel(custoReparacao);
        droneEmArranjo.setCasco(droneEmArranjo.getCasco() + reparacao);
        DeepSeaLog.getInstance().log("[OFICINA]: Sucesso na reparação do Drone " + idDrone + ". Casco: " + (int) droneEmArranjo.getCasco());
        return true;
    }

    /**
     * Transfere combustível do reservatório do navio para o tanque do drone.
     *
     * @param idDrone Identificador do drone a abastecer.
     * @param qnt Quantidade de combustível a transferir.
     * @return {@code true} se o abastecimento foi concluído; {@code false} se houver falha na
     *         seleção ou combustível insuficiente no navio.
     */
    public boolean abastecerDrone(int idDrone, double qnt) {
        if (!selecionarDrone(idDrone)) {
            return false;
        }
        if (navio.getCombustivel() < qnt) {
            DeepSeaLog.getInstance().log("[OFICINA]: Falha no abastecimento do Drone n-" + droneEmArranjo.getId());
            return false;
        }
        navio.gastaCombustivel(qnt);
        droneEmArranjo.setCombustivel(qnt);
        DeepSeaLog.getInstance().log("[OFICINA]: Drone n-" + droneEmArranjo.getId() + "abastecido");
        return true;
    }

    /**
     * Aplica uma melhoria permanente na capacidade do tanque de combustível do drone.
     * <p>
     * O custo em minérios depende do nível atual (Nível 0: 20, Nível 1: 30, Nível 2: 40).
     * Existe um limite global de 5 upgrades totais por drone.
     * </p>
     *
     * @param idDrone Identificador do drone.
     * @return {@code true} se o upgrade foi aplicado; {@code false} se atingir o limite de 5 upgrades,
     *         se não houver minérios suficientes ou se o nível máximo de combustível for atingido.
     */
    public boolean melhorarCapacidadeCombustivel(int idDrone) {
        if (!selecionarDrone(idDrone)) return false;

        if (droneEmArranjo.getNUpgrades() >= 5) {
            DeepSeaLog.getInstance().log("[OFICINA]: Drone " + idDrone + " atingiu o limite de 5 upgrades.");
            return false;
        }

        int nivel = droneEmArranjo.getNivelUpgradeCombustivel();
        int custoMinerios = (nivel == 0) ? 20 : (nivel == 1) ? 30 : (nivel == 2) ? 40 : -1;

        if (custoMinerios == -1) return false;

        // LOG DE FALHA: Recursos
        if (navio.getQntMinerios() < custoMinerios) {
            DeepSeaLog.getInstance().log("[OFICINA]: Minérios insuficientes para upgrade de Tanque (" + custoMinerios + " necessários).");
            return false;
        }

        navio.setQntMinerios(-custoMinerios);

        if (nivel == 0) droneEmArranjo.aumentarCapacidadeCombustivel(20.0);
        else if (nivel == 1) droneEmArranjo.aumentarCapacidadeCombustivel(30.0);
        else if (nivel == 2) droneEmArranjo.aumentarCapacidadeCombustivel(50.0);

        droneEmArranjo.aumentarNUpgrades();

        DeepSeaLog.getInstance().log("[OFICINA]: Upgrade Tanque Nível " + (nivel + 1) + " concluído no Drone " + idDrone);
        return true;
    }

    /**
     * Aplica uma melhoria permanente na resistência máxima do casco do drone.
     * <p>
     * O custo em minérios depende do nível atual (Nível 0: 20, Nível 1: 25, Nível 2: 60).
     * Além do aumento da capacidade máxima, o upgrade também recupera o casco do drone.
     * </p>
     *
     * @param idDrone Identificador do drone.
     * @return {@code true} se o upgrade foi aplicado; {@code false} se atingir o limite de 5 upgrades,
     *         se não houver minérios suficientes ou se o nível máximo de blindagem for atingido.
     */
    public boolean melhorarCasco(int idDrone) {
        if(!selecionarDrone(idDrone)) return false;
        if (droneEmArranjo.getNUpgrades() >= 5) {
            DeepSeaLog.getInstance().log("[OFICINA]: Drone " + idDrone + " atingiu o limite de 5 upgrades.");
            return false;
        }

        int nivel = droneEmArranjo.getNivelUpgradeCasco();
        int custoMinerios = (nivel == 0) ? 20 : (nivel == 1) ? 25 : (nivel == 2) ? 60 : -1;

        if (custoMinerios == -1) return false;

        if (navio.getQntMinerios() < custoMinerios) {
            DeepSeaLog.getInstance().log("[OFICINA]: Minérios insuficientes para upgrade de Blindagem (" + custoMinerios + " necessários).");
            return false;
        }

        navio.setQntMinerios(-custoMinerios);

        if (nivel == 0) droneEmArranjo.aumentarCascoMaximo(20.0);
        else if (nivel == 1) droneEmArranjo.aumentarCascoMaximo(10.0);
        else if (nivel == 2) droneEmArranjo.aumentarCascoMaximo(50.0);

        droneEmArranjo.aumentarNUpgrades();

        DeepSeaLog.getInstance().log("[OFICINA]: Upgrade Blindagem Nível " + (nivel + 1) + " concluído no Drone " + idDrone);
        return true;
    }
}