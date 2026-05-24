package atendimento_api.repository;

import atendimento_api.dto.ConsultaDTO;
import atendimento_api.model.Consulta;
import atendimento_api.model.StatusConsulta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ConsultaRepository {

    private final DynamoDbClient dynamoDbClient;

    private final String table_name = "atendimentos";

    public void salvar(Consulta consulta) {

        Map<String, AttributeValue> item = new HashMap<>();

        item.put("PK", AttributeValue.fromS(consulta.getPk()));
        item.put("SK", AttributeValue.fromS(consulta.getSk()));

        item.put("nome", AttributeValue.fromS(consulta.getNome()));
        item.put("telefone", AttributeValue.fromS(consulta.getTelefone()));

        item.put("medico", AttributeValue.fromS(consulta.getMedico()));
        item.put("especialidade", AttributeValue.fromS(consulta.getEspecialidade()));

        item.put("status", AttributeValue.fromS(consulta.getStatus().name()));

        PutItemRequest request = PutItemRequest.builder()
                .tableName(table_name)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }

    public List<Consulta> buscar(String cpf, String dataConsulta) {
        Map<String, AttributeValue> item = new HashMap<>();

        item.put(":pkvalor", AttributeValue.fromS("PACIENTE#" + cpf));
        QueryRequest request;
        if (dataConsulta != null) {
            item.put(":skvalor", AttributeValue.fromS("CONSULTA#" + dataConsulta));
                request = QueryRequest.builder()
                    .tableName(table_name)
                    .keyConditionExpression("PK = :pkvalor AND SK = :skvalor")
                    .expressionAttributeValues(item)
                    .build();
        }
        else {
                request = QueryRequest.builder()
                    .tableName(table_name)
                    .keyConditionExpression("PK = :pkvalor")
                    .expressionAttributeValues(item)
                    .build();
        }

        List<Map<String, AttributeValue>> itens = dynamoDbClient.query(request).items();

        return itens.stream()
                .map(this::maptoConsulta)
                .collect(Collectors.toList());
    }


    public  void atualizar (String cpf, String dataConsulta, String especialidade, String status) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("PK", AttributeValue.fromS("PACIENTE#" + cpf));
        item.put("SK", AttributeValue.fromS("CONSULTA#" + dataConsulta));

        List<String> parte = new ArrayList<>();
        Map<String, AttributeValue> valores = new HashMap<>();
        Map<String, String> nomes = new HashMap<>();

        if (especialidade != null) {
            parte.add("especialidade = :especialidade");
            valores.put(":especialidade", AttributeValue.fromS(especialidade));


        }
        if (status != null) {
            parte.add("#status = :status");
            valores.put(":status", AttributeValue.fromS(status));
            nomes.put("#status", "status");
        }

        if (parte.isEmpty()) {
            return;
            //exception add
        }
        String expression = "SET " + String.join(", ", parte);


        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(table_name)
                .key(item)
                .updateExpression(expression)
                .expressionAttributeValues(valores)
                .expressionAttributeNames(nomes.isEmpty() ? null : nomes)
                .build();

        dynamoDbClient.updateItem(request);
    }


    public Consulta deletar(String cpf, String dataConsulta) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("PK", AttributeValue.fromS("PACIENTE#" + cpf));
        item.put("SK", AttributeValue.fromS("CONSULTA#" + dataConsulta));

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(table_name)
                .key(item)
                .returnValues(ReturnValue.ALL_OLD)
                .build();

        DeleteItemResponse itemdeletado = dynamoDbClient.deleteItem(request);
        Map<String, AttributeValue> atributos = itemdeletado.attributes();
        return maptoConsulta(atributos);


    }

    public void atualizarData(String cpf, String dataAntiga,  String dataatualizada) {
        Map<String, AttributeValue> keyantiga = new HashMap<>();
        keyantiga.put("PK", AttributeValue.fromS("PACIENTE#" + cpf));
        keyantiga.put("SK", AttributeValue.fromS("CONSULTA#" + dataAntiga));

        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(table_name)
                .key(keyantiga)
                .build();

        Map<String, AttributeValue> itemAntigo = dynamoDbClient.getItem(getItemRequest).item();

        if (itemAntigo == null || itemAntigo.isEmpty()) {
            return; //add exception
        }

        Map<String, AttributeValue> novoItem = new HashMap<>(itemAntigo);
        novoItem.put("SK", AttributeValue.fromS("CONSULTA#" + dataatualizada));


        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(table_name)
                .item(novoItem)
                .build();

        dynamoDbClient.putItem(putItemRequest);

        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder()
                .tableName(table_name)
                .key(keyantiga)
                .build();

        dynamoDbClient.deleteItem(deleteItemRequest);

    }


    private Consulta maptoConsulta(Map<String, AttributeValue> item) {
         Consulta consulta = new Consulta();
         consulta.setNome(item.get("nome").s());
         consulta.setPk(item.get("PK").s());
         consulta.setTelefone(item.get("telefone").s());
         consulta.setEspecialidade(item.get("especialidade").s());
         consulta.setSk(item.get("SK").s());
         consulta.setMedico(item.get("medico").s());
        String status = item.get("status").s();
        StatusConsulta statusConsulta = StatusConsulta.valueOf(status);
         consulta.setStatus(statusConsulta);
         return consulta;
    }

}
