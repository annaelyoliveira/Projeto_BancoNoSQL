package atendimento_api.controller;

import atendimento_api.dto.ConsultaDTO;
import atendimento_api.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarConsulta(@RequestBody ConsultaDTO dto) {

        service.salvar(dto);
    }

    @GetMapping
    public String teste() {

        return "API funcionando!";
    }
}