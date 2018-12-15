package com.axeane.service;

import com.axeane.domain.Client;
import com.axeane.domain.Compte;
import com.axeane.domain.dto.ClientDTO;
import com.axeane.domain.mapper.ClientMapper;
import com.axeane.repository.ClientRepository;
import com.axeane.repository.CompteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClientService {
    private final Logger log = LoggerFactory.getLogger(ClientService.class);
    private final ClientMapper mapperClient;
    private final ClientRepository clientRepository;
    private final CompteRepository compteRepository;

    public ClientService(ClientMapper mapperClient, ClientRepository clientRepository, CompteRepository compteRepository) {
        this.mapperClient = mapperClient;
        this.clientRepository = clientRepository;
        this.compteRepository = compteRepository;
    }

    public ClientDTO createClient(ClientDTO clientDTO) {
        log.debug("Request to save Client : {}", clientDTO.toString());
        Client client = mapperClient.clientDTOToClient(clientDTO);
        client.getComptes().forEach(compte -> compte.setClient(client));
        return mapperClient.clientToClientDTO(clientRepository.save(client));
    }

    @Transactional(readOnly = true)
    public ClientDTO getClientById(Long id) {
        log.debug("Request to get Client by id: {}", id);
        Client client = clientRepository.findClientById(id);
        return mapperClient.clientToClientDTO(client);
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> getClientByNom(String nom) {
        log.debug("Request to get Client by nom : {}", nom);
        List<Client> clients = clientRepository.findAllByNom(nom);
        return mapperClient.convertClientListToClientDTOList(clients);
    }

    @Transactional(readOnly = true)
    public ClientDTO getClientBynCin(String cin) {
        log.debug("Request to get Client by cin: {}", cin);
        Client client = clientRepository.findClientByCin(cin);
        return mapperClient.clientToClientDTO(client);
    }

    @Transactional(readOnly = true)
    public ClientDTO getClientBynNumCompte(Long numCompte) {
        log.debug("Request to get Client by numCompte : {}", numCompte);
        Compte compte = compteRepository.findByNumCompte(numCompte);
        Client client = compte.getClient();
        return mapperClient.clientToClientDTO(client);
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> findAllClient() {
        log.debug("Request to get all Client");
        List<Client> clients = clientRepository.findAll();
        return mapperClient.convertClientListToClientDTOList(clients);
    }

    public void deleteClient(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.deleteById(id);
    }
}
