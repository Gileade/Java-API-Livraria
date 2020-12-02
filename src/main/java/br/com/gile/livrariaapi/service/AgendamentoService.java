package br.com.gile.livrariaapi.service;

import br.com.gile.livrariaapi.model.entity.Emprestimo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private static final String CRON_EMPRESTIMOS_ATRASADOS = "0 0 0 1/1 * ?";

    @Value("${application.email.emprestimoatrasado.mensagem}")
    private String mensagem;

    private final EmprestimoService emprestimoService;
    private final EmailService emailService;

    @Scheduled(cron = CRON_EMPRESTIMOS_ATRASADOS)
    public void enviaEmailParaEmprestimosAtrasados(){
        List<Emprestimo> allEmprestimosAtrasados = emprestimoService.getAllEmprestimosAtrasados();
        List<String> listaDeEmails = allEmprestimosAtrasados.stream()
                .map(emprestimo -> emprestimo.getEmailCliente())
                .collect(Collectors.toList());

        emailService.enviaEmails(mensagem,listaDeEmails);
    }
}
