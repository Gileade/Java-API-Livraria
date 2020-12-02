package br.com.gile.livrariaapi.service.impl;

import br.com.gile.livrariaapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Value("${application.email.default-remetente}")
    private String remetente;

    private final JavaMailSender javaMailSender;

    @Override
    public void enviaEmails(String mensagem, List<String> listaDeEmails) {
        String[] emails = listaDeEmails.toArray(new String[listaDeEmails.size()]);

        SimpleMailMessage mensagemDeEmail = new SimpleMailMessage();
        mensagemDeEmail.setFrom(remetente);
        mensagemDeEmail.setSubject("Livro com empréstimo atrasado");
        mensagemDeEmail.setText(mensagem);
        mensagemDeEmail.setTo(emails);

        javaMailSender.send(mensagemDeEmail);
    }
}
