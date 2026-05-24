package atendimento_api.dto;

import atendimento_api.model.StatusConsulta;
import lombok.Data;

@Data
public class ConsultaDTO {

    private String nome;
    private String cpf;
    private String telefone;
    private String dataConsulta;
    private String dataConsultaatualizada;
    private String especialidade;
    private String medico;
    private StatusConsulta status;

    public ConsultaDTO() {};
}