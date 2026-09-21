Enunciado do Trabalho Prático

1.	Introdução
O objetivo deste trabalho é o desenvolvimento do jogo de estratégia e exploração "Deep Sea Mining".
O jogador assume o comando de uma operação de resgate em águas profundas, com o objetivo de recuperar artefactos perdidos de uma civilização antiga. A missão exige a gestão cuidada de recursos (combustível e integridade do casco) e a superação de perigos nas profundezas do oceano.
O desenvolvimento deve ser realizado em linguagem Java, aplicando o paradigma da Programação Orientada aos Objetos e recorrendo à tecnologia JavaFX e a padrões arquiteturais e funcionais aplicáveis ao desenvolvimento de software.
Este enunciado é propositadamente omisso em algumas partes. Ao longo do período de desenvolvimento do trabalho serão divulgadas mais informações sobre algumas regras que deverão ser respeitadas.

2.	Mecânica do jogo
O jogo desenrola-se em alto-mar, onde o jogador alterna entre a gestão estratégica do navio à superfície e a exploração nas profundezas, usando drones de exploração subaquática. A missão global consiste em completar uma coleção de n1 artefactos antigos, exigindo para tal a realização de múltiplas expedições de mergulho. Cada artefacto possui um identificador único, variando entre 1 e n1.
Existem 3 drones disponíveis no navio, mas apenas um será usado em cada momento. Para ter sucesso, o jogador deve monitorizar e gerir dois recursos fundamentais:
•	Combustível – Essencial para a locomoção do navio na superfície e para os propulsores do drone. O tipo de combustível do drone é igual ao do navio, abastecendo-se a partir deste. Se o combustível acabar durante uma missão, o drone fica à deriva e é dado como perdido;
•	Integridade do Casco do Drone – Representa a blindagem do drone, com valores entre 0 e 100. Colisões com rochas ou ataques de fauna hostil reduzem este valor. O valor é reduzido seguindo uma sequência de números primos (1, 2, 3, 5, 7, ...), reiniciada a cada expedição do drone ao fundo do mar. Se atingir zero, o drone é destruído e perdido definitivamente. Quando no navio, os cascos dos drones não totalmente destruídos podem ser reparados, mas essa operação consome combustível extra do navio.
O jogo termina se os artefactos forem todos recolhidos, se os 3 drones forem perdidos ou se o combustível do navio acabar.
Cada expedição divide-se nas quatro fases que se passam a descrever.

A.	Operações à Superfície
O jogo inicia-se à superfície. O jogador visualiza um mapa em grelha representando a superfície do oceano (n2 x m2; ex.: 30 x 15) e possui as seguintes opções:
•	Navegar – Mover o navio para coordenadas adjacentes para escolher o ponto de início do próximo mergulho. Esta ação consome Combustível. A apresentação gráfica da grelha deve permitir identificar as células da grelha da superfície onde estão os artefactos no fundo do mar (usar cores e outras formas de representação alternativas).
•	Oficina – O jogador pode gerir os seus recursos:
o	Selecionar drone – Selecionar o drone a ser alvo de operações;
o	Abastecer drone – Transferir combustível do navio para o drone;
o	Reparar drone – Restaurar a integridade do casco do drone, consumindo uma quantidade de Combustível;
o	Melhorar drone – Troca minérios recolhidos por melhorias (aumentar capacidade do tanque de combustível ou reforçar a blindagem máxima);
•	Rampa de lançamento – O jogador pode selecionar o drone a usar e iniciar a fase de descida.

B.	Descida/Subida
Nesta fase, o drone viaja da superfície até ao fundo ou vice-versa, através de um fosso marinho.
O jogador controla os movimentos de subida e descida, bem como os deslocamentos laterais para evitar obstáculos. O objetivo é atingir o fundo (na descida) ou a superfície (na subida) evitando o contacto com as rochas laterais e perigos que aí se encontram (fauna hostil). Qualquer colisão reduz a integridade do casco; o movimento dos propulsores consome pequenas quantidades de combustível, que poderão ser superiores na presença de correntes mais fortes.
Esta fase é também representada por uma grelha n3 x m3. A quantidade de rochas laterais do fosso (uma rocha corresponderá a uma célula) deverá ser gerada aleatoriamente, sabendo que a soma das rochas em ambos os lados não podem ultrapassar 50% do número de células da grelha. Para além das rochas laterais, poderão existir n4 obstáculos no caminho. Os obstáculos poderão ser animais marinhos ou correntes subaquáticas, não sendo impeditivos da passagem do drone. No entanto, os animais marinhos provocam danos no casco e as correntes obrigam ao consumo do dobro do combustível de um deslocamento normal. Cada um destes obstáculos deverá possuir um identificador inteiro positivo sequencial iniciado em 1001 (quando é realizada uma subida ou descida num mesmo local o contador não é reiniciado, sendo partilhado por todos os locais de expedição).

Se, na subida, o drone for totalmente destruído (Integridade do Casco atingiu o valor 0) ou ficar perdido por falta de combustível, os minérios e artefactos recolhidos na expedição em curso serão depositados no fundo do mar. Nesta situação o drone deixa de constar da lista de drones disponíveis.
Quando chega à superfície com sucesso, o drone é recolhido automaticamente. Os artefactos e minérios são recolhidos e incluídos no portfólio do jogador.

(imagem apenas ilustrativa, gerada por AI)

C.	Exploração do Fundo
Ao atingir o fundo do mar, a visão altera-se para uma grelha n5 x m5 (ex. 10 x 10), em que as células ainda não visitadas são representadas por escuridão. O jogador é inicialmente posicionado numa célula central da grelha e poderá mover o drone célula a célula para revelar o mapa (uma célula apenas é revelada quando é visitada). Cada movimento consome Combustível. O jogador deve estar atento para garantir que tem combustível suficiente para explorar e regressar, podendo optar por iniciar a subida a qualquer momento. Se voltar a descer à mesma zona no fundo do mar, as células anteriormente reveladas já deverão aparecer descobertas.
Uma célula pode estar vazia ou incluir um dos seguintes elementos:
•	Minérios – Pode optar pela sua recolha para depois trocar por capacidades adicionais na oficina, mas a sua recolha implica um consumo extra de combustível (ex.: 1%);
•	Monstros – Danificam a Integridade do Casco (ex.: 5%), tal como os animais encontrados na descida. Os monstros possuem um identificador único, originado da mesma forma do que a referida para os obstáculos na fase de descida/subida;
•	Artefactos – A sua recolha assume-se como o objetivo principal. No entanto, para serem recolhidos, será necessário ultrapassar o desafio descrito na secção seguinte.

D.	Recuperação (Minijogo)
Ao tentar recolher um artefacto, o jogador enfrenta um sistema de bloqueio antigo: um puzzle 4 x 4 com 15 peças. O desafio é o de ordenar as peças numeradas de 1 a 15 de forma crescente (ou em alternativa uma imagem dividida em 4 x 4 quadrados), deslizando-as para o espaço vazio. O jogador deverá finalizar o puzzle em n6 movimentos.
Se o jogador conseguir resolver o puzzle, o artefacto é recolhido e o drone volta ao modo de exploração. Se o número de movimentos se esgotar, o drone inicia imediatamente a subida, regressando à superfície sem o artefacto falhado (poderá manter os minérios e outros artefactos recolhidos na expedição em curso, caso a subida tenha sucesso).

Notas adicionais sobre a preparação inicial do jogo e localização dos elementos:
•	A cada posição da grelha da superfície corresponde uma grelha de descida e uma grelha do fundo do mar;
•	As grelhas das rochas laterais de cada fosso, são todas sorteadas no início do jogo;
•	As posições dos n1 artefactos são sorteadas no início do jogo (essencial para que a grelha à superfície permita orientar o jogador na procura dos artefactos);
•	As localizações dos minérios no fundo do mar são sorteadas no início do jogo, não se sobrepondo aos locais onde se encontram os artefactos;
•	Em caso de destruição do drone ou perda desta por falta de combustível na subida, caso ele possua minérios ou artefactos, estes serão dispostos novamente no fundo do mar, em locais novamente sorteados, passando a grelha respetiva a estar em escuridão (não explorada);
•	Os obstáculos na descida e subida (animais marinhos e correntes) são sorteados no início desse percurso, podendo existir no máximo n4 obstáculos;
•	A quantidade e localização dos monstros no fundo do mar são sorteadas de cada vez que o drone atingir o fundo do mar, entre as células em escuridão e não ocupadas por minérios ou artefactos. Não deverão existir mais de n7 monstros por cada grelha do fundo do mar.
 

3.	Desenvolvimento
O trabalho será desenvolvido em grupos, com no máximo 3 elementos (de acordo com a constituição de grupo comunicada até ao final da primeira semana de março). As diversas etapas de realização do trabalho serão comunicadas através de mensagem no InforEstudante, com um resumo das funcionalidades a serem implementadas, regras que devem ser respeitadas, data-limite de apresentação e outras informações essenciais para a realização das tarefas previstas.
A verificação e avaliação das etapas será efetuada em sala de aula. Caso o grupo não esteja presente não obterá classificação na etapa, mas não fica impedido de continuar o desenvolvimento do projeto. Os grupos constituídos exclusivamente por trabalhadores-estudantes, que optem por um grupo com este estatuto (não podendo variar de etapa para etapa), deverão entregar até à data-limite da etapa um relatório sobre o desenvolvimento realizado, alterações/correções de desenvolvimentos anteriores e o contributo de cada elemento. Os alunos trabalhadores-estudantes poderão ser convocados para esclarecimento de dúvidas sobre a realização da tarefa, sendo para isso marcada uma reunião (presencial ou remota) em data a combinar com o grupo.
Para auxiliar as tarefas de gestão do desenvolvimento em grupo, registo da evolução e acompanhamento pelos docentes, será usado o GitHub+GitHub Classroom. Na primeira aula prática após a divulgação deste enunciado, será fornecido um link que permitirá aos elementos dos grupos registarem-se e serem associados a um grupo. Os alunos deverão registar contas no GitHub que permitam a sua fácil identificação: <primeiro_nome><número_de_aluno> (ex.: “Zeferino2020202020”).
A cada etapa de evolução do trabalho, o grupo deverá criar um branch para realização das tarefas. O branch deverá ter a designação que será comunicada no início da etapa. Até à data de entrega da tarefa deverá ser feito um Pull Request e um Merge (preferencialmente Squash and Merge) para o branch main. Não deve ser incluído na submissão qualquer desenvolvimento que não seja requerido na etapa em realização ou nas suas anteriores.
Até ao final do dia 1 de Junho todos os grupos (independentemente do tipo de grupo) deverão fazer a submissão final do trabalho e entregar um relatório final. Os grupos com avaliação do tipo “Normal”, ou seja, com avaliação em aula, deverão preparar uma apresentação da versão final do trabalho e respetivo relatório. A apresentação não poderá exceder 10 minutos, seguindo-se a defesa das opções tomadas. Estas defesas serão realizadas maioritariamente nos horários normais das aulas práticas e é obrigatória a presença de todos os elementos do grupo.
Para realização das defesas dos trabalhos desenvolvidos sem avaliação em aula (trabalhadores- estudantes) serão marcadas sessões presenciais a realizar durante a última semana de aulas. Estas sessões incluirão a apresentação, demonstração das funcionalidades e defesa, tendo uma duração de
+/- 30 minutos e sendo obrigatório que todos os elementos do grupo estejam presentes.
As notas atribuídas aos alunos de um mesmo grupo poderão variar de acordo com o conhecimento demonstrado nas defesas (etapas e final), bem como com o número de presenças nas aulas práticas (as faltas não devidamente justificadas serão tidas consideração na atribuição da nota de cada etapa). Caso um aluno não participe na defesa final, ficará com nota 0 (zero) na componente de trabalho prático, independentemente da sua participação ao longo do semestre.
 
Como já referido, o desenvolvimento do trabalho prático será realizado nas seguintes etapas, as quais serão anunciadas através de mensagens no InforEstudante com os pormenores de implementação a respeitar em cada uma:
•	Definição do modelo de dados – Definição das classes que permitam modelar todos os elementos do jogo;
•	Gestão de jogo e persistência dos dados – Classes para representar um jogo e lógica de jogo, gestão da persistência dos dados através de serialização, geração de relatório de texto com o log completo de evolução de jogo;
•	Interface gráfica Java FX – Criação da interface gráfica, opções da aplicação, desenho dos cenários de jogo, jogabilidade, sons, etc.

No desenvolvimento da aplicação serão aplicados diversos padrões de programação. No entanto, a forma de implementação dos padrões é muito diversa, pelo que se deve restringir às formas de implementação usadas no contexto das aulas e às indicações divulgadas na descrição de cada etapa. Outras formas de implementação não serão consideradas nem avaliadas.
O código fonte da aplicação deve apresentar uma divisão clara entre o modelo e a interface com o utilizador (UI). O código do modelo não deve conter qualquer tipo de interação com o utilizador e a interface com o utilizador não deve incluir qualquer tipo de lógica do jogo. O código da interface com o utilizador poderá incluir lógica de pré-tratamento das ações do utilizador (por exemplo, filtrar ou pré- processar os inputs do utilizador), sem incluir validações que dependam do estado do jogo, e o modelo deve ser robusto de modo a atuar adequadamente nas ordens e receção de dados incorretos ou fora de contexto. A estrutura do projeto, em termos de packages, deve refletir esta separação, conforme explicado mais à frente.
Todas as constantes (por exemplo, combustível inicial, consumos por movimentos do navio e do drone, quantidade de artefactos e minérios, penalizações associadas a colisões, número máximo de monstros em cada grelha do fundo do mar, número máximo de movimentos para finalizar um puzzle, quantidade de combustível consumida por cada reparação do drone, etc.) deverão ser definidas através de uma única classe ou interface, para que a aplicação possa ser recompilada e executada com diferentes valores padrão. Durante a fase de desenvolvimento, a escolha destes valores ficará ao critério de cada grupo.
A atualização da UI deverá estar de acordo com o padrão de notificações assíncronas estudado nas aulas. Espera-se que as interfaces com o utilizador sejam apelativas, intuitivas e funcionais.
As classes de todo o modelo de dados deverão possuir comentários compatíveis com a geração de documentação usando JavaDoc. A documentação gerada não necessita de ser entregue com o trabalho, mas deverão estar aptos a gerá-la durante a defesa final.
As classes principais do modelo de dados deverão incluir testes unitários (JUnit). Cada uma dessas classes deverá ter 2 testes diferentes, no mínimo.
No desenvolvimento desta aplicação não poderá recorrer a bibliotecas externas não referidas explicitamente no contexto deste enunciado ou das aulas.
 
Estrutura de projeto
A aplicação deve estar organizada em packages, incluindo pelo menos os seguintes (podem existir outros):
●	pt.isec.pa.deepsea – package que abrange toda a aplicação;
●	pt.isec.pa.deepsea.model – inclui a classe que constitui a Facade de acesso à lógica, que internamente distribui as responsabilidades que lhe são pedidas;
●	pt.isec.pa.deepsea.model.data – contém as classes que representam as estruturas de dados e respetiva gestão;
●	pt.isec.pa.deepsea.model.state – contém as classes que representam os estados da aplicação;
●	pt.isec.pa.deepsea.ui – classes que implementam a interface em modo gráfico em Java Fx;
●	pt.isec.pa.deepsea.ui.res – package base para gestão dos recursos multimédia da aplicação, recorrendo, por exemplo, ao padrão multiton.


Na raiz do diretório de projeto, para além da pasta src com os packages referidos, deverão existir os seguintes diretórios:
●	reports – relatórios produzidos: relatórios por tarefa (trabalhadores-estudantes ou similares) e relatório final (todos os alunos);
●	test – todas as classes para realização dos testes unitários, respeitando a hierarquia de packages referida anteriormente (no contexto de projetos maven, esta pasta estará dentro da pasta src).

O relatório final deve incluir:
●	Uma descrição sintética acerca das opções e decisões tomadas na implementação;
●	Diagramas de padrões de programação aplicados no trabalho;
●	Tabela com descrição sucinta das classes utilizadas no programa (o que representam e os objetivos);
●	Descrição sucinta do relacionamento entre as classes (podem ser usados diagramas UML);
●	Para cada funcionalidade esperada da aplicação, a indicação de cumprido/implementado totalmente ou parcialmente (especificar o que foi efetivamente cumprido neste caso) ou não cumprido/implementado (especificar a razão). O uso de uma tabela pode simplificar a elaboração desta parte.

