package atendimento_api.dto;

import lombok.Data;

@Data
public class ConsultaDTO {

    private String nome;
    private String cpf;
    private String telefone;
    private String dataConsulta;
    private String especialidade;
    private String medico;

    public ConsultaDTO() {};
}