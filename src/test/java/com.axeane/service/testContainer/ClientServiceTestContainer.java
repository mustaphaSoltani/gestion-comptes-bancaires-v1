package com.axeane.service.testContainer;

import com.axeane.GestionCompteBancaireApplication;
import com.axeane.domain.Client;
import com.axeane.domain.Compte;
import com.axeane.repository.ClientRepository;
import com.axeane.repository.CompteRepository;
import com.axeane.service.ClientService;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = {ClientServiceTestContainer.Initializer.class})
@SpringBootTest(classes = GestionCompteBancaireApplication.class)
@TestPropertySource("/application-test-container.properties")
@ComponentScan({"com.axeane.domain.util","com.axeane.service"})
public class ClientServiceTestContainer {
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientRepository clientRepository;
@Autowired
private CompteRepository compteRepository;
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer =
            (PostgreSQLContainer) new PostgreSQLContainer("postgres:9.6.10")
                    .withDatabaseName("spring")
                    .withUsername("postgres")
                    .withPassword("admin")
                    .withStartupTimeout(Duration.ofSeconds(10));

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Test
    public void saveClientTest() throws Exception {
        Client client = new Client();
        client.setNom("Sami");
        client.setAdresse("aloui");
        client.setCin("12345609");
        clientService.createClient(client);
        Client clientResult = clientRepository.findAll().get(clientRepository.findAll().size() - 1);
        assertThat(clientResult.getNom(), is("Sami"));
    }

    @Test
    public void getClientByIdTest() throws Exception {
        Client client = new Client();
        client.setNom("Bilel");
        client.setCin("12345602");
        client.setPrenom("Omrani");
        client.setEmail("bilel@gmail.com");
        client.setNumTel("12345678");
        client.setAdresse("Bardo");

        clientService.createClient(client);
        List<Client> listClientAfterSave = clientService.findAllClient();
        Client clientSaved = clientService.getClientById(listClientAfterSave.get(listClientAfterSave.size() - 1).getId());
        assertThat(clientSaved.getNom(), is("Bilel"));
    }

    @Test
    public void getClientByNomTest() throws Exception {
        Client client = new Client();
        client.setNom("Bilell");
        client.setCin("12345608");
        client.setPrenom("Omrani");
        client.setEmail("bilel@gmail.com");
        client.setNumTel("12345678");
        client.setAdresse("Bardo");

        clientService.createClient(client);
        List<Client> listClientAfterSave = clientService.findAllClient();
        List<Client> clientSaved = clientService.getClientByNom(listClientAfterSave.get(listClientAfterSave.size() - 1).getNom());
        assertThat(clientSaved.get(listClientAfterSave.size() - 1).getNom(), is("Bilell"));
    }

    @Test
    public void getClientBynCinTest() throws Exception {
        Client client = new Client();
        client.setNom("Bilel");
        client.setCin("12345608");
        client.setPrenom("Omrani");
        client.setEmail("bilel@gmail.com");
        client.setNumTel("12345678");
        client.setAdresse("Bardo");

        clientService.createClient(client);
        List<Client> listClientAfterSave = clientService.findAllClient();
        Client clientSaved = clientService.getClientBynCin(listClientAfterSave.get(listClientAfterSave.size() - 1).getCin());
        assertThat(clientSaved.getNom(), is("Bilel"));
    }

    @Test
    public void getClientBynNumCompteTest() throws Exception {
        Client client = new Client();
        client.setNom("Bilel");
        client.setCin("12345608");
        client.setPrenom("Omrani");
        client.setEmail("bilel@gmail.com");
        client.setNumTel("12345678");
        client.setAdresse("Bardo");
        Compte comtpe = new Compte();
        comtpe.setNumCompte(321);
        Set<Compte> comptes = new HashSet<>();
        comptes.add(comtpe);
        client.setComptes(comptes);
        clientService.createClient(client);
        List<Client> listClientAfterSave = clientService.findAllClient();
        Client clientSaved = clientService.getClientBynNumCompte(listClientAfterSave.get(listClientAfterSave.size() - 1).getComptes().iterator().next().getNumCompte());
        assertThat(clientSaved.getNom(), is("Bilel"));
        assertThat(clientSaved.getComptes().iterator().next().getNumCompte(), is(321));
    }

    @Test
    public void findAllTest() throws Exception {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        Client client = new Client();
        client.setNom("Soltani");
        client.setAdresse("Bardo");
        client.setCin("12345608");
        List<Client> listClientAfterSave = new ArrayList<>();
        boolean throwException = false;
        try {
            clientRepository.save(client);
            listClientAfterSave = clientService.findAllClient();
        } catch (Exception e) {
            throwException = true;
        }
        assertThat(listClientAfterSave.size(), is(sizeListClientBeforeSave + 1));
        assertThat(throwException, is(false));
    }

    @Test
    public void deleteTest() throws Exception {
        int sizeListClientBeforeDelete = clientService.findAllClient().size();
        Client client = new Client();
        client.setCin("12345608");
        clientRepository.save(client);
        Client client1 = clientRepository.findAll().get(clientRepository.findAll().size() - 1);
        clientService.deleteClient(client1.getId());
        int sizeListClientAfterDelete = clientService.findAllClient().size();
        assertThat(sizeListClientBeforeDelete, is(sizeListClientAfterDelete));
    }
    @Test
    public void checkCinIsLengthThan8() throws Exception {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        Client client1 = new Client();
        client1.setCin("1234467");
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
        Client client1 = new Client();
        client1.setCin("12345608");
        client1.setNom(StringUtils.repeat("a",66));
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
        Client client1 = new Client();
        client1.setCin("12355678");
        client1.setPrenom(StringUtils.repeat("a",66));
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
        Client client1 = new Client();
        client1.setCin("12348678");
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
    public void checkEmailIsLengthMax60() throws Exception {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        Client client1 = new Client();
        client1.setCin("12347678");
        client1.setEmail(StringUtils.repeat("a",66));
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
    public void checkEmailIsLengthMin10() throws Exception {
        int sizeListClientBeforeSave = clientRepository.findAll().size();
        Client client1 = new Client();
        client1.setCin("14785222");
        client1.setEmail(StringUtils.repeat("a",8));
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
