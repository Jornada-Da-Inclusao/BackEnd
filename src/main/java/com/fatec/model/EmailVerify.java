// O pacote onde o modelo de dados está localizado. Organizar pacotes é uma boa prática.
package com.fatec.model;

// Importações necessárias para utilizar funcionalidades do JPA e outras bibliotecas.
import java.time.LocalDateTime;
import jakarta.persistence.*;  // A importação do JPA (Jakarta Persistence API)

// A anotação `@Entity` indica que esta classe é uma entidade JPA. Isso significa que a classe será mapeada para uma tabela no banco de dados.
@Entity
public class EmailVerify {

    // A anotação `@Id` marca o campo `emailId` como a chave primária da tabela.
    // A anotação `@GeneratedValue` define a estratégia de geração do valor da chave primária.
    // A estratégia `GenerationType.AUTO` faz com que o JPA escolha a melhor estratégia para gerar o valor da chave (auto-incremento, sequência, etc).
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long emailId;

    // Define o ID do usuário que está associado ao e-mail. Esse campo pode ser usado para associar o e-mail a um usuário específico no sistema.
    private Long usuarioId;

    // Define o campo que armazena o endereço de e-mail do remetente.
    private String emailFrom;

    // Define o campo que armazena o endereço de e-mail do destinatário.
    private String emailTo;

    // Define o campo para o assunto do e-mail.
    private String subject;

    // O campo `text` é o conteúdo do corpo do e-mail. O tipo `TEXT` no banco de dados permite armazenar um texto longo.
    @Column(columnDefinition = "TEXT")
    private String text;

    // Define o campo que armazena a data e hora em que o e-mail foi enviado.
    private LocalDateTime sendDateEmail;

    // Define o status do e-mail, que provavelmente indica se o e-mail foi enviado com sucesso, falhou, está pendente, etc.
    // O tipo `StatusEmail` é um tipo enumerado que não está mostrado no código, mas podemos assumir que é uma enumeração que representa os status do e-mail.
    private StatusEmail statusEmail;

    // Métodos getter e setter para acessar e modificar os campos da classe.

    public Long getEmailId() {
        return emailId;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public String getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getSendDateEmail() {
        return sendDateEmail;
    }

    public void setSendDateEmail(LocalDateTime sendDateEmail) {
        this.sendDateEmail = sendDateEmail;
    }

    public StatusEmail getStatusEmail() {
        return statusEmail;
    }

    public void setStatusEmail(StatusEmail statusEmail) {
        this.statusEmail = statusEmail;
    }
}
