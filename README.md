# WebMessagerRMI
## Compilacao
Para Compilar o projeto basta seguir o seguinte passo a passo
```bash
git clone https://github.com/rodrigodc07/WebMessagerRMI.git
cd src
find -name "*.java" > sources.txt
javac @sources.txt
rmic UERJ.client.ClientImpl
rmic UERJ.server.ServerImpl
```

## Executar
### RMI-Registry
- Para iniciar o registry do RMI basta rodar a classe java abaixo com o primeiro argumento sendo a porta desejada para o servidor RMI-Registry.
```bash
java UERJ.RMIUtils.RMIStarter porta
```
### Servidor
- Para executar o servidor primeiro é preciso ter em execução a classe do RMIStarter.
- Executar a classe java abaixo com primeiro argumento sendo a porta onde o RMI-Registry está.
```bash
java UERJ.server.ServerMain porta
```

### Client
- Para iniciar um cliente primeiro é preciso ter em execução a classe do RMIStarter.
- Para iniciar um cliente primeiro é preciso ter em execução a classe do ServerMain.
- Executar a classe java abaixo com primeiro argumento sendo a porta onde o RMI-Registry está e o segundo o nome de usuario para o bate-papo.
```bash
java UERJ.client.ClientMain porta usuario
```
