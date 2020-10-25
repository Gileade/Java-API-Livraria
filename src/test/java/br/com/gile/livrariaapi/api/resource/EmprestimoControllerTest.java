package br.com.gile.livrariaapi.api.resource;

import br.com.gile.livrariaapi.api.dto.EmprestimoDto;
import br.com.gile.livrariaapi.api.dto.EmprestimoRetornadoDTO;
import br.com.gile.livrariaapi.exception.BusinessException;
import br.com.gile.livrariaapi.model.entity.Emprestimo;
import br.com.gile.livrariaapi.model.entity.Livro;
import br.com.gile.livrariaapi.service.EmprestimoService;
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

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = EmprestimoController.class)
@AutoConfigureMockMvc
public class EmprestimoControllerTest {

    static final String EMPRESTIMO_API = "/api/emprestimos";

    @Autowired
    MockMvc mvc;

    @MockBean
    private LivroService livroService;
    @MockBean
    private EmprestimoService emprestimoService;

    @Test
    @DisplayName("Deve realizar um emprestimo.")
    public void criaEmprestimoTest() throws Exception{
        EmprestimoDto dto = EmprestimoDto.builder().isbn("123").cliente("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        Livro livro = Livro.builder().id(1l).isbn("123").build();
        BDDMockito.given(livroService.getByIsbn("123")).willReturn(Optional.of(livro));

        Emprestimo emprestimo = Emprestimo.builder().id(1l).cliente("Fulano").livro(livro).dataDoEmprestimo(LocalDate.now()).build();
        BDDMockito.given(emprestimoService.save(Mockito.any(Emprestimo.class))).willReturn(emprestimo);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(EMPRESTIMO_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar fazer emprestimo de um livro inexistente.")
    public void isbnInvalidoCriaEmprestimoTest() throws Exception{
        EmprestimoDto dto = EmprestimoDto.builder().isbn("123").cliente("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        BDDMockito.given(livroService.getByIsbn("123")).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(EMPRESTIMO_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Livro não encontrado para o isbn"));
    }

    @Test
    @DisplayName("Deve retornar erro ao tentar fazer emprestimo de um livro já emprestado.")
    public void livroEmprestadoErrorCriaEmprestimoTest() throws Exception{
        EmprestimoDto dto = EmprestimoDto.builder().isbn("123").cliente("Fulano").build();
        String json = new ObjectMapper().writeValueAsString(dto);

        Livro livro = Livro.builder().id(1l).isbn("123").build();
        BDDMockito.given(livroService.getByIsbn("123")).willReturn(Optional.of(livro));

        BDDMockito.given(emprestimoService.save(Mockito.any(Emprestimo.class)))
                .willThrow(new BusinessException("Livro já emprestado"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(EMPRESTIMO_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("errors[0]").value("Livro já emprestado"));
    }

    @Test
    @DisplayName("Deve retornar um livro")
    public void retornaLivroTest() throws Exception {
        EmprestimoRetornadoDTO dto = EmprestimoRetornadoDTO.builder().retornado(true).build();
        Emprestimo emprestimo = Emprestimo.builder().id(1l).build();
        BDDMockito.given(emprestimoService.getById(Mockito.anyLong()))
                .willReturn(Optional.of(emprestimo));

        String json = new ObjectMapper().writeValueAsString(dto);

        mvc.perform(
            patch(EMPRESTIMO_API.concat("/1"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        ).andExpect(status().isOk());

        Mockito.verify(emprestimoService, Mockito.times(1)).update(emprestimo);
    }
}
