package atendimento_api.service;

import atendimento_api.dto.ConsultaDTO;
import atendimento_api.model.Consulta;
import atendimento_api.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository repository;

    public void salvar(ConsultaDTO dto) {

        Consulta consulta = new Consulta();

        consulta.setPk("PACIENTE#" + dto.getCpf());

        consulta.setSk("CONSULTA#" + dto.getDataConsulta());

        consulta.setNome(dto.getNome());

        consulta.setTelefone(dto.getTelefone());

        consulta.setMedico(dto.getMedico());

        consulta.setEspecialidade(dto.getEspecialidade());

        consulta.setStatus("AGUARDANDO");

        repository.salvar(consulta);
    }
}