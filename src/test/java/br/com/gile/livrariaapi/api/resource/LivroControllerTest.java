package br.com.gile.livrariaapi.api.resource;

import br.com.gile.livrariaapi.api.dto.LivroDTO;
import br.com.gile.livrariaapi.api.model.entity.Livro;
import br.com.gile.livrariaapi.service.LivroService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

import java.util.regex.Matcher;

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
        LivroDTO dto = LivroDTO.builder().autor("Gile").titulo("God of War").isbn("001").build();
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
}
