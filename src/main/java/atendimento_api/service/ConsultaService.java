package atendimento_api.service;

import atendimento_api.dto.ConsultaDTO;
import atendimento_api.model.Consulta;
import atendimento_api.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;

    public void salvar(ConsultaDTO consultaDTO) {

        Consulta consulta = new Consulta();

        consulta.setPk("PACIENTE#" + consultaDTO.getCpf());

        consulta.setSk("CONSULTA#" + consultaDTO.getDataConsulta());

        consulta.setNome(consultaDTO.getNome());

        consulta.setTelefone(consultaDTO.getTelefone());

        consulta.setMedico(consultaDTO.getMedico());

        consulta.setEspecialidade(consultaDTO.getEspecialidade());

        consulta.setStatus("AGUARDANDO");

        consultaRepository.salvar(consulta);
    }

    public List<ConsultaDTO> buscar(String cpf, String dataConsulta) {
        List<Consulta> consultas = consultaRepository.buscar(cpf, dataConsulta);

        return  consultas.stream()
                .map(this::converterDTO)
                .collect(Collectors.toList());
    }

    public void atualizar(String cpf, String dataConsulta, String especialidade, String status) {
        consultaRepository.atualizar(cpf, dataConsulta, especialidade, status);
    }


    public void deletar(String cpf, String dataConsulta) {
        consultaRepository.deletar(cpf, dataConsulta);
    }

    private ConsultaDTO converterDTO(Consulta consulta) {
        ConsultaDTO consultaDTO = new ConsultaDTO();

        consultaDTO.setNome(consulta.getNome());
        consultaDTO.setTelefone(consulta.getTelefone());

        String cpf = consulta.getPk().replace("PACIENTE#", "");
        consultaDTO.setCpf(cpf);

        String data = consulta.getSk().replace("CONSULTA#", "");
        consultaDTO.setDataConsulta(data);

        consultaDTO.setEspecialidade(consulta.getEspecialidade());
        consultaDTO.setMedico(consulta.getMedico());

        return  consultaDTO;
    }
}