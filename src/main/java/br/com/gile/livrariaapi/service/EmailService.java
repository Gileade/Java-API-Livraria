package br.com.gile.livrariaapi.service;

import java.util.List;

public interface EmailService {
    public void enviaEmails(String mensagem, List<String> listaDeEmails);
}
