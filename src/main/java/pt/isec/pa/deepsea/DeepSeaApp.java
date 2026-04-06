package pt.isec.pa.deepsea;

import pt.isec.pa.deepsea.model.data.Artefacto;
import pt.isec.pa.deepsea.model.data.Puzzle;

import java.util.Scanner;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.MAX_MOVES_PUZZLE;

public class DeepSeaApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
       Puzzle puzzle = new Puzzle(MAX_MOVES_PUZZLE);

        System.out.println("=== TESTE DO PUZZLE ===");

        while (puzzle.getMovesLeft() > 0 ) {
            System.out.println(puzzle);
            System.out.println("peça a mover: ");

            int pecaChossen =  scanner.nextInt();
            if(pecaChossen == 99){
                break;
            }

            boolean sucesso = puzzle.movePiece(pecaChossen);

            if(sucesso){
                //System.out.println("erroooo");
                if(puzzle.isResolved()){
                    System.out.println("puzzle resolvido");
                    break;
                } else if(puzzle.getMovesLeft() == 0){
                    System.out.println("Nao exitem mais moviemnto ");
                }
            }else {
                System.out.println("eroo");
            }
        }

        System.out.println("Fim do teste.");
        scanner.close();
    }
}
