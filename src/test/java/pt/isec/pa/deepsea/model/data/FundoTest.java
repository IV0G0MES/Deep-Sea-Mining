package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;

class FundoTest {

    private Fundo fundo;

    @BeforeEach
    void preparaTeste(){
        fundo = new Fundo();
    }

    @AfterEach
    void mostraFundo(){
        System.out.println(fundo.toString());
    }

    @Test
    void testGeraElementosFundo(){
        for (int x = 0; x < FUNDO; x++) {
            for (int y = 0; y < FUNDO; y++) {
                ElementoFundo elemento = fundo.getElementoFundo(x, y);
                assertNotNull(elemento,
                        "Erro: A posição (" + x + ", " + y + ") ficou a null! O mapa não foi preenchido corretamente.");
            }
        }
    }

    @RepeatedTest(20)
    void testGeraMinerios(){
        fundo.geraElementosFundo();
        int contadorMinerios = 0;

        for (int x = 0; x < FUNDO ; x++) {
            for (int y = 0; y < FUNDO; y++) {
                ElementoFundo elemento = fundo.getElementoFundo(x,y);
                if (fundo.getElementoFundo(x, y) instanceof Minerio) {
                    contadorMinerios++;

                    Minerio min = (Minerio) elemento;
                    assertTrue(min.getQnt() >= 1,
                            "Erro: O valor gerado na pedra foi menor que 1!");
                    assertTrue(min.getQnt() <= QNT_MAX_MINERIO,
                            "Erro: O valor na pedra (" + min.getQnt() + ") ultrapassou o máximo permitido.");
                }
            }
        }

        assertTrue(contadorMinerios >= 1, "Erro: Gerou menos minérios do que o mínimo permitido. Gerou " + contadorMinerios);
        assertTrue(contadorMinerios <= NMAX_MINERIO, "Erro: Ultrapassou o limite máximo de minérios. Gerou " + contadorMinerios);
    }

    @RepeatedTest(20)
    void testGeraMonstros() {
        fundo.geraElementosFundo();
        int contadorMonstros = 0;
        for (int x = 0; x < FUNDO; x++) {
            for (int y = 0; y < FUNDO; y++) {
                if (fundo.getElementoFundo(x, y) instanceof Monstro) {
                    contadorMonstros++;
                }
            }
        }

        assertTrue(contadorMonstros >= 1, "Erro: O jogo gerou 0 monstros! Tinha de gerar pelo menos 1.");
        assertTrue(contadorMonstros <= MAXNUM_MONSTROS, "Erro: Ultrapassou o limite máximo de monstros. Gerou: " + contadorMonstros);
    }

    @Test
    void testColocarArtefacto() {
        fundo.colocarArtefacto();
        int contadorArtefactos = 0;

        for (int x = 0; x < FUNDO; x++) {
            for (int y = 0; y < FUNDO; y++) {
                if (fundo.getElementoFundo(x, y) instanceof Artefacto) {
                    contadorArtefactos++;
                }
            }
        }

        assertEquals(1, contadorArtefactos, "Erro: Depois de chamar colocarArtefacto(), devia existir exatamente 1 no mapa, mas encontrou " + contadorArtefactos);
    }


}