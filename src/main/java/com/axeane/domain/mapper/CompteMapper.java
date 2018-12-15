package com.axeane.domain.mapper;

import com.axeane.domain.Compte;
import com.axeane.domain.dto.CompteDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring", uses = {ClientMapper.class})
public interface CompteMapper {
    @InheritInverseConfiguration
    CompteDTO compteToCompteDTO(Compte compte);

    Compte compteDTOToCompte(CompteDTO compteDto);

    List<Compte> convertCompteDTOListToCompteList(List<CompteDTO> list);

    List<CompteDTO> convertCompteListToCompteDTOList(List<Compte> list);
}
