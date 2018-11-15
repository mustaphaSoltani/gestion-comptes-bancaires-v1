package com.axeane.service;

import com.axeane.GestionCompteBancaireApplication;
import com.axeane.domain.Client;
import com.axeane.domain.Compte;
import com.axeane.domain.dto.ClientDTO;
import com.axeane.domain.dto.CompteDTO;
import com.axeane.repository.ClientRepository;
import com.axeane.service.ClientService;
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
    public void saveClientTest() throws Exception {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setClientName("mSai");
        clientDTO.setClientPrenom("aloui");
        clientDTO.setClientCin("12345678");
        clientService.createClient(clientDTO);
        Client clientResult = clientRepository.findAll().get(clientRepository.findAll().size() - 1);
        assertThat(clientResult.getCin(), is("12345678"));
    }

    @Test
    public void getClientByIdTest() throws Exception {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setClientName("Sami");
        clientDTO.setClientPrenom("aloui");
        clientDTO.setClientCin("12345678");
        clientDTO.setClientEmail("bilel@gmail.com");
        clientDTO.setClientCin("12345678");
        clientDTO.setClientAdresse("Bardo");

        clientService.createClient(clientDTO);
        List<ClientDTO> listClientAfterSave = clientService.findAllClient();
        ClientDTO clientSaved = clientService.getClientById(listClientAfterSave.get(listClientAfterSave.size() - 1).getClientId());
        assertThat(clientSaved.getClientName(), is("Sami"));
    }

    @Test
    public void getClientByNomTest() throws Exception {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setClientName("Ahmed");
        clientDTO.setClientPrenom("Kefi");
        clientDTO.setClientCin("12345678");
        clientDTO.setClientEmail("bilel@gmail.com");
        clientDTO.setClientCin("12345678");
        clientDTO.setClientAdresse("Bardo");

        clientService.createClient(clientDTO);
        List<ClientDTO> listClientAfterSave = clientService.findAllClient();
        List<ClientDTO> clientSaved = clientService.getClientByNom(listClientAfterSave.get(listClientAfterSave.size() - 1).getClientName());
        assertThat(clientSaved.get(listClientAfterSave.size() - 1).getClientName(), is("Ahmed"));
    }

    @Test
    public void getClientBynCinTest() throws Exception {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setClientName("Bilel");
        clientDTO.setClientPrenom("Kefi");
        clientDTO.setClientCin("12345678");
        clientDTO.setClientEmail("bilel@gmail.com");
        clientDTO.setClientCin("12345678");
        clientDTO.setClientAdresse("Bardo");

        clientService.createClient(clientDTO);
        List<ClientDTO> listClientAfterSave = clientService.findAllClient();
        ClientDTO clientSaved = clientService.getClientBynCin(listClientAfterSave.get(listClientAfterSave.size() - 1).getClientCin());
        assertThat(clientSaved.getClientName(), is("Bilel"));
    }

    @Test
    public void getClientBynNumCompteTest() throws Exception {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setClientName("Bilel");
        clientDTO.setClientPrenom("Kefi");
        clientDTO.setClientCin("12345678");
        clientDTO.setClientEmail("bilel@gmail.com");
        clientDTO.setClientCin("12345678");
        clientDTO.setClientAdresse("Bardo");
        CompteDTO comtpe = new CompteDTO();
        comtpe.setCompteNumCompte(321);
        Set<CompteDTO> comptes = new HashSet<>();
        comptes.add(comtpe);
        clientDTO.setComptes(comptes);
        clientService.createClient(clientDTO);
        List<ClientDTO> listClientAfterSave = clientService.findAllClient();
        ClientDTO clientSaved = clientService.getClientBynNumCompte(listClientAfterSave.get(listClientAfterSave.size() - 1).getComptes().iterator().next().getCompteNumCompte());
        assertThat(clientSaved.getClientName(), is("Bilel"));
        assertThat(clientSaved.getComptes().iterator().next().getCompteNumCompte(), is(321));
    }

    @Test
    public void findAllTest() throws Exception {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setClientName("Soltani");
        clientDTO.setClientAdresse("Bardo");
        clientDTO.setClientCin("12345678");
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
    public void deleteTest() throws Exception {
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
    public void checkCinIsLengthThan8() throws Exception {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        ClientDTO client1 = new ClientDTO();
        client1.setClientCin("1234567");
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
    public void checkNomIsLengthMax50() throws Exception {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        ClientDTO client1 = new ClientDTO();
        client1.setClientCin("12345678");
        client1.setClientName(StringUtils.repeat("a", 66));
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
    public void checkPrenomIsLengthMax50() throws Exception {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        ClientDTO client1 = new ClientDTO();
        client1.setClientCin("12345678");
        client1.setClientPrenom(StringUtils.repeat("a", 66));
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
    public void checkNumTelIsLengthThan8() throws Exception {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        ClientDTO client1 = new ClientDTO();
        client1.setClientCin("12345678");
        client1.setClientNumTel("1234567");
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
    public void checkEmailIsLengthMax60() throws Exception {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        ClientDTO client1 = new ClientDTO();
        client1.setClientCin("12345678");
        client1.setClientEmail(StringUtils.repeat("a", 66));
        List<Client> listClientAfterSave = new ArrayList<>();
        boolean throwException = false;
        try {
            clientService.createClient(client1);
            listClientAfterSave = clientRepository.findAll();
        } catch (Exception e) {
            throwException = true;
            ;
        }
        assertThat(listClientAfterSave.size(), is(sizeListClientBeforeSave));
        assertThat(throwException, is(true));
    }

    @Test
    public void checkEmailIsLengthMin10() throws Exception {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        ClientDTO client1 = new ClientDTO();
        client1.setClientCin("12345678");
        client1.setClientEmail(StringUtils.repeat("a", 8));
        List<Client> listClientAfterSave = new ArrayList<>();
        boolean throwException = false;
        try {
            clientService.createClient(client1);
            listClientAfterSave = clientRepository.findAll();
        } catch (Exception e) {
            throwException = true;
            ;
        }
        assertThat(listClientAfterSave.size(), is(sizeListClientBeforeSave));
        assertThat(throwException, is(true));
    }
}