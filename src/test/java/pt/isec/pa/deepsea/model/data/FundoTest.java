package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import pt.isec.pa.deepsea.model.data.Settings.Settings;

class FundoTest {

    private Fundo fundo;

    @BeforeEach
    void preparaTeste(){
        fundo = new Fundo();
    }

    @Test
    void testGeraElementosFundo(){

    }

    @Test
    void testGeraMinerios(){
        int contadorMinerios = 0;

        for (int x = 0; x < Settings.FUNDO ; x++) {
            for (int y = 0; y < Settings.FUNDO; y++) {
                ElementoFundo elemento = fundo.getElementoFundo(x,y);
                if (fundo.getElementoFundo(x, y) instanceof Minerio) {
                    contadorMinerios++;

                    Minerio min = (Minerio) elemento;
                    assertTrue(min.getQnt() >= 1,
                            "Erro: O valor gerado na pedra foi menor que 1!");
                    assertTrue(min.getQnt() <= Settings.QNT_MAX_MINERIO,
                            "Erro: O valor na pedra (" + min.getQnt() + ") ultrapassou o máximo permitido.");
                }
            }
        }

        assertTrue(contadorMinerios >= 1,
                "Erro: Gerou menos minérios do que o mínimo permitido. Gerou " + contadorMinerios);

        assertTrue(contadorMinerios <= Settings.NMAX_MINERIO,
                "Erro: Ultrapassou o limite máximo de minérios. Gerou " + contadorMinerios);
    }

    @Test
    void testGeraMonstros() {
    }

    @Test
    void testColocarArtefacto(){

    }
}