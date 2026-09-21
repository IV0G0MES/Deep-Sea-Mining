Etapa 1 – Modelo de dados

1.	Objetivos
Nesta fase, o foco do desenvolvimento será na criação da estrutura de classes que suportará o jogo "Deep Sea Mining". Não deve ser implementada lógica relacionada com a jogabilidade, focando-se apenas nas formas de armazenamento dos dados e iniciação/geração da informação de jogo, conforme descrito no enunciado.

2.	Requisitos
Pretende-se que todos os elementos necessários à modelação do jogo sejam implementados através de classes adequadas, recorrendo, sempre que possível e justificado, à herança e polimorfismo.
Entidades a modelar (na implementação, poderá definir os nomes que achar mais adequados):
•	Jogo – Classe principal do modelo de dados que agrega todas as suas componentes;
•	Grelha da superfície – array bidimensional único (não poderão existir vários arrays para representar a sua informação). A cada elemento da grelha deve corresponder:
o	Grelha que represente um fosso marinho – array bidimensional (só existe um por fosso) que permita representar os elementos existentes nas fases de subida e descida, nomeadamente: rochas, animais marinhos e correntes marítimas;
o	Grelha que represente o fundo do mar – array bidimensional (só existe um por zona) que permita representar o fundo do mar, incluindo informação sobre se a célula foi revelada e os elementos existentes no fundo do mar: minérios, monstros e artefactos;
•	Navio – Classe representativa do navio com todas as suas características:
o	Quantidade de combustível – inicialmente tem o depósito cheio, de acordo com a quantidade de combustível que for definida pelo grupo;
o	Quantidade de minérios – inicialmente 0 (zero);
o	Artefactos recolhidos – coleção do tipo List com os artefactos encontrados;
o	Drones – coleção do tipo Set com os drones funcionais (inicialmente 3);
o	Localização – inicialmente a posição do navio é aproximadamente no centro da grelha de superfície.
•	Drones – Classe representativa do drone que permita gerir o combustível (e respetiva quantidade máxima), integridade do casco, localização (notar que quando está na superfície é assumido que se encontra no navio, não possuindo uma localização própria) e recursos transportados. A localização inicial do drone na descida é o centro do fosso em cima, na subida é o centro do fosso em baixo e no fundo é aproximadamente o centro da grelha do fundo do mar.

Nesta primeira etapa já devem estar desenvolvidos os mecanismos de geração de toda a informação, nomeadamente a geração da grelha de superfície, que inclui, para cada uma das suas células:
•	A geração de cada uma das grelhas correspondentes aos fossos marinhos, incluindo as rochas laterais. Para assegurar a navegabilidade, a geração das rochas deve ser feita de cima para baixo, garantindo que a posição da rocha mais interior de cada margem não pode oscilar mais de uma coluna face à linha anterior. A soma das rochas de ambos os lados em cada linha não pode ultrapassar 50% da largura da grelha;
•	A geração de cada uma das grelhas correspondentes ao fundo do mar, incluindo a localização dos minérios e artefactos. A quantidade de minérios em cada localização poderá ter valores diferentes.
Devem também ser desenvolvidos todos os métodos que permitam dispor os elementos que devem ser gerados (sorteados) apenas quando uma determinada grelha fica ativa, incluindo: animais, monstros e correntes.
Devem existir também os métodos de consulta que permitam mais tarde tomar decisões acerca do evoluir do jogo. Para além de outros métodos de consulta, deverão existir métodos que permitam obter listas ordenadas de drones, por quantidade de combustível ou por integridade do casco.
A forma de definição do minijogo é da total liberdade do grupo, podendo optar pela solução que considere mais adequada, desde que permita gerir o estado das 15 peças na grelha 4 x 4 e o respetivo contador de movimentos. A opção tomada deve ser devidamente justificada no relatório final.
A classe ou interface referida no enunciado para especificação das diversas contantes que definem as diversas vertentes do jogo, deve também estar devidamente definida e ser utilizada no código desenvolvido.
Para facilitar o teste das funcionalidades durante a defesa dos trabalhos, deverá existir uma constante que indica se a aplicação está ou não a ser gerida em modo “defesa”. Se estiver em modo defesa:
•	os artefactos deverão estar todos concentrados em duas zonas no fundo, relativas a células centrais na superfície;
•	no fosso marinho existe apenas uma rocha de cada lado e existe apenas um animal e uma corrente alinhados verticalmente, aproximadamente no centro da respetiva grelha;
•	no fundo do mar todas as células estarão visíveis, existirá apenas um monstro e 2 minérios (um com quantidade 1 e outro com a quantidade mais elevada que faça sentido com os valores definidos pelo grupo).
As classes principais do modelo de dados deverão incluir testes unitários (Junit 5). Cada uma dessas classes deverá ter 2 testes diferentes, no mínimo.

3.	Notas sobre a realização da etapa
Data-limite de submissão: 2026.04.06 13:00
Gestão e submissão: GitHub+Classroom, usando o repositório criado para cada grupo Nome do branch: Stage 1
Na data-limite o código deverá estar integrado no branch principal através de Squash and Merge.
