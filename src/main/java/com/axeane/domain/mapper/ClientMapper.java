package com.axeane.domain.mapper;

import com.axeane.domain.Client;
import com.axeane.domain.dto.ClientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper
public interface ClientMapper {

    @Mappings({@Mapping(target = "clientId", source = "entity.id"), @Mapping(target = "clientCin", source = "entity.cin"),
            @Mapping(target = "clientName", source = "entity.nom"), @Mapping(target = "clientPrenom", source = "entity.prenom"),
            @Mapping(target = "clientAdresse", source = "entity.adresse"), @Mapping(target = "clientEmail", source = "entity.email"),
            @Mapping(target = "clientNumTel", source = "entity.numTel")})
    ClientDTO clientToClientDTO(Client entity);

    @Mappings({@Mapping(target = "id", source = "clientId"), @Mapping(target = "cin", source = "clientCin"),
            @Mapping(target = "nom", source = "clientName"), @Mapping(target = "prenom", source = "clientPrenom"),
            @Mapping(target = "adresse", source = "clientAdresse"), @Mapping(target = "email", source = "clientEmail"),
            @Mapping(target = "numTel", source = "clientNumTel")})
    Client clientDTOToClient(ClientDTO dto);

    List<Client> convertClientDTOListToClientList(List<ClientDTO> list);

    List<ClientDTO> convertClientListToClientDTOList(List<Client> list);

//    ClientDTO clientToClientDTO(Client source);
//
//    Client clientDTOToClient(ClientDTO destination);
}
