# WebMessagerRMI
## Compilacao
Para Compilar o projeto basta seguir o seguinte passo a passo
```bash
git clone https://github.com/rodrigodc07/WebMessagerRMI.git
cd WebMessagerRMI/src
find -name "*.java" > sources.txt
javac @sources.txt
rmic UERJ.client.ClientImpl
rmic UERJ.server.ServerImpl
```
## Decições de Projeto
### Endereçamento, Portas
O programa tem 3 variaveis de ambiente, as quais são definidas no arquivo application.properties.
- rmi.port: A porta onde será rodado o servidor de nomes do RMI
- socket.address: O endereço ip de multicast utilizado pelos servidores de envio de menssagem
- socket.port: A porta o onde será inicializado o servidor de envio de mensagem.
### Registro
O Registro do RMI é iniciado por um programa java assim como o cliente e o servidor.
### Properties
Este pacote contem apenas uma classe java JavaProperties, reponsavel por obter as variaveis definidas no arquivo application.properties e.
### RMIUtils
Este pacote contem duas classes java, a primeira RMIStarter é reponsavel por iniciar o servidor de nomes RMI e mante-lo em execução, ela utiliza a classe JavaProperties para obter o valor da porta onde deverá rodar o servidor de nomes RMI.

Já a classe RMIRegistry é uma classes com os seguintes metodos estaticos para facilitar a comunicação com o servidor RMI
- getObjectFromRMI
- registryInRMI
- listObjects
- getRegistry
### Server
Este pacote possui duas classes java e uma interface. responsaveis por definir o servidor de envios de menssagens e inicia-lo.

A interface ServerInterface diz que um servidor de extender a interface remote, além de ter dois metodos
- sendMessage: Envia uma menssagem para os outros servidores.
- pushMessage: Envia uma menssagem para seus clientes.

A classe ServerImpl implementa a interface ServerInterface, tem um objeto do tipo messageSenderService e outro do tipo messageReceiverService, os quais são utilizados para implementar os metodos definidos pela interface.

E no seu construtor utiliza da classe JavaProperties para obter a variavel de ambiente da porta na qual está o servidor de RMI, além de iniciar os objetos messageSenderService e messageReceiverService, este ultimo enviando a lambda para quando uma menssagem for recebir utilizar o metodo pushMenssage().

### Client
### Input
### Output
## Executar
### RMI-Registry
- Para iniciar o registry do RMI basta rodar a classe java desejada para o servidor RMI-Registry.
```bash
java UERJ.RMIUtils.RMIStarter
```
### Servidor
- Para executar o servidor primeiro é preciso ter em execução a classe do RMIStarter.
- Executar a classe java abaixo
```bash
java UERJ.server.ServerMain
```

### Client
- Para iniciar um cliente primeiro é preciso ter em execução a classe do RMIStarter.
- Para iniciar um cliente primeiro é preciso ter em execução a classe do ServerMain.
- Executar a classe java abaixo e o primeiro argumento passado ao java é o nome de usuario para o bate-papo.
```bash
java UERJ.client.ClientMain usuario
```
