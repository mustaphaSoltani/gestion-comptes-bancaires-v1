package com.axeane.domain.mapper;

import com.axeane.domain.Client;
import com.axeane.domain.dto.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper
public interface ClientMapper {

    @Mappings({@Mapping(target = "clientId", source = "client.id"),
            @Mapping(target = "clientCin", source = "client.cin"),
            @Mapping(target = "clientName", source = "client.nom"),
            @Mapping(target = "clientPrenom", source = "client.prenom"),
            @Mapping(target = "clientAdresse", source = "client.adresse"),
            @Mapping(target = "clientEmail", source = "client.email"),
            @Mapping(target = "clientNumTel", source = "client.numTel"),
            @Mapping(target = "comptes", source = "client.comptes")
    })
    ClientDTO clientToClientDTO(Client client);

    @Mappings({@Mapping(target = "id", source = "clientId"),
            @Mapping(target = "cin", source = "clientCin"),
            @Mapping(target = "nom", source = "clientName"),
            @Mapping(target = "prenom", source = "clientPrenom"),
            @Mapping(target = "adresse", source = "clientAdresse"),
            @Mapping(target = "email", source = "clientEmail"),
            @Mapping(target = "numTel", source = "clientNumTel"),
            @Mapping(target = "comptes", source = "comptes")
    })
    Client clientDTOToClient(ClientDTO dto);

    List<Client> convertClientDTOListToClientList(List<ClientDTO> list);

    List<ClientDTO> convertClientListToClientDTOList(List<Client> list);

}
