package pt.isec.pa.deepsea.model.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static pt.isec.pa.deepsea.model.data.Settings.Settings.*;

/**
 * Classe responsável pela gestão e geração do Fundo.
 * <p>
 * Gere uma grelha bidimensional de {@link ElementoFundo}, controlando a distribuição
 * de monstros, jazigos de minério e artefactos. Implementa a lógica de "Fundo Rico"
 * para variação de recursos e gere o estado de exploração (visibilidade) das células.
 * </p>
 * * @author João Barão, Ivo Gomes, Inês Moreira
 * @version 1.0
 */
public class Fundo implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Matriz bidimensional que armazena os elementos físicos do fundo do mar. */
    private ElementoFundo[][] fundo;

    /** Contador de artefactos presentes nesta instância do fundo. */
    private int qntArtefactos;

    private int maxId;

    /**
     * Construtor da classe Fundo.
     * Inicializa a grelha com as dimensões definidas nas Settings
     * e preenche-a inicialmente com água.
     */
    public Fundo(){
        fundo = new ElementoFundo[FUNDO][FUNDO];
        qntArtefactos = 0;
        geraAgua();
    }

    /**
     * Devolve a matriz completa de elementos do fundo.
     * @return Matriz bidimensional de {@link ElementoFundo}.
     */
    public ElementoFundo[][] getFundo(){ return fundo; }

    /**
     * Obtém o elemento presente numa coordenada específica.
     * @param x Coordenada da linha.
     * @param y Coordenada da coluna.
     * @return O {@link ElementoFundo} na posição indicada.
     */
    public ElementoFundo getElementoFundo(int x, int y){ return fundo[x][y]; }

    /**
     * Gera procedimentalmente os elementos interativos do fundo (Monstros e Minérios).
     * <p>
     * No <b>MODO_DEFESA</b>, gera uma configuração fixa de perigos e revela todo o mapa.
     * No modo normal, utiliza probabilidades para determinar a densidade de monstros
     * e verifica o coeficiente do Fundo Rico nas Settings para decidir se o setor
     * terá abundância de recursos.
     * </p>
     */
    public void geraElementosFundo(){
        if(MODO_DEFESA){
            geraMonstros();
            geraMinerios(QNT_MAX_MINERIO);
            geraMinerios(1);
            FundoVisivel();
        }
        else {
            int qntMinerios = 0;
            int nMonstros = (int) (Math.random() * (MAXNUM_MONSTROS)) + 1;
            for (int i = 0; i < nMonstros; i++) {
                geraMonstros();
            }

            double sorteioFundoRico = Math.random();
            if (sorteioFundoRico > FUNDORICO) {
                qntMinerios = NMAX_MINERIO;
            } else {
                qntMinerios = (int) (Math.random() * NMAX_MINERIO) + 1;
            }

            for (int i = 0; i < qntMinerios; i++) {
                int randQuant = (int) (Math.random() * QNT_MAX_MINERIO) + 1;
                geraMinerios(randQuant);
            }
        }
    }

    public List<Obstaculo> getObstaculos(){
        List<Obstaculo> obstaculos=new ArrayList<>();
        for(int i=0;i<FUNDO;i++){
            for(int j=0;j<FUNDO;j++){
                ElementoFundo elemento=fundo[i][j];
                if(elemento instanceof Obstaculo){
                    obstaculos.add((Obstaculo) elemento);
                }
            }
        }
        return obstaculos;
    }

    /**
     * Limpa o fundo para uma nova exploração, mantendo apenas os artefactos.
     * Substitui todos os outros elementos por água e redefine a visibilidade
     * dos artefactos para oculto.
     */
    public void limpaFundo() {
        for (int i = 0; i < FUNDO; i++) {
            for (int j = 0; j < FUNDO; j++) {
                if(!fundo[i][j].isVisivel()) {
                    if (!(fundo[i][j] instanceof Artefacto)) {
                        fundo[i][j] = new Agua();
                    }
                }
            }
        }
    }

    /**
     * Verifica se este setor do fundo contém algum artefacto.
     * @return {@code true} se houver pelo menos um artefacto; {@code false} caso contrário.
     */
    public boolean eArtefacto(){
        return qntArtefactos > 0;
    }

    /**
     * Torna todos os elementos da grelha visíveis simultaneamente.
     * Utilizado principalmente para depuração ou modos de jogo específicos.
     */
    private void FundoVisivel(){
        for (int i = 0; i < FUNDO; i++) {
            for (int j = 0; j < FUNDO; j++) {
                fundo[i][j].setVisivel(true);
            }
        }
    }

    /**
     * Preenche as células vazias da grelha com instâncias de {@link Agua}.
     * Garante que não existem posições nulas na matriz.
     */
    private void geraAgua(){
        for (int i = 0; i < FUNDO; i++) {
            for (int j = 0; j < FUNDO; j++) {
                if(!(fundo[i][j] instanceof Artefacto || fundo[i][j] instanceof Minerio || fundo[i][j] instanceof Monstro)){
                    fundo[i][j] = new Agua();
                }
            }
        }
    }

    /**
     * Tenta colocar um jazigo de minério numa posição aleatória válida (água não visível).
     * @param qnt Quantidade de minério contida no jazigo.
     */
    public void geraMinerios(int qnt){
        int posXMin;
        int posYMin;
        do {
            posXMin = (int) (Math.random() * FUNDO);
            posYMin = (int) (Math.random() * FUNDO);
        } while(!(fundo[posXMin][posYMin] instanceof Agua) && fundo[posXMin][posYMin].isVisivel());
        fundo[posXMin][posYMin] = new Minerio(qnt);
    }

    /**
     * Tenta colocar um monstro numa posição aleatória válida (água não visível).
     */
    public void geraMonstros(){
        int posXMon;
        int posYMon;
        do {
            posXMon = (int) (Math.random() * FUNDO);
            posYMon = (int) (Math.random() * FUNDO);
        } while (!(fundo[posXMon][posYMon] instanceof Agua) && fundo[posXMon][posYMon].isVisivel());
        fundo[posXMon][posYMon] = new Monstro(posXMon, posYMon);
    }

    /**
     * Coloca um artefacto (objetivo principal) numa posição aleatória de água.
     * Incrementa o contador global de artefactos deste setor.
     */
    public void colocarArtefacto(){
        int posXArt;
        int posYArt;
        do {
            posXArt = (int) (Math.random() * FUNDO);
            posYArt = (int) (Math.random() * FUNDO);
        } while (!(fundo[posXArt][posYArt] instanceof Agua));

        fundo[posXArt][posYArt] = new Artefacto();
        qntArtefactos++;
    }

    /** @return O número total de artefactos depositados neste fundo. */
    public int getQntArtefactos(){
        return qntArtefactos;
    }

    /**
     * Adiciona um elemento específico numa posição aleatória de água.
     * Define a visibilidade inicial do elemento como falsa.
     * @param elemento O {@link ElementoFundo} a ser adicionado.
     */
    public void adicionarElementoAleatorio(ElementoFundo elemento) {
        int posX;
        int posY;
        do {
            posX = (int) (Math.random() * FUNDO);
            posY = (int) (Math.random() * FUNDO);
        } while (!(fundo[posX][posY] instanceof Agua));
        fundo[posX][posY] = elemento;
        fundo[posX][posY].setVisivel(false);
    }

    /**
     * Remove um elemento da grelha, substituindo-o por {@link Agua}.
     * @param posX Coordenada X.
     * @param posY Coordenada Y.
     */
    public void removeElemento(int posX, int posY){
        ElementoFundo elemento=fundo[posX][posY];
        if(elemento instanceof Artefacto){
            qntArtefactos--;
        }
        fundo[posX][posY] = new Agua();
    }

    /**
     * Gera uma representação textual da grelha do fundo.
     * <p>
     * Se a célula não tiver sido explorada (não visível), exibe [?].
     * Caso contrário, utiliza símbolos para Água [ ], Monstros [M], Minérios [m] e Artefactos [*].
     * </p>
     * @return String formatada com o mapa do fundo.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < FUNDO; i++) {
            for (int j = 0; j < FUNDO; j++) {
                ElementoFundo elemento = fundo[i][j];
                if (!elemento.isVisivel() && !MODO_DEFESA) {
                    sb.append("[?] ");
                    continue;
                }
                switch (elemento) {
                    case Agua agua -> sb.append("[ ]");
                    case Monstro monstro -> sb.append("[M]");
                    case Minerio minerio -> sb.append("[m]");
                    case Artefacto artefacto -> sb.append("[*]");
                    default -> { }
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
