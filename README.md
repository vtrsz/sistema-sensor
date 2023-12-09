

## Como rodar todas as aplicações necessárias:
1. <a href="https://docs.docker.com/engine/install/" target="_blank" rel="noopener noreferrer">Instale o Docker</a>
2. <a href="https://www.oracle.com/java/technologies/downloads/#java21" target="_blank" rel="noopener noreferrer">Instale o JDK 21</a>
3. <a href="https://www.baeldung.com/java-home-on-windows-7-8-10-mac-os-x-linux" target="_blank" rel="noopener noreferrer">Configure a Variável de Ambiente "JAVA_HOME" para a JDK 21</a>
4. Vá para pasta backend (`cd backend/`) e rode o comando no terminal para buildar o .jar
   - Windows: `mvnw.cmd clean package`
   - Linux `./mvnw clean package`
5. Volte para a pasta principal do projeto (`cd ..`) e rode o docker com o comando: 
   - `docker compose up` (caso sua versão do docker compose seja a V1, utilize o comando: `docker-compose up`)

Pronto! Todas aplicações irão iniciar.