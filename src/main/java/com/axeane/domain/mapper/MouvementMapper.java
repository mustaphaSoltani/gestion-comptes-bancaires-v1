package com.axeane.domain.mapper;

import com.axeane.domain.Mouvement;
import com.axeane.domain.dto.MouvementDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {CompteMapper.class})

public interface MouvementMapper {
    @InheritInverseConfiguration
    Mouvement mouvementDTOToMouvement(MouvementDTO mouvementDTO);
    MouvementDTO mouvementToMouvementDTO(Mouvement mouvement);

    List<Mouvement> convertMouvementDTOListToMouvementList(List<MouvementDTO> list);

    List<MouvementDTO> convertMouvementListToMouvementDTOList(List<Mouvement> list);
}
