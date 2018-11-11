package com.axeane.web.rest.testContainer;

import com.axeane.GestionCompteBancaireApplication;
import com.axeane.domain.Client;
import com.axeane.repository.ClientRepository;
import com.axeane.service.business.ExtraitCompteBancaireService;
import com.axeane.service.business.MailExtraitService;
import com.axeane.service.ClientService;
import com.axeane.web.errors.ExceptionTranslator;
import com.axeane.web.rest.ClientResource;
import com.axeane.web.rest.config.TestUtil;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.junit.runner.RunWith;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.persistence.EntityManager;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = {ClientResourceTestContainer.Initializer.class})
@SpringBootTest(classes = GestionCompteBancaireApplication.class)
@TestPropertySource("/application-test-container.properties")
@ComponentScan({"com.axeane.domain.util","com.axeane.service"})
public class ClientResourceTestContainer {

    private static final String DEFAULT_NOM1 = "Soltaniii";
    private static final String UPDATED_NOM = "AAAAA";

    private static final String DEFAULT_PRENOM = "Mustaphaaa";
    private static final String UPDATED_PRENOM = "CCCC";

    private static final String DEFAULT_ADRESS = "Bardo";
    private static final String UPDATED_ADRESS = "TTTT";

    private static final String DEFAULT_EMAIL = "email14@gmail.com";
    private static final String UPDATED_EMAIL = "email23@gmail.com";

    private static final String DEFAULT_CIN = "12345678";
    private static final String UPDATED_CIN = "12345678";

    private static final String DEFAULT_NUM_TEL = "12345678";
    private static final String UPDATED_NUM_TEL = "12345678";

    @Autowired
    private ClientService clientService;

    @Autowired
    private ExtraitCompteBancaireService extraitCompteBancaireService;

    @Autowired
    private MailExtraitService mailExtraitService;

    @Autowired
    private ClientRepository clientRepository;

    private MappingJackson2HttpMessageConverter jacksonMessageConverter=new MappingJackson2HttpMessageConverter();

    private PageableHandlerMethodArgumentResolver pageableArgumentResolver= new PageableHandlerMethodArgumentResolver();

    private ExceptionTranslator exceptionTranslator=new ExceptionTranslator();

    @Autowired
    private EntityManager em;

    private MockMvc restClientMockMvc;

    private Client client;

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

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ClientResource clientResource = new ClientResource(clientService, extraitCompteBancaireService, mailExtraitService);
        this.restClientMockMvc = MockMvcBuilders.standaloneSetup(clientResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    public Client createEntity(EntityManager em) {
        Client client = new Client();
        client.setNom(DEFAULT_NOM1);
        client.setCin(DEFAULT_CIN);
        client.setPrenom(DEFAULT_PRENOM);
        client.setNumTel(DEFAULT_NUM_TEL);
        client.setEmail(DEFAULT_EMAIL);
        client.setAdresse(DEFAULT_ADRESS);
        return client;
    }

    @Before
    public void initTest() {
        client = createEntity(em);
    }

    @Test
    @Transactional
    public void createClientTest() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();
        restClientMockMvc.perform(post("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(client)))
                .andExpect(status().isCreated());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getNom()).isEqualTo(DEFAULT_NOM1);
        assertThat(testClient.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testClient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClient.getCin()).isEqualTo(DEFAULT_CIN);
        assertThat(testClient.getAdresse()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testClient.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);
    }

    @Test
    @Transactional
    public void updateClientTest() throws Exception {
        clientRepository.save(client);
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        Client updatedClient = clientRepository.getClientById(client.getId());
        updatedClient.setEmail(UPDATED_EMAIL);
        updatedClient.setNumTel(UPDATED_NUM_TEL);
        updatedClient.setNom(UPDATED_NOM);
        updatedClient.setAdresse(UPDATED_ADRESS);
        updatedClient.setPrenom(UPDATED_PRENOM);
        updatedClient.setCin(UPDATED_CIN);

        restClientMockMvc.perform(put("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClient)))
                .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testClient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClient.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testClient.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testClient.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testClient.getAdresse()).isEqualTo(UPDATED_ADRESS);
    }

    @Test
    public void getAllClientTest() throws Exception {
        // Initialize the database
        Client clientSaved = clientRepository.saveAndFlush(client);
        // Get all the clientList
        restClientMockMvc.perform(get("/api/clients?sort=id,desc", clientSaved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(clientSaved.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM1)))
                .andExpect(jsonPath("$.[*].numTel").value(hasItem(DEFAULT_NUM_TEL)))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
                .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
                .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESS)))
                .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN)))
        ;
    }

    @Test
    public void getClientByIdTest() throws Exception {
        // Initialize the database
        Client clientSaved = clientRepository.saveAndFlush(client);
        // Get the clients
        restClientMockMvc.perform(get("/api/clients/{id}", clientSaved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(clientSaved.getId()))
                .andExpect(jsonPath("$.numTel").value(DEFAULT_NUM_TEL))
                .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESS))
                .andExpect(jsonPath("$.nom").value(DEFAULT_NOM1))
                .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM));
    }

    @Test
    public void getNonExistingClientTest() throws Exception {
        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteClientTest() throws Exception {
        clientRepository.save(client);
        int databaseSizeBeforeDelet = clientRepository.findAll().size();

        restClientMockMvc.perform(delete("/api/clients/{id}", client.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        List<Client> clientstList = clientRepository.findAll();
        assertThat(clientstList).hasSize(databaseSizeBeforeDelet - 1);
    }
}
