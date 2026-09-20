# Motiva Verde - Sprint 3

## Integrantes

| Nome | RM |
|------|----|
| Arthur da Silva Alencar | 563684 |
| Felipe Paula Burba Molonhoni | 564395 |
| Lucas de Freitas Barbosa | 564685 |
| Pedro Del Neri Correia | 562168 |
| Vitor Limeira dos Santos | 565280 |

---

## Sobre o projeto

O Motiva Verde é uma solução voltada ao gerenciamento e à priorização de intervenções de manutenção da vegetação em trechos rodoviários.

O sistema utiliza informações como:

- quilômetro do trecho;
- altura da vegetação;
- condição de crescimento;
- equipe responsável;
- monitoramento por sensores IoT;
- intervenções operacionais;
- prioridade da intervenção.

As prioridades utilizadas pelo sistema são:

- `BAIXA`
- `MODERADA`
- `ALTA`
- `URGENTE`

---

## Novidades da Sprint 3

Nesta Sprint foram implementados:

- Persistência de dados com Oracle Database;
- Conexão com banco utilizando JDBC;
- Uso do Oracle JDBC Driver (`ojdbc17.jar`);
- Padrão DAO para acesso aos dados;
- CRUD completo das principais entidades;
- Scripts SQL para criação das tabelas;
- Script SQL para carga de dados de teste;
- Persistência do histórico de relatórios;
- Integração entre o Motor de Prioridade e o banco de dados;
- Carregamento dos trechos diretamente do Oracle;
- Uso de `PreparedStatement`;
- Uso de `Record` para representar registros retornados pelo banco;
- Tratamento de exceções relacionadas ao banco de dados.

---

## Estrutura do projeto

```text
MOTIVAVERDE_POO_SPRINT03/
├── lib/
│   └── ojdbc17.jar
│
├── sql/
│   ├── script-criacao.sql
│   └── script-dados.sql
│
├── src/
│   └── br/com/motivaverde/
│       ├── dao/
│       │   ├── EquipeManutencaoDAO.java
│       │   ├── IntervencaoOperacionalDAO.java
│       │   ├── RelatorioPrioridadeDAO.java
│       │   └── TrechoRodoviaDAO.java
│       │
│       ├── db/
│       │   └── ConexaoBD.java
│       │
│       ├── interfaces/
│       │
│       ├── intervencao/
│       │
│       ├── main/
│       │   └── Main.java
│       │
│       ├── model/
│       │
│       └── service/
│           ├── GeradorRelatorio.java
│           └── MotorPrioridade.java
│
├── .gitignore
└── README.md
```

---

## Banco de dados

O projeto utiliza **Oracle Database**.

### Configuração

```text
Host: oracle.fiap.com.br
Porta: 1521
SID: ORCL
```

As credenciais não são armazenadas diretamente no código-fonte.

O projeto utiliza as seguintes variáveis de ambiente:

```text
ORACLE_USER
ORACLE_PASSWORD
```

### Configuração das variáveis no PowerShell

```powershell
$env:ORACLE_USER="SEU_USUARIO"
$env:ORACLE_PASSWORD="SUA_SENHA"
```

---

## Conexão JDBC

A conexão com o Oracle é realizada pela classe:

```text
ConexaoBD.java
```

O projeto utiliza o driver:

```text
ojdbc17.jar
```

armazenado dentro da pasta:

```text
lib/
```

A classe `ConexaoBD` utiliza o padrão Singleton para manter uma instância centralizada da conexão com o banco.

---

## Scripts SQL

### script-criacao.sql

O arquivo:

```text
sql/script-criacao.sql
```

é responsável pela criação das tabelas utilizadas pela aplicação:

```text
TB_EQUIPE_MANUTENCAO
TB_TRECHO_RODOVIA
TB_INTERVENCAO_OPERACIONAL
TB_RELATORIO_PRIORIDADE
```

As tabelas utilizam:

- chaves primárias;
- chaves estrangeiras;
- constraints;
- campos `IDENTITY`;
- valores padrão;
- relacionamentos entre as entidades.

### script-dados.sql

O arquivo:

```text
sql/script-dados.sql
```

é responsável pela carga de dados utilizados para testes da aplicação.

O script também realiza a limpeza dos dados de teste anteriores antes de inserir uma nova carga, evitando duplicidades.

---

## Tabelas

### TB_EQUIPE_MANUTENCAO

Armazena as equipes responsáveis pelas atividades de manutenção.

Principais informações:

```text
ID_EQUIPE
NOME
ESPECIALIDADE
```

---

### TB_TRECHO_RODOVIA

Armazena os trechos monitorados pelo sistema.

Principais informações:

```text
ID_TRECHO
QUILOMETRO
ALTURA_VEGETACAO
CONDICAO_CRESCIMENTO
ID_EQUIPE
TIPO_TRECHO
CODIGO_SENSOR
```

O campo `TIPO_TRECHO` permite diferenciar trechos comuns de trechos monitorados via IoT.

Exemplos:

```text
RODOVIA
MONITORADO
```

---

### TB_INTERVENCAO_OPERACIONAL

Armazena o histórico das intervenções realizadas.

Os tipos de intervenção utilizados pelo sistema são:

```text
ROCADA_MECANIZADA
PULVERIZACAO
```

Também são armazenadas informações como:

```text
DATA_EXECUCAO
ALTURA_ANTES
ALTURA_DEPOIS
```

---

### TB_RELATORIO_PRIORIDADE

Armazena o histórico dos relatórios gerados pelo sistema.

Cada relatório registra a quantidade de trechos classificados como:

```text
BAIXA
MODERADA
ALTA
URGENTE
```

Também é armazenado um resumo da análise.

---

## Padrão DAO

A Sprint 3 utiliza o padrão **DAO - Data Access Object** para separar a lógica de persistência da lógica da aplicação.

Foram implementados os seguintes DAOs:

```text
EquipeManutencaoDAO
TrechoRodoviaDAO
IntervencaoOperacionalDAO
RelatorioPrioridadeDAO
```

Todos seguem o padrão CRUD solicitado.

---

## EquipeManutencaoDAO

Responsável pelas operações relacionadas às equipes de manutenção.

Métodos implementados:

```text
inserir()
buscarPorId()
listarTodas()
atualizar()
deletar()
```

---

## TrechoRodoviaDAO

Responsável pela persistência dos trechos rodoviários.

Métodos implementados:

```text
inserir()
buscarPorId()
listarTodas()
atualizar()
deletar()
```

O DAO também suporta os dados específicos de um trecho monitorado, como:

```text
TIPO_TRECHO
CODIGO_SENSOR
```

---

## IntervencaoOperacionalDAO

Responsável pela persistência das intervenções operacionais.

Métodos implementados:

```text
inserir()
buscarPorId()
listarTodas()
atualizar()
deletar()
```

As intervenções utilizadas no projeto são:

```text
RocadaMecanizada
Pulverizacao
```

---

## RelatorioPrioridadeDAO

Responsável pelo histórico dos relatórios de prioridade.

Métodos implementados:

```text
inserir()
buscarPorId()
listarTodas()
atualizar()
deletar()
salvarRelatorio()
```

O método:

```text
salvarRelatorio()
```

é utilizado pelo `GeradorRelatorio` para persistir automaticamente o resultado de uma análise.

---

## PreparedStatement

Todas as operações que recebem parâmetros utilizam `PreparedStatement`.

Exemplo conceitual:

```java
String sql = "SELECT * FROM TB_EQUIPE_MANUTENCAO WHERE ID_EQUIPE = ?";

PreparedStatement stmt = conn.prepareStatement(sql);

stmt.setLong(1, id);
```

Dessa forma, os valores não são concatenados diretamente às consultas SQL.

---

## Records

Os DAOs utilizam `Record` para representar registros retornados pelo banco de dados.

Exemplos:

```text
EquipeRegistro
TrechoRegistro
IntervencaoRegistro
RelatorioRegistro
```

Esses objetos representam os dados persistidos no Oracle durante as operações dos DAOs.

---

## Motor de Prioridade

A classe:

```text
MotorPrioridade.java
```

mantém a lógica de análise desenvolvida nas Sprints anteriores.

O motor analisa cada trecho e determina sua prioridade:

```text
BAIXA
MODERADA
ALTA
URGENTE
```

Trechos que implementam monitoramento via IoT podem atualizar seus dados antes da classificação.

Dependendo da situação, o sistema também pode indicar intervenções como:

```text
Roçada Mecanizada
Pulverização
```

---

## Gerador de Relatório

A classe:

```text
GeradorRelatorio.java
```

é responsável por integrar o Motor de Prioridade ao banco de dados.

O fluxo ocorre da seguinte maneira:

```text
Trechos
   ↓
MotorPrioridade
   ↓
Classificação das prioridades
   ↓
ResultadoPrioridades
   ↓
GeradorRelatorio
   ↓
RelatorioPrioridadeDAO
   ↓
Oracle Database
```

Dessa forma, o relatório continua sendo apresentado no console, mas também passa a possuir um histórico persistente no banco de dados.

---

## Carregamento dos trechos

Nas Sprints anteriores, os trechos eram instanciados diretamente dentro do código Java.

Na Sprint 3, eles passam a ser carregados através do Oracle.

O fluxo é:

```text
Oracle Database
       ↓
TrechoRodoviaDAO
       ↓
TrechoRegistro
       ↓
TrechoRodovia / TrechoMonitorado
       ↓
MotorPrioridade
```

Assim, o sistema utiliza dados persistidos em vez de depender apenas de objetos criados diretamente no código.

---

## Main

A classe principal está localizada em:

```text
src/br/com/motivaverde/main/Main.java
```

Durante a execução, o sistema demonstra:

1. conexão com o Oracle;
2. CRUD de equipes de manutenção;
3. CRUD de trechos rodoviários;
4. CRUD de intervenções operacionais;
5. CRUD de relatórios;
6. carregamento dos trechos persistidos;
7. geração do relatório de prioridade;
8. persistência automática do relatório;
9. consulta ao histórico;
10. encerramento da conexão com o banco.

Os registros temporários utilizados na demonstração dos CRUDs são removidos ao final dos testes.

---

## Como executar

### 1. Criar as tabelas

Execute no Oracle:

```text
sql/script-criacao.sql
```

---

### 2. Carregar os dados de teste

Depois execute:

```text
sql/script-dados.sql
```

---

### 3. Configurar as credenciais

No PowerShell:

```powershell
$env:ORACLE_USER="SEU_USUARIO"
$env:ORACLE_PASSWORD="SUA_SENHA"
```

---

### 4. Compilar o projeto

Na raiz do projeto:

```powershell
$files = Get-ChildItem -Recurse -Path src -Filter *.java | ForEach-Object { $_.FullName }
```

Depois:

```powershell
javac -cp "lib/ojdbc17.jar" -d bin $files
```

---

### 5. Executar a aplicação

```powershell
java -cp "bin;lib/ojdbc17.jar" br.com.motivaverde.main.Main
```

---

## Tecnologias utilizadas

```text
Java 21
Oracle Database
JDBC
Oracle JDBC Driver
SQL
Git
GitHub
Visual Studio Code
Oracle SQL Developer for VS Code
```

---

## Segurança

As credenciais do Oracle não são armazenadas diretamente dentro das classes Java.

O projeto utiliza variáveis de ambiente:

```text
ORACLE_USER
ORACLE_PASSWORD
```

Dessa forma, informações sensíveis não precisam ser publicadas no repositório do GitHub.

---

## Conclusão

A Sprint 3 evolui o Motiva Verde adicionando uma camada completa de persistência de dados.

O sistema passa a integrar:

```text
Orientação a Objetos
        +
       JDBC
        +
     Padrão DAO
        +
 Oracle Database
        +
Persistência de Relatórios
```

Com isso, equipes, trechos, intervenções e relatórios deixam de existir apenas durante a execução da aplicação e passam a ser armazenados e consultados através do banco de dados Oracle.## Sobre o projeto

O Motiva Verde é uma solução voltada ao gerenciamento e à priorização de intervenções de manutenção da vegetação em trechos rodoviários.

O sistema utiliza informações como:

- quilômetro do trecho;
- altura da vegetação;
- condição de crescimento;
- equipe responsável;
- monitoramento por sensores IoT;
- intervenções operacionais;
- prioridade da intervenção.

As prioridades utilizadas pelo sistema são:

- `BAIXA`
- `MODERADA`
- `ALTA`
- `URGENTE`

---

## Novidades da Sprint 3

Nesta Sprint foram implementados:

- Persistência de dados com Oracle Database;
- Conexão com banco utilizando JDBC;
- Uso do Oracle JDBC Driver (`ojdbc17.jar`);
- Padrão DAO para acesso aos dados;
- CRUD completo das principais entidades;
- Scripts SQL para criação das tabelas;
- Script SQL para carga de dados de teste;
- Persistência do histórico de relatórios;
- Integração entre o Motor de Prioridade e o banco de dados;
- Carregamento dos trechos diretamente do Oracle;
- Uso de `PreparedStatement`;
- Uso de `Record` para representar registros retornados pelo banco;
- Tratamento de exceções relacionadas ao banco de dados.

---

## Estrutura do projeto

```text
MOTIVAVERDE_POO_SPRINT03/
├── lib/
│   └── ojdbc17.jar
│
├── sql/
│   ├── script-criacao.sql
│   └── script-dados.sql
│
├── src/
│   └── br/com/motivaverde/
│       ├── dao/
│       │   ├── EquipeManutencaoDAO.java
│       │   ├── IntervencaoOperacionalDAO.java
│       │   ├── RelatorioPrioridadeDAO.java
│       │   └── TrechoRodoviaDAO.java
│       │
│       ├── db/
│       │   └── ConexaoBD.java
│       │
│       ├── interfaces/
│       │
│       ├── intervencao/
│       │
│       ├── main/
│       │   └── Main.java
│       │
│       ├── model/
│       │
│       └── service/
│           ├── GeradorRelatorio.java
│           └── MotorPrioridade.java
│
├── .gitignore
└── README.md
```

---

## Banco de dados

O projeto utiliza **Oracle Database**.

### Configuração

```text
Host: oracle.fiap.com.br
Porta: 1521
SID: ORCL
```

As credenciais não são armazenadas diretamente no código-fonte.

O projeto utiliza as seguintes variáveis de ambiente:

```text
ORACLE_USER
ORACLE_PASSWORD
```

### Configuração das variáveis no PowerShell

```powershell
$env:ORACLE_USER="SEU_USUARIO"
$env:ORACLE_PASSWORD="SUA_SENHA"
```

---

## Conexão JDBC

A conexão com o Oracle é realizada pela classe:

```text
ConexaoBD.java
```

O projeto utiliza o driver:

```text
ojdbc17.jar
```

armazenado dentro da pasta:

```text
lib/
```

A classe `ConexaoBD` utiliza o padrão Singleton para manter uma instância centralizada da conexão com o banco.

---

## Scripts SQL

### script-criacao.sql

O arquivo:

```text
sql/script-criacao.sql
```

é responsável pela criação das tabelas utilizadas pela aplicação:

```text
TB_EQUIPE_MANUTENCAO
TB_TRECHO_RODOVIA
TB_INTERVENCAO_OPERACIONAL
TB_RELATORIO_PRIORIDADE
```

As tabelas utilizam:

- chaves primárias;
- chaves estrangeiras;
- constraints;
- campos `IDENTITY`;
- valores padrão;
- relacionamentos entre as entidades.

### script-dados.sql

O arquivo:

```text
sql/script-dados.sql
```

é responsável pela carga de dados utilizados para testes da aplicação.

O script também realiza a limpeza dos dados de teste anteriores antes de inserir uma nova carga, evitando duplicidades.

---

## Tabelas

### TB_EQUIPE_MANUTENCAO

Armazena as equipes responsáveis pelas atividades de manutenção.

Principais informações:

```text
ID_EQUIPE
NOME
ESPECIALIDADE
```

---

### TB_TRECHO_RODOVIA

Armazena os trechos monitorados pelo sistema.

Principais informações:

```text
ID_TRECHO
QUILOMETRO
ALTURA_VEGETACAO
CONDICAO_CRESCIMENTO
ID_EQUIPE
TIPO_TRECHO
CODIGO_SENSOR
```

O campo `TIPO_TRECHO` permite diferenciar trechos comuns de trechos monitorados via IoT.

Exemplos:

```text
RODOVIA
MONITORADO
```

---

### TB_INTERVENCAO_OPERACIONAL

Armazena o histórico das intervenções realizadas.

Os tipos de intervenção utilizados pelo sistema são:

```text
ROCADA_MECANIZADA
PULVERIZACAO
```

Também são armazenadas informações como:

```text
DATA_EXECUCAO
ALTURA_ANTES
ALTURA_DEPOIS
```

---

### TB_RELATORIO_PRIORIDADE

Armazena o histórico dos relatórios gerados pelo sistema.

Cada relatório registra a quantidade de trechos classificados como:

```text
BAIXA
MODERADA
ALTA
URGENTE
```

Também é armazenado um resumo da análise.

---

## Padrão DAO

A Sprint 3 utiliza o padrão **DAO - Data Access Object** para separar a lógica de persistência da lógica da aplicação.

Foram implementados os seguintes DAOs:

```text
EquipeManutencaoDAO
TrechoRodoviaDAO
IntervencaoOperacionalDAO
RelatorioPrioridadeDAO
```

Todos seguem o padrão CRUD solicitado.

---

## EquipeManutencaoDAO

Responsável pelas operações relacionadas às equipes de manutenção.

Métodos implementados:

```text
inserir()
buscarPorId()
listarTodas()
atualizar()
deletar()
```

---

## TrechoRodoviaDAO

Responsável pela persistência dos trechos rodoviários.

Métodos implementados:

```text
inserir()
buscarPorId()
listarTodas()
atualizar()
deletar()
```

O DAO também suporta os dados específicos de um trecho monitorado, como:

```text
TIPO_TRECHO
CODIGO_SENSOR
```

---

## IntervencaoOperacionalDAO

Responsável pela persistência das intervenções operacionais.

Métodos implementados:

```text
inserir()
buscarPorId()
listarTodas()
atualizar()
deletar()
```

As intervenções utilizadas no projeto são:

```text
RocadaMecanizada
Pulverizacao
```

---

## RelatorioPrioridadeDAO

Responsável pelo histórico dos relatórios de prioridade.

Métodos implementados:

```text
inserir()
buscarPorId()
listarTodas()
atualizar()
deletar()
salvarRelatorio()
```

O método:

```text
salvarRelatorio()
```

é utilizado pelo `GeradorRelatorio` para persistir automaticamente o resultado de uma análise.

---

## PreparedStatement

Todas as operações que recebem parâmetros utilizam `PreparedStatement`.

Exemplo conceitual:

```java
String sql = "SELECT * FROM TB_EQUIPE_MANUTENCAO WHERE ID_EQUIPE = ?";

PreparedStatement stmt = conn.prepareStatement(sql);

stmt.setLong(1, id);
```

Dessa forma, os valores não são concatenados diretamente às consultas SQL.

---

## Records

Os DAOs utilizam `Record` para representar registros retornados pelo banco de dados.

Exemplos:

```text
EquipeRegistro
TrechoRegistro
IntervencaoRegistro
RelatorioRegistro
```

Esses objetos representam os dados persistidos no Oracle durante as operações dos DAOs.

---

## Motor de Prioridade

A classe:

```text
MotorPrioridade.java
```

mantém a lógica de análise desenvolvida nas Sprints anteriores.

O motor analisa cada trecho e determina sua prioridade:

```text
BAIXA
MODERADA
ALTA
URGENTE
```

Trechos que implementam monitoramento via IoT podem atualizar seus dados antes da classificação.

Dependendo da situação, o sistema também pode indicar intervenções como:

```text
Roçada Mecanizada
Pulverização
```

---

## Gerador de Relatório

A classe:

```text
GeradorRelatorio.java
```

é responsável por integrar o Motor de Prioridade ao banco de dados.

O fluxo ocorre da seguinte maneira:

```text
Trechos
   ↓
MotorPrioridade
   ↓
Classificação das prioridades
   ↓
ResultadoPrioridades
   ↓
GeradorRelatorio
   ↓
RelatorioPrioridadeDAO
   ↓
Oracle Database
```

Dessa forma, o relatório continua sendo apresentado no console, mas também passa a possuir um histórico persistente no banco de dados.

---

## Carregamento dos trechos

Nas Sprints anteriores, os trechos eram instanciados diretamente dentro do código Java.

Na Sprint 3, eles passam a ser carregados através do Oracle.

O fluxo é:

```text
Oracle Database
       ↓
TrechoRodoviaDAO
       ↓
TrechoRegistro
       ↓
TrechoRodovia / TrechoMonitorado
       ↓
MotorPrioridade
```

Assim, o sistema utiliza dados persistidos em vez de depender apenas de objetos criados diretamente no código.

---

## Main

A classe principal está localizada em:

```text
src/br/com/motivaverde/main/Main.java
```

Durante a execução, o sistema demonstra:

1. conexão com o Oracle;
2. CRUD de equipes de manutenção;
3. CRUD de trechos rodoviários;
4. CRUD de intervenções operacionais;
5. CRUD de relatórios;
6. carregamento dos trechos persistidos;
7. geração do relatório de prioridade;
8. persistência automática do relatório;
9. consulta ao histórico;
10. encerramento da conexão com o banco.

Os registros temporários utilizados na demonstração dos CRUDs são removidos ao final dos testes.

---

## Como executar

### 1. Criar as tabelas

Execute no Oracle:

```text
sql/script-criacao.sql
```

---

### 2. Carregar os dados de teste

Depois execute:

```text
sql/script-dados.sql
```

---

### 3. Configurar as credenciais

No PowerShell:

```powershell
$env:ORACLE_USER="SEU_USUARIO"
$env:ORACLE_PASSWORD="SUA_SENHA"
```

---

### 4. Compilar o projeto

Na raiz do projeto:

```powershell
$files = Get-ChildItem -Recurse -Path src -Filter *.java | ForEach-Object { $_.FullName }
```

Depois:

```powershell
javac -cp "lib/ojdbc17.jar" -d bin $files
```

---

### 5. Executar a aplicação

```powershell
java -cp "bin;lib/ojdbc17.jar" br.com.motivaverde.main.Main
```

---

## Tecnologias utilizadas

```text
Java 21
Oracle Database
JDBC
Oracle JDBC Driver
SQL
Git
GitHub
Visual Studio Code
Oracle SQL Developer for VS Code
```

---

## Segurança

As credenciais do Oracle não são armazenadas diretamente dentro das classes Java.

O projeto utiliza variáveis de ambiente:

```text
ORACLE_USER
ORACLE_PASSWORD
```

Dessa forma, informações sensíveis não precisam ser publicadas no repositório do GitHub.

---

## Conclusão

A Sprint 3 evolui o Motiva Verde adicionando uma camada completa de persistência de dados.

O sistema passa a integrar:

```text
Orientação a Objetos
        +
       JDBC
        +
     Padrão DAO
        +
 Oracle Database
        +
Persistência de Relatórios
```

Com isso, equipes, trechos, intervenções e relatórios deixam de existir apenas durante a execução da aplicação e passam a ser armazenados e consultados através do banco de dados Oracle.