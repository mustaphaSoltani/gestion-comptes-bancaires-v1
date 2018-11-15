package com.axeane.domain.mapper;

import com.axeane.domain.Compte;
import com.axeane.domain.dto.CompteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Mapper
public interface CompteMapper {

    @Mappings({@Mapping(target = "compteId", source = "id"), @Mapping(target = "compteNumCompte", source = "entity.numCompte"), @Mapping(target = "soldeDTO", source = "entity.solde"), @Mapping(target = "compteClientId", source = "entity.clientId")})
    CompteDTO compteToCompteDTO(Compte entity);
    CompteDTO compteeToCompteDTO(Optional<Compte> entity);
    @Mappings({@Mapping(target = "id", source = "compteId"), @Mapping(target = "numCompte", source = "compteNumCompte"), @Mapping(target = "solde", source = "soldeDTO"), @Mapping(target = "clientId", source = "compteClientId")})
    Compte compteDTOToCompte(CompteDTO dto);

    List<Compte> convertCompteDTOListToCompteList(List<CompteDTO> list);
    List<CompteDTO> convertCompteListToCompteDTOList(List<Compte> list);

    Set<Compte> convertCompteDTOSetToCompteSet(Set<CompteDTO> list);
    Set<CompteDTO> convertCompteSetToCompteDTOSet(Set<Compte> list);


}
