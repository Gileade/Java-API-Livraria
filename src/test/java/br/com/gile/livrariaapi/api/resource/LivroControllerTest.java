package br.com.gile.livrariaapi.api.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

    @Test
    @DisplayName("Deve criar um livro com sucesso.")
    public void criaLivroTest() throws Exception{
        //Cria um objeto Json
        String json = new ObjectMapper().writeValueAsString(null);

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
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("titulo").value("Meu Livro"))
                .andExpect(jsonPath("autor").value("Autor"))
                .andExpect(jsonPath("isbn").value("123212"))
        ;
    }

    @Test
    @DisplayName("Deve lançar erro de validação quando não houver dados suficiente para criação do livro.")
    public void criaLivroInvalidoTest(){

    }
}
