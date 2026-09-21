Etapa 2 – Gestão do Jogo e Persistência de Dados
1.	Objetivos
Nesta fase, o foco do desenvolvimento será na implementação de toda a lógica de jogabilidade do jogo "Deep Sea Mining" e registo da evolução do jogo. Pretende-se ainda que sejam implementadas funcionalidades de gravação e recuperação de jogos em ficheiro. Não deve ser implementada a User Interface (UI) gráfica nem mecanismos de notificação entre o modelo e a UI.

2.	Requisitos
A evolução do jogo deverá ser gerida recorrendo a uma máquina de estados (FSM – Finite State Machine) implementada de acordo com o lecionado nas aulas. Outras formas de implementação não serão aceites.
Deverá definir um diagrama de estados para o jogo, identificando os estados e transições de estados que reflitam todas as possibilidades de evolução no jogo. O diagrama de estados em formato PDF deverá ser inserido na pasta reports do projeto. Deverá depois definir todos os estados e transições através de uma implementação polimórfica do padrão FSM, incluindo:
•	DeepSeaContext – classe com a implementação do contexto da FSM, agindo como Facade da FSM, controlando o estado atual da mesma, redirecionando os pedidos para o estado atual e disponibilizando métodos get adequados para acesso aos dados;
•	DeepSeaState – enumeração de todos os estados e fábrica de estados (Factory method);
•	IDeepSeaState – interface Java com a declaração de todas as transições de estado;
•	DeepSeaStateAdapter – classe abstrata para implementação por omissão dos métodos das transições e implementação de mecanismos auxiliares para transição de estados, recorrendo à classe contexto da FSM. Deve possuir uma referência para o modelo de dados e outra para a classe de contexto;
•	Classes com a implementação de todos os estados concretos.
Ter em atenção que todas as ações que alteram o modelo de dados devem ser executadas a partir de uma transição de estado.
A complexidade de todo o modelo de dados (classes desenvolvidas no contexto da Etapa 1 e as da presente etapa) deverá ser escondida recorrendo ao padrão Facade, disponibilizando uma classe DeepSeaManager. Esta classe representará todo o modelo de dados, passando por ela todos os pedidos ao modelo e obtenção de respostas do mesmo. Os métodos desta classe não deverão retornar para fora do modelo referências para objetos internos. O retorno/envio de informação para fora do
modelo deverá ser limitado a dados de tipos primitivos, enumerações, objetos imutáveis ou coleções imutáveis dos tipos anteriores.
No âmbito desta etapa pretende-se desde já garantir a persistência de todas as informações essenciais do jogo, garantindo que um jogo pode ser salvaguardado e, mais tarde, possa ser restaurado para ser continuado. Estas funcionalidades devem ser disponibilizadas recorrendo a processos serialização/desserialização para ficheiros binários.
Implemente uma classe, DeepSeaLog, que permita efetuar o log de todas as operações que são realizadas sobre o modelo de dados. Esta classe deve ser implementada seguindo o padrão Singleton. Embora a linguagem Java forneça um conjunto de classes que permitem criar e gerir diferentes tipos de log, pretende-se que implemente um log simplificado como abordado nas aulas. A cada mensagem recebida deve ser associada a data/hora a que foi reportada (esta marca temporal é introduzida pela classe – não é recebida por parâmetro). Para além de registar todas as mensagens que lhe sejam reportadas, o log deverá permitir operações para: consulta, gravação para ficheiro de texto (gravação de todas as mensagens de log, uma mensagem por linha) e eliminação de mensagens. O nome do ficheiro de log deverá estar definido juntamente com as outras constantes da aplicação.
O projeto deverá possuir testes às classes principais, que permitam verificar o bom funcionamento do jogo. No contexto desta etapa poderá criar uma UI em texto para testar a jogabilidade, mas este desenvolvimento é opcional e não será avaliado.
As classes principais deverão estar documentadas com Javadoc (não devem gerar o Javadoc para o diretório do projeto, para evitar sobrecarga desnecessária no repositório remoto).

3.	Notas sobre a realização da etapa
Data-limite de submissão: 2026.04.26 23:59
Gestão e submissão: GitHub+Classroom, usando o repositório criado para cada grupo

Nome do branch: Stage2
Na data-limite o código deverá estar integrado no branch principal (main) através de Squash and Merge. Cada grupo deve ter apenas um Pull Request para o branch da etapa e o Squash&Merge deve ser feito obrigatoriamente no website do GitHub.
Não devem apagar qualquer branch que seja criado, sem ser na presença de um docente da unidade curricular.
Os alunos devem definir em cada grupo regras para realização de commits que permitam refletir uma distribuição equilibrada das tarefas pelos elementos dos grupos (por outras palavras, não queremos ver um elemento do grupo a fazer 20 commits de alteração de “prints” ou ficheiros “readme” e o outro a fazer apenas um commit correspondente à criação de 20 classes). Os alunos que se encontram a realizar o trabalho individualmente, também deverão fazer commits regulares e não apenas um por etapa.
