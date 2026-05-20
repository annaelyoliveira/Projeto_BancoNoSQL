package atendimento_api.dto;

import lombok.Data;

@Data
public class ConsultaDTO {

    private String cpf;

    private String dataConsulta;

    private String nome;

    private String telefone;

    private String medico;

    private String especialidade;
}