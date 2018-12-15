package com.axeane.domain.mapper;

import com.axeane.domain.Client;
import com.axeane.domain.dto.ClientDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {CompteMapper.class})
public interface ClientMapper {
    @InheritInverseConfiguration
    ClientDTO clientToClientDTO(Client client);

    Client clientDTOToClient(ClientDTO clientDTO);

    List<Client> convertClientDTOListToClientList(List<ClientDTO> list);

    List<ClientDTO> convertClientListToClientDTOList(List<Client> list);

}
