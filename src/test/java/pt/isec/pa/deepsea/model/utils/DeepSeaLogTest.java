package pt.isec.pa.deepsea.model.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeepSeaLogTest {

    @BeforeEach
    void setUp() {
        DeepSeaLog.getInstance().reset();
    }

    @Test
    void testeReset() {
        DeepSeaLog.getInstance().log("Log1: A descer");
        DeepSeaLog.getInstance().log("Log2: Chegou ao fundo");

        DeepSeaLog.getInstance().reset();
        assertTrue(DeepSeaLog.getInstance().getLog().isEmpty());
    }
    @Test
    void testeMesmaInstacia() {
        DeepSeaLog instancia1 = DeepSeaLog.getInstance();
        DeepSeaLog instancia2 = DeepSeaLog.getInstance();

        assertSame(instancia1, instancia2);
    }
}