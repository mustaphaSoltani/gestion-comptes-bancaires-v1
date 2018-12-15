package com.axeane.service;

import com.axeane.GestionCompteBancaireApplication;
import com.axeane.domain.Client;
import com.axeane.domain.dto.ClientDTO;
import com.axeane.domain.dto.CompteDTO;
import com.axeane.repository.ClientRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestionCompteBancaireApplication.class)
@DataJpaTest
@ComponentScan("com.axeane")
@TestPropertySource("/application.properties")
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    public void saveClientTest() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("mSai");
        clientDTO.setPrenom("aloui");
        clientDTO.setCin("12345678");
        clientService.createClient(clientDTO);
        Client clientResult = clientRepository.findAll().get(clientRepository.findAll().size() - 1);
        assertThat(clientResult.getCin(), is("12345678"));
    }

    @Test
    public void getClientByIdTest(){
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("Sami");
        clientDTO.setPrenom("aloui");
        clientDTO.setCin("12345678");
        clientDTO.setEmail("bilel@gmail.com");
        clientDTO.setCin("12345678");
        clientDTO.setAdresse("Bardo");

        clientService.createClient(clientDTO);
        List<ClientDTO> listClientAfterSave = clientService.findAllClient();
        ClientDTO clientSaved = clientService.getClientById(listClientAfterSave.get(listClientAfterSave.size() - 1).getId());
        assertThat(clientSaved.getName(), is("Sami"));
    }

    @Test
    public void getClientByNomTest() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("Ahmed");
        clientDTO.setPrenom("Kefi");
        clientDTO.setCin("12345678");
        clientDTO.setEmail("bilel@gmail.com");
        clientDTO.setCin("12345678");
        clientDTO.setAdresse("Bardo");

        clientService.createClient(clientDTO);
        List<ClientDTO> listClientAfterSave = clientService.findAllClient();
        List<ClientDTO> clientSaved = clientService.getClientByNom(listClientAfterSave.get(listClientAfterSave.size() - 1).getName());
        assertThat(clientSaved.get(listClientAfterSave.size() - 1).getName(), is("Ahmed"));
    }

    @Test
    public void getClientBynCinTest() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("Bilel");
        clientDTO.setPrenom("Kefi");
        clientDTO.setAdresse("12345678");
        clientDTO.setEmail("bilel@gmail.com");
        clientDTO.setCin("12345678");
        clientDTO.setAdresse("Bardo");

        clientService.createClient(clientDTO);
        List<ClientDTO> listClientAfterSave = clientService.findAllClient();
        ClientDTO clientSaved = clientService.getClientBynCin(listClientAfterSave.get(listClientAfterSave.size() - 1).getCin());
        assertThat(clientSaved.getName(), is("Bilel"));
    }

    @Test
    public void getClientBynNumCompteTest(){
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("Bilel");
        clientDTO.setPrenom("Kefi");
        clientDTO.setAdresse("12345678");
        clientDTO.setEmail("bilel@gmail.com");
        clientDTO.setCin("12345678");
        clientDTO.setAdresse("Bardo");
        CompteDTO comtpe = new CompteDTO();
        comtpe.setNumCompte(321);
        Set<CompteDTO> comptes = new HashSet<>();
        comptes.add(comtpe);
        clientDTO.setComptes(comptes);
        clientService.createClient(clientDTO);
        List<ClientDTO> listClientAfterSave = clientService.findAllClient();
       // ClientDTO clientSaved = clientService.getClientBynNumCompte(listClientAfterSave.get(listClientAfterSave.size() - 1).getComptes().iterator().next().getCompteNumCompte());
       // assertThat(clientSaved.getClientName(), is("Bilel"));
       // assertThat(clientSaved.getComptes().iterator().next().getCompteNumCompte(), is(321));
    }

    @Test
    public void findAllTest() {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("Soltani");
        clientDTO.setAdresse("Bardo");
        clientDTO.setCin("12345678");
        List<ClientDTO> listClientAfterSave = new ArrayList<>();
        boolean throwException = false;
        try {
            clientService.createClient(clientDTO);
            listClientAfterSave = clientService.findAllClient();
        } catch (Exception e) {
            throwException = true;
        }
        assertThat(listClientAfterSave.size(), is(sizeListClientBeforeSave + 1));
        assertThat(throwException, is(false));
    }

    @Test
    public void deleteTest() {
        int sizeListClientBeforeDelete = clientRepository.findAll().size();
        Client client = new Client();
        client.setCin("12345678");
        clientRepository.save(client);
        Client client1 = clientRepository.findAll().get(clientRepository.findAll().size() - 1);
        clientService.deleteClient(client1.getId());
        int sizeListClientAfterDelete = clientRepository.findAll().size();
        assertThat(sizeListClientBeforeDelete, is(sizeListClientAfterDelete));
    }

    @Test
    public void checkCinIsLengthThan8() {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        ClientDTO client1 = new ClientDTO();
        client1.setCin("1234567");
        List<Client> listClientAfterSave = new ArrayList<>();
        boolean throwException = false;
        try {
            clientService.createClient(client1);
            listClientAfterSave = clientRepository.findAll();
        } catch (Exception e) {
            throwException = true;
        }
        assertThat(listClientAfterSave.size(), is(sizeListClientBeforeSave));
        assertThat(throwException, is(true));
    }

    @Test
    public void checkNomIsLengthMax50(){
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        ClientDTO client1 = new ClientDTO();
        client1.setCin("12345678");
        client1.setName(StringUtils.repeat("a", 66));
        List<Client> listClientAfterSave = new ArrayList<>();
        boolean throwException = false;
        try {
            clientService.createClient(client1);
            listClientAfterSave = clientRepository.findAll();
        } catch (Exception e) {
            throwException = true;
        }
        assertThat(listClientAfterSave.size(), is(sizeListClientBeforeSave));
        assertThat(throwException, is(true));
    }

    @Test
    public void checkPrenomIsLengthMax50() {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        ClientDTO client1 = new ClientDTO();
        client1.setCin("12345678");
        client1.setPrenom(StringUtils.repeat("a", 66));
        List<Client> listClientAfterSave = new ArrayList<>();
        boolean throwException = false;
        try {
            clientService.createClient(client1);
            listClientAfterSave = clientRepository.findAll();
        } catch (Exception e) {
            throwException = true;
        }
        assertThat(listClientAfterSave.size(), is(sizeListClientBeforeSave));
        assertThat(throwException, is(true));
    }

    @Test
    public void checkNumTelIsLengthThan8() {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        ClientDTO client1 = new ClientDTO();
        client1.setCin("12345678");
        client1.setNumTel("1234567");
        List<Client> listClientAfterSave = new ArrayList<>();
        boolean throwException = false;
        try {
            clientService.createClient(client1);
            listClientAfterSave = clientRepository.findAll();
        } catch (Exception e) {
            throwException = true;
        }
        assertThat(listClientAfterSave.size(), is(sizeListClientBeforeSave));
        assertThat(throwException, is(true));
    }

    @Test
    public void checkEmailIsLengthMax60(){
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        ClientDTO client1 = new ClientDTO();
        client1.setCin("12345678");
        client1.setEmail(StringUtils.repeat("a", 66));
        List<Client> listClientAfterSave = new ArrayList<>();
        boolean throwException = false;
        try {
            clientService.createClient(client1);
            listClientAfterSave = clientRepository.findAll();
        } catch (Exception e) {
            throwException = true;
        }
        assertThat(listClientAfterSave.size(), is(sizeListClientBeforeSave));
        assertThat(throwException, is(true));
    }

    @Test
    public void checkEmailIsLengthMin10() {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        ClientDTO client1 = new ClientDTO();
        client1.setCin("12345678");
        client1.setEmail(StringUtils.repeat("a", 8));
        List<Client> listClientAfterSave = new ArrayList<>();
        boolean throwException = false;
        try {
            clientService.createClient(client1);
            listClientAfterSave = clientRepository.findAll();
        } catch (Exception e) {
            throwException = true;
        }
        assertThat(listClientAfterSave.size(), is(sizeListClientBeforeSave));
        assertThat(throwException, is(true));
    }
}