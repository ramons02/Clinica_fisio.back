# Clinica_fisio.back.

---


```markdown
#Clínica de Fisioterapia - API Back-end

API REST robusta responsável pela persistência de dados, lógica de negócio e gerenciamento dos pacientes e agendamentos da clínica.

## Tecnologias
* **Linguagem:** Java 17
* **Framework:** Spring Boot 3.x
* **Persistência:** Spring Data JPA
* **Banco de Dados:** H2 (Desenvolvimento) / PostgreSQL (Produção)
* **Build Tool:** Maven

##Modelo de Dados (Entidades)
* **Paciente:** Armazena nome, idade, telefone, e-mail, condição clínica, histórico de lesões e controle de mensalidade.
* **Agenda:** Gerencia os horários vinculados aos pacientes, com suporte a status (AGENDADO, REALIZADA, CANCELADO).

##Endpoints Principais (API)

###Agenda
- `GET /api/agenda` - Retorna todos os agendamentos.
- `POST /api/agenda` - Cria um novo compromisso.
- `PUT /api/agenda/{id}` - Atualiza dados ou status de uma sessão.
- `DELETE /api/agenda/{id}` - Remove um agendamento.

###Pacientes
- `GET /api/pacientes` - Lista todos os pacientes cadastrados.
- `POST /api/pacientes` - Cadastro de novos pacientes (suporta Multipart para documentos/fotos).
- `GET /api/pacientes/{id}` - Detalhes de um paciente específico.

##Configuração do Ambiente
1. Certifique-se de ter o JDK 17+ instalado.
2. Configure as propriedades do banco no arquivo `src/main/resources/application.properties`.
3. Execute o comando Maven para rodar:
   ```bash
   mvn spring-boot:run
A API estará rodando em: http://localhost:8080

Segurança & CORS
A API está configurada para aceitar requisições do front-end (http://localhost:4200) através de uma política de CORS liberada para desenvolvimento.


---

### Dica de Organização:
Se você quiser deixar tudo ainda mais "top", coloque uma imagem do sistema (aqueles prints que você me mandou) dentro da pasta do Front-end e adicione no README assim:
`![Dashboard](imagem_774704.png)`

**Esses arquivos ajudam muito na organização! Gostaria que eu criasse um arquivo `.gitignore` também para evitar que arquivos desnecessários (como a pasta `node_modules`) sejam salvos no seu backup?**
