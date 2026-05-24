package atendimento_api.model;

import lombok.Data;

@Data
public class Consulta {

    private String pk;
    private String sk;

    private String nome;
    private String telefone;
    private String medico;
    private String especialidade;
    private StatusConsulta status;
}