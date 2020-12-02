package br.com.gile.livrariaapi.service;

import java.util.List;

public interface EmailService {
    public void enviaEmails(List<String> listaDeEmails, String mensagem);
}
