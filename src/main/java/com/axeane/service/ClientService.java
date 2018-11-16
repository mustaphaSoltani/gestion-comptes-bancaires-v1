package com.axeane.service;

import com.axeane.domain.Client;
import com.axeane.domain.Compte;
import com.axeane.domain.dto.ClientDTO;
import com.axeane.domain.dto.CompteDTO;
import com.axeane.domain.mapper.ClientMapper;
import com.axeane.domain.mapper.CompteMapper;
import com.axeane.repository.ClientRepository;
import com.axeane.repository.CompteRepository;
import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ClientService {
    private final Logger log = LoggerFactory.getLogger(ClientService.class);
    private ClientMapper mapperClient = Mappers.getMapper(ClientMapper.class);
    private CompteMapper mapperCompte = Mappers.getMapper(CompteMapper.class);

    private final ClientRepository clientRepository;
    private final CompteRepository compteRepository;

    public ClientService(ClientRepository clientRepository, CompteRepository compteRepository) {
        this.clientRepository = clientRepository;
        this.compteRepository = compteRepository;
    }

    public Client createClient(ClientDTO clientDTO) {
        log.debug("Request to save Client : {}", clientDTO.toString());

        clientDTO.getClientComptes().forEach(compte -> {
            compte.setCompteClient(clientDTO);
        });
        Set<CompteDTO> compteDTO = clientDTO.getClientComptes();
        Set<Compte> comptes = mapperCompte.convertCompteDTOSetToCompteSet(compteDTO);

        Client client = mapperClient.clientDTOToClient(clientDTO);
        client.setComptes(comptes);
        client.getComptes().forEach(compte -> {
            compte.setClient(client);
        });
        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public ClientDTO getClientById(Long id) {
        log.debug("Request to get Client by id: {}", id);
        Client client = clientRepository.getClientById(id);
        Set<Compte> comptes=client.getComptes();
        Set<CompteDTO>compteDTO=mapperCompte.convertCompteSetToCompteDTOSet(comptes);
        ClientDTO clientDTO=mapperClient.clientToClientDTO(client);
        clientDTO.setClientComptes(compteDTO);
        return clientDTO;
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> getClientByNom(String nom) {
        log.debug("Request to get Client by nom : {}", nom);
        List<Client> clients = clientRepository.getAllByNom(nom);
        Set<Compte> comptes=clients.iterator().next().getComptes();
        Set<CompteDTO>compteDTO=mapperCompte.convertCompteSetToCompteDTOSet(comptes);
        List<ClientDTO>clientss=mapperClient.convertClientListToClientDTOList(clients);
        clientss.iterator().next().setClientComptes(compteDTO);
        return clientss;
    }

    @Transactional(readOnly = true)
    public ClientDTO getClientBynCin(String cin) {
        log.debug("Request to get Client by cin: {}", cin);
        Client client = clientRepository.getClientByCin(cin);
        Set<Compte> comptes=client.getComptes();
        Set<CompteDTO>compteDTO=mapperCompte.convertCompteSetToCompteDTOSet(comptes);
        ClientDTO clientDTO=mapperClient.clientToClientDTO(client);
        clientDTO.setClientComptes(compteDTO);
        return clientDTO;
    }

    @Transactional(readOnly = true)
    public ClientDTO getClientBynNumCompte(Integer numCompte) {
        log.debug("Request to get Client by numCompte : {}", numCompte);

        Compte compte = compteRepository.findByNumCompte(numCompte);
        Client client = compte.getClient();
        Set<Compte> comptes=client.getComptes();
        Set<CompteDTO>compteDTO=mapperCompte.convertCompteSetToCompteDTOSet(comptes);
        ClientDTO clientDTO=mapperClient.clientToClientDTO(client);
        clientDTO.setClientComptes(compteDTO);
        return clientDTO;
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> findAllClient() {
        log.debug("Request to get all Client");
        List<Client> clients = clientRepository.findAll();
        Set<Compte> comptes=clients.iterator().next().getComptes();
        Set<CompteDTO>compteDTO=mapperCompte.convertCompteSetToCompteDTOSet(comptes);
        List<ClientDTO>clientss=mapperClient.convertClientListToClientDTOList(clients);
        clientss.iterator().next().setClientComptes(compteDTO);
        return clientss;
    }

    public void deleteClient(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.deleteById(id);
    }
}
