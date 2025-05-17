package com.example.demo;

import com.example.demo.Model.Usuario;
import com.example.demo.Service.UsuarioService;
import com.example.demo.Controller.UsuarioController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario1;
    private Usuario usuario2;

    @BeforeEach
    void setUp() {
        usuario1 = new Usuario(1L, "João", "joao@example.com");
        usuario2 = new Usuario(2L, "Maria", "maria@example.com");
    }

    @Test
    void testListarTodos() throws Exception {
        List<Usuario> lista = Arrays.asList(usuario1, usuario2);
        when(service.listarTodos()).thenReturn(lista);

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nome", is("João")))
                .andExpect(jsonPath("$[1].nome", is("Maria")));

        verify(service, times(1)).listarTodos();
    }

    @Test
    void testBuscarPorIdExistente() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(Optional.of(usuario1));

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("João")))
                .andExpect(jsonPath("$.email", is("joao@example.com")));

        verify(service, times(1)).buscarPorId(1L);
    }

    @Test
    void testBuscarPorIdNaoExistente() throws Exception {
        when(service.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/usuarios/99"))
                .andExpect(status().isNotFound());

        verify(service, times(1)).buscarPorId(99L);
    }

    @Test
    void testSalvarValido() throws Exception {
        Usuario novo = new Usuario(null, "Ana", "ana@example.com");
        Usuario salvo = new Usuario(3L, "Ana", "ana@example.com");
        when(service.salvar(any(Usuario.class))).thenReturn(salvo);

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(novo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Ana")))
                .andExpect(jsonPath("$.email", is("ana@example.com")))
                .andExpect(jsonPath("$.id", is(3)));

        verify(service, times(1)).salvar(any(Usuario.class));
    }

    @Test
    void testSalvarInvalido() throws Exception {
        Usuario invalido = new Usuario(null, "", "invalid_email");

        mockMvc.perform(post("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());

        verify(service, times(0)).salvar(any(Usuario.class));
    }

    @Test
    void testAtualizarValido() throws Exception {
        Usuario atualizado = new Usuario(1L, "João Atualizado", "joao_atualizado@example.com");
        when(service.buscarPorId(1L)).thenReturn(Optional.of(usuario1));
        when(service.salvar(any(Usuario.class))).thenReturn(atualizado);

        mockMvc.perform(put("/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("João Atualizado")))
                .andExpect(jsonPath("$.email", is("joao_atualizado@example.com")));

        verify(service, times(1)).buscarPorId(1L);
        verify(service, times(1)).salvar(any(Usuario.class));
    }

    @Test
    void testAtualizarInvalido() throws Exception {
        Usuario invalido = new Usuario(1L, "", "invalid_email");
        when(service.buscarPorId(1L)).thenReturn(Optional.of(usuario1));

        mockMvc.perform(put("/usuarios/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest());

        verify(service, times(1)).buscarPorId(1L);
        verify(service, times(0)).salvar(any(Usuario.class));
    }

    @Test
    void testAtualizarNaoExistente() throws Exception {
        when(service.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario1)))
                .andExpect(status().isNotFound());

        verify(service, times(1)).buscarPorId(99L);
        verify(service, times(0)).salvar(any(Usuario.class));
    }

    @Test
    void testDeletarExistente() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(Optional.of(usuario1));

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deletar(1L);
    }

    @Test
    void testDeletarNaoExistente() throws Exception {
        doThrow(new IllegalArgumentException("Usuário não encontrado")).when(service).deletar(99L);

        mockMvc.perform(delete("/usuarios/99"))
                .andExpect(status().isNotFound());

        verify(service, times(1)).deletar(99L);
    }
}