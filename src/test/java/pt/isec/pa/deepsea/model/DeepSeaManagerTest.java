package pt.isec.pa.deepsea.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.isec.pa.deepsea.model.data.Artefacto;
import pt.isec.pa.deepsea.model.state.DeepSeaState;
import pt.isec.pa.deepsea.model.utils.DeepSeaLog;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeepSeaManagerTest {
    private DeepSeaManager manager;

    @BeforeEach
    void setUp() {
        manager = new DeepSeaManager();
        DeepSeaLog.getInstance().reset();
    }


    @Test
    void testeListasInitVazia() {
        List<Artefacto> artefactos = manager.getArtefactos();
        assertNotNull(artefactos);
        assertTrue(artefactos.isEmpty());

        assertEquals(0, manager.getQtdMinerios());
    }

    @Test
    void testeSaveLoad() {
        String fTeste = "test_deepSea.dat";
        manager.moveNavio(1,0);

        int posXSave = manager.getNavioPosX();
        int posYSave = manager.getNavioPosY();

        boolean guarda = manager.guardarJogo(fTeste);
        assertTrue(guarda);

        DeepSeaManager manager2 = new DeepSeaManager();

        boolean carregou = manager2.carregarJogo(fTeste);
        assertTrue(carregou);

        assertEquals(posXSave, manager2.getNavioPosX());
        assertEquals(posYSave, manager2.getNavioPosY());
    }


    @Test
    void testeLogMensagemSimples() {
        String mensagemTeste = "Ola";

        DeepSeaLog.getInstance().log(mensagemTeste);

        List<String> logs = DeepSeaLog.getInstance().getLog();


        assertEquals(1, logs.size(), "O log deveria ter exatamente 1 mensagem.");

        assertTrue(logs.getFirst().startsWith(mensagemTeste),"A mensagem guardada deveria comecar por: " + mensagemTeste);
    }

    @Test
    void testeResetLog() {
        DeepSeaLog.getInstance().log("Mensagem 1");
        DeepSeaLog.getInstance().reset();

        assertTrue(DeepSeaLog.getInstance().getLog().isEmpty(), "O log deveria estar vazio após o reset.");
    }
}
