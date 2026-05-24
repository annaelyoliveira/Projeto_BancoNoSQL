package atendimento_api.service;

import atendimento_api.dto.ConsultaDTO;
import atendimento_api.model.Consulta;
import atendimento_api.model.StatusConsulta;
import atendimento_api.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

        consulta.setStatus(StatusConsulta.AGUARDANDO);

        consultaRepository.salvar(consulta);
    }

    public List<ConsultaDTO> buscar(String cpf, String dataConsulta) {
        List<Consulta> consultas = consultaRepository.buscar(cpf, dataConsulta);

        return  consultas.stream()
                .map(this::converterDTO)
                .collect(Collectors.toList());
    }

    public void atualizar(ConsultaDTO consultaDTO) {
        String status = null;
        if (consultaDTO.getStatus() != null) {
            status = consultaDTO.getStatus().name();
        }
        consultaRepository.atualizar(consultaDTO.getCpf(), consultaDTO.getDataConsulta(), consultaDTO.getEspecialidade(), status);
    }


    public ConsultaDTO deletar(ConsultaDTO consultaDTO) {
        Consulta consultaDeletada = consultaRepository.deletar(consultaDTO.getCpf(), consultaDTO.getDataConsulta());

        if (consultaDeletada == null) {
            return null;
            //add exception
        }

        return converterDTO(consultaDeletada);
    }

    public void atualizarData(ConsultaDTO consultaDTO) {
        consultaRepository.atualizarData(consultaDTO.getCpf(), consultaDTO.getDataConsulta(), consultaDTO.getDataConsultaatualizada());
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
        consultaDTO.setStatus(consulta.getStatus());
        return  consultaDTO;
    }

}