package pt.isec.pa.deepsea.model.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Classe responsável pela centralização e gestão de registos (logs) de eventos do jogo.
 * <p>
 * Implementa o padrão <b>Singleton</b> para garantir que existe apenas
 * uma instância de registo partilhada por todo o sistema. Permite armazenar mensagens
 * de estado, avisos e eventos críticos, facilitando a monitorização das ações do
 * jogador e do comportamento da Máquina de Estados.
 * </p>
 *
 * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class DeepSeaLog {
        private static DeepSeaLog _instance=null;

        public static DeepSeaLog getInstance() {
            if (_instance == null)
                _instance = new DeepSeaLog();
            return _instance;
        }
        protected ArrayList<String> log;

        private DeepSeaLog() {
            log = new ArrayList<>();
        }

        public void reset() {
            log.clear();
        }

        public void log(String msg) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm:ss");
            String dateTimeString = now.format(formatter);

            log.add(msg + "<=> " + dateTimeString);
        }

        public List<String> getLog() {
            return List.copyOf(log);
        }


        /**
         * Grava o conteúdo do log num ficheiro de texto (.txt).
         * * @param nomeFicheiro O nome ou caminho do ficheiro (ex: "consola_log.txt")
         * @return true se a gravação foi bem sucedida, false caso contrário.
         */
        public boolean guardarLog(String nomeFicheiro){
            if(log == null || log.isEmpty()) return false;

            try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(nomeFicheiro,false)))) {
                for (String linha : log) {
                    writer.println(linha);
                }
                return true;
            }catch (IOException e){
                System.err.println("Erro ao gravar log em texto: " + e.getMessage());
                return false;
            }
        }
}
