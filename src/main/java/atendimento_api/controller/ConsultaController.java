package atendimento_api.controller;

import atendimento_api.dto.ConsultaDTO;
import atendimento_api.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarConsulta(@RequestBody ConsultaDTO dto) {

        consultaService.salvar(dto);
    }

    @GetMapping
    public List<ConsultaDTO> buscarConsulta(
            @RequestParam(required = true) String cpf,
            @RequestParam(required = false) String dataConsulta
    ) {
        return consultaService.buscar(cpf, dataConsulta);
    }

    @PatchMapping
    public void atualizarConsulta(
            @RequestParam(required = true) String cpf,
            @RequestParam(required = true) String dataConsulta,
            @RequestParam(required = false) String especialidade,
            @RequestParam(required = false) String status
    ) {
        consultaService.atualizar(cpf, dataConsulta, especialidade, status);
    }

    @DeleteMapping
    public  void deletarConsulta(
            @RequestParam(required = true) String cpf,
            @RequestParam(required = true) String dataConsulta
    ) {
        consultaService.deletar(cpf, dataConsulta);
    }
}