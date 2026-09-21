package pt.isec.pa.deepsea.model.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;

import static org.junit.jupiter.api.Assertions.*;

class PuzzleTest {
    private Puzzle puzzle;

    @BeforeEach
    void setUp() {
        puzzle = new Puzzle(MAX_MOVES_PUZZLE);
    }

    @Test
    void testSetPuzzle(){
        assertEquals(MAX_MOVES_PUZZLE, puzzle.getMovesLeft(),
                "Os movimentos inicias tem q ser iguais ao das Settings.MAX_MOVES_PUZZLE");
        assertFalse(puzzle.isResolved());
    }
    @Test
    void testPiecesIntegrety(){

        int tab[][] = puzzle.getTabuleiro();
        boolean[] found = new boolean[16];

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                int num = tab[i][j];

                assertTrue(num >= 0 && num <= 15, "peça invalida: " + num);

                found[num] = true;
            }
        }
        for(int i =  0; i < 16; i++){
            assertTrue(found[i], "peça:" + i + " desapareceu");
        }
    }

}