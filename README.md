# Sincronizacao Receita (JAVA)
Aplicativo em Java para sincronização de um serviço com informações passadas por um arquivo .csv

Cenário de Negócio:
Todo dia útil por volta das 6 horas da manhã um colaborador da retaguarda do Sicredi recebe e organiza as informações de contas para enviar ao Banco Central. Todas agencias e cooperativas enviam arquivos Excel à Retaguarda. Hoje o Sicredi já possiu mais de 4 milhões de contas ativas.
Esse usuário da retaguarda exporta manualmente os dados em um arquivo CSV para ser enviada para a Receita Federal, antes as 10:00 da manhã na abertura das agências.

Requisito:
Usar o "serviço da receita" (fake) para processamento automático do arquivo.

Funcionalidade:
1. Criar uma aplicação SprintBoot standalone. Exemplo: java -jar SincronizacaoReceita <input-file>
2. Processa um arquivo CSV de entrada com o formato abaixo.
3. Envia a atualização para a Receita através do serviço (SIMULADO pela classe ReceitaService).
4. Retorna um arquivo com o resultado do envio da atualização da Receita. Mesmo formato adicionando o resultado em uma nova coluna.



Na pasta target, há um exemplo de executável em jar do aplicativo e o respectivo arquivo.

Para executar o jar use o seguinte comando em java: 

```bash
java -jar SincronizacaoReceita-0.0.1-SNAPSHOT.jar receita.csv 
```