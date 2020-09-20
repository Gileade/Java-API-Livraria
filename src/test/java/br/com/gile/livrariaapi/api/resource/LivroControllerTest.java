package br.com.gile.livrariaapi.api.resource;

import br.com.gile.livrariaapi.api.dto.LivroDTO;
import br.com.gile.livrariaapi.exception.BusinessException;
import br.com.gile.livrariaapi.model.entity.Livro;
import br.com.gile.livrariaapi.service.LivroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class LivroControllerTest {

    static String LIVRO_API = "/api/livros";

    @Autowired
    MockMvc mvc;

    @MockBean
    LivroService service;

    @Test
    @DisplayName("Deve criar um livro com sucesso.")
    public void criaLivroTest() throws Exception{
        //Cria um Objeto Livro DTO
        LivroDTO dto = criaNovoLivro();
        //Cria um livro Mockado
        Livro livroSalvo = Livro.builder().id(10l).autor("Gile").titulo("God of War").isbn("001").build();

        //Usa um Mock para retornar o livro que foi supostamente salvo
        BDDMockito.given(service.save(Mockito.any(Livro.class))).willReturn(livroSalvo);

        //Cria um objeto Json apartir dos dados do DTO
        String json = new ObjectMapper().writeValueAsString(dto);

        //Faz uma requisição post para (LIVRO_API = "/api/livros")
        //Aceitando um arquivo JSON
        //E no caso passando o json escrito na variavel json
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LIVRO_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        //Executa a requisição
        //E espera que retorne o status isCreated (201)
        //E que retorne no Json de retorno uma propriedade id, e que ela não esteja vazia
        //E que retorne no Json de retorno uma propriedade titulo, com o valor "Meu Livro"
        //E que retorne no Json de retorno uma propriedade autor, com o valor "Autor"
        //E que retorne no Json de retorno uma propriedade isbn, com o valor 123212
        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(10l))
                .andExpect(jsonPath("titulo").value(dto.getTitulo()))
                .andExpect(jsonPath("autor").value(dto.getAutor()))
                .andExpect(jsonPath("isbn").value(dto.getIsbn()))
        ;
    }


    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficiente para criação do livro.")
    public void criaLivroInvalidoTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new LivroDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LIVRO_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(3)))
        ;
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar cadastrar um livro com isbn já cadastrado anteriormente.")
    public void criaLivroComIsbnDuplicado() throws Exception{
        LivroDTO dto = criaNovoLivro();
        String json = new ObjectMapper().writeValueAsString(dto);
        String mensagemErro = "Isbn já cadastrado.";

        BDDMockito.given(service.save(Mockito.any(Livro.class))).willThrow(new BusinessException(mensagemErro));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(LIVRO_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", hasSize(1)))
                .andExpect(jsonPath("errors[0]").value(mensagemErro));
        ;
    }

    @Test
    @DisplayName("Deve obter informações de um livro.")
    public void obtemInfomacaoDeUmLivroTest() throws Exception {
        //Cenário
        Long id = 1l;
        Livro livro = Livro.builder()
                .id(id)
                .autor(criaNovoLivro().getAutor())
                .titulo(criaNovoLivro().getTitulo())
                .isbn(criaNovoLivro().getIsbn())
                .build();

        BDDMockito.given(service.getById(id)).willReturn(Optional.of(livro));

        //Execução
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(LIVRO_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("titulo").value(criaNovoLivro().getTitulo()))
                .andExpect(jsonPath("autor").value(criaNovoLivro().getAutor()))
                .andExpect(jsonPath("isbn").value(criaNovoLivro().getIsbn()))
        ;
    }

    private LivroDTO criaNovoLivro() {
        return LivroDTO.builder().autor("Gile").titulo("God of War").isbn("001").build();
    }
}
