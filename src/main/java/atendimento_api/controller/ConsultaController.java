package atendimento_api.controller;

import atendimento_api.dto.ConsultaDTO;
import atendimento_api.service.ConsultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaService consultaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarConsulta(@RequestBody ConsultaDTO consultaDTO) {

        consultaService.salvar(consultaDTO);
    }

    @GetMapping
    public List<ConsultaDTO> buscarConsulta(
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String dataConsulta
    ) {
        return consultaService.buscar(cpf, dataConsulta);
    }

    @PatchMapping
    public void atualizarConsulta(@RequestBody ConsultaDTO consultaDTO) {
        consultaService.atualizar(consultaDTO);
    }

    @DeleteMapping
    public  ConsultaDTO deletarConsulta(@RequestBody ConsultaDTO consultaDTO) {
        return consultaService.deletar(consultaDTO);

    }
    @PatchMapping("alterar-data")
    public void alterarData(@RequestBody ConsultaDTO consultaDTO) {
        consultaService.atualizarData(consultaDTO);
    }
}