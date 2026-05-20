package atendimento_api.repository;

import atendimento_api.model.Consulta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ConsultaRepository {

    private final DynamoDbClient dynamoDbClient;

    private final String TABLE_NAME = "atendimentos";

    public void salvar(Consulta consulta) {

        Map<String, AttributeValue> item = new HashMap<>();

        item.put("PK", AttributeValue.fromS(consulta.getPk()));
        item.put("SK", AttributeValue.fromS(consulta.getSk()));

        item.put("nome", AttributeValue.fromS(consulta.getNome()));
        item.put("telefone", AttributeValue.fromS(consulta.getTelefone()));

        item.put("medico", AttributeValue.fromS(consulta.getMedico()));
        item.put("especialidade", AttributeValue.fromS(consulta.getEspecialidade()));

        item.put("status", AttributeValue.fromS(consulta.getStatus()));

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }
}