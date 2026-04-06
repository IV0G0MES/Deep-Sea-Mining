package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static pt.isec.pa.deepsea.model.data.Settings.Settings.FOSSO;

class FossoTest {
    private Fosso fosso;
    @BeforeEach
    void setUp() {
        fosso = new Fosso();
    }


    @Test
    void testGerarRochas() {
        int limMax = FOSSO/2;

        for(int i = 0; i < FOSSO; i++) {
            int rochasEsq = 0;
            int rochasDir = 0;

            for (int j = 0; j < FOSSO; j++) {
                if(!(fosso.getElementoFosso(i, j) instanceof Rocha)) {
                    break;
                }else {
                    rochasEsq++;
                }
            }
            for(int j = FOSSO-1; j >= 0; j--) {
                if(!(fosso.getElementoFosso(i, j) instanceof Rocha)) {
                    break;
                }else{
                      rochasDir++;
                }
            }

            assertTrue(rochasDir + rochasEsq <= limMax);
        }
    }


    @Test
    void testgeraAnimalCorrente() {
        int contavazios = 0;
        for(int i = 0; i < FOSSO; i++) {
            for (int j = 0; j < FOSSO; j++) {
                if(fosso.getElementoFosso(i, j) == null){
                    contavazios++;
                }
            }
        }
        fosso.geraAnimalCorrente();

        int contaobstaculos = 0;
        for(int i = 0; i < FOSSO; i++) {
            for (int j = 0; j < FOSSO; j++) {
                ElementoFosso elementoFosso = fosso.getElementoFosso(i, j);
                if(elementoFosso instanceof AnimalMarinho || elementoFosso instanceof CorrenteMaritima) {
                    contaobstaculos++;
                }
            }
        }
        assertTrue(contaobstaculos <= contavazios);
    }

    @Test
    void testlimpaAnimaisCorrentes() {
        int animalX = -1, animalY = -1;
        int correnteX = -1, correnteY = -1;
        int rocheX = -1, rocheY = -1;

        for(int i = 0; i < FOSSO; i++){
            for(int j = 0; j < FOSSO; j++){
                if(fosso.getElementoFosso(i,j) == null){
                    if(animalX == -1){
                        animalX = i;
                        animalY = j;
                    } else if(correnteX == -1){
                        correnteX = i;
                        correnteY = j;
                    }
                } else if (fosso.getElementoFosso(i, j) instanceof Rocha) {
                    rocheX = i;
                    rocheY = j;
                }
            }
        }

        fosso.getFosso()[animalX][animalY] = new AnimalMarinho(animalX,animalY);
        fosso.getFosso()[correnteX][correnteY] = new CorrenteMaritima(correnteX,correnteY);
        System.out.println(fosso);

        fosso.limpaAnimaisCorrentes();
        assertNull(fosso.getElementoFosso(animalX,animalY), "Nao eliminou o objeto");
        assertNull(fosso.getElementoFosso(correnteX,correnteY), "Nao eliminou o objeto");

        assertNotNull(fosso.getElementoFosso(rocheX,rocheY), "Apagou uma rocha");
        System.out.println(fosso);
    }
}