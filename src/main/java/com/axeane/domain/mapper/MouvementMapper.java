package com.axeane.domain.mapper;

import com.axeane.domain.Mouvement;
import com.axeane.domain.dto.MouvementDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper
public interface MouvementMapper {

    @Mappings({@Mapping(target = "mouvementId", source = "entity.id"),
            @Mapping(target = "mouvementTypeMouvement", source = "entity.typeMouvement"),
            @Mapping(target = "mouvementSomme", source = "entity.somme"),
            @Mapping(target = "mouvementDate", source = "entity.date"),
            @Mapping(target = "mouvementCompte", source = "entity.compte"),
            @Mapping(target = "mouvementCompteId", source = "entity.compteId")})
    MouvementDTO mouvementToMouvementDTO(Mouvement entity);

    @Mappings({@Mapping(target = "id", source = "mouvementId"),
            @Mapping(target = "typeMouvement", source = "mouvementTypeMouvement"),
            @Mapping(target = "somme", source = "mouvementSomme"),
            @Mapping(target = "date", source = "mouvementDate"),
            @Mapping(target = "compte", source = "mouvementCompte"),
            @Mapping(target = "compteId", source = "mouvementCompteId")})
    Mouvement mouvementDTOToMouvement(MouvementDTO dto);

    List<Mouvement> convertMouvementDTOListToMouvementList(List<MouvementDTO> list);

    List<MouvementDTO> convertMouvementListToMouvementDTOList(List<Mouvement> list);

}
