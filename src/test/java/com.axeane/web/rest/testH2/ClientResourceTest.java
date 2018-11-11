package com.axeane.web.rest.testH2;

import com.axeane.GestionCompteBancaireApplication;
import com.axeane.domain.Client;
import com.axeane.repository.ClientRepository;
import com.axeane.service.ClientService;
import com.axeane.service.business.ExtraitCompteBancaireService;
import com.axeane.service.business.MailExtraitService;
import com.axeane.web.errors.ExceptionTranslator;
import com.axeane.web.rest.ClientResource;
import com.axeane.web.rest.config.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application.properties")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestionCompteBancaireApplication.class)
@DataJpaTest
@ComponentScan({"com.axeane.domain.util","com.axeane.service"})
public class ClientResourceTest {

    private static final String DEFAULT_NOM1 = "Soltani";
    private static final String UPDATED_NOM1 = "AAAA";

    private static final String DEFAULT_PRENOM1 = "Mustapha";
    private static final String UPDATED_PRENOM = "BBBB";

    private static final String DEFAULT_ADRESSE = "Bardo";
    private static final String UPDATED_ADRESSE = "TTTT";

    private static final String DEFAULT_EMAIL = "email1@gmail.com";
    private static final String UPDATED_EMAIL = "email2@gmail.com";

    private static final String DEFAULT_CIN = "78945612";
    private static final String UPDATED_CIN = "78949612";

    private static final String DEFAULT_NUM_TEL = "78545612";
    private static final String UPDATED_NUM_TEL = "78945612";

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
        client.setPrenom(DEFAULT_PRENOM1);
        client.setEmail(DEFAULT_EMAIL);
        client.setNumTel(DEFAULT_NUM_TEL);
        client.setAdresse(DEFAULT_ADRESSE);
        return client;
    }

    @Before
    public void initTest() {
        client = createEntity(em);
    }

    @Test
    public void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();
        restClientMockMvc.perform(post("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(client)))
                .andExpect(status().isCreated());
        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getNom()).isEqualTo(DEFAULT_NOM1);
        assertThat(testClient.getPrenom()).isEqualTo(DEFAULT_PRENOM1);
        assertThat(testClient.getCin()).isEqualTo(DEFAULT_CIN);
        assertThat(testClient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClient.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testClient.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);
    }

    @Test
    public void updateClient() throws Exception {
        // Initialize the database
        clientRepository.save(client);
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        Client updatedClient = clientRepository.getClientById(client.getId());
        updatedClient.setEmail(UPDATED_EMAIL);
        updatedClient.setNumTel(UPDATED_NUM_TEL);
        updatedClient.setNom(UPDATED_NOM1);
        updatedClient.setPrenom(UPDATED_PRENOM);
        updatedClient.setAdresse(UPDATED_ADRESSE);
        updatedClient.setCin(UPDATED_CIN);

        restClientMockMvc.perform(put("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedClient)))
                .andExpect(status().isOk());
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testClient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClient.getNom()).isEqualTo(UPDATED_NOM1);
        assertThat(testClient.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testClient.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testClient.getAdresse()).isEqualTo(UPDATED_ADRESSE);
    }

    @Test
    public void getAllClient() throws Exception {
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
                .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM1)))
                .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
                .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN)))
        ;
    }

    @Test
    public void getClientById() throws Exception {
        // Initialize the database
        Client clientSaved = clientRepository.saveAndFlush(client);
        // Get the clients
        restClientMockMvc.perform(get("/api/clients/{id}", clientSaved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(clientSaved.getId()))
                .andExpect(jsonPath("$.numTel").value(DEFAULT_NUM_TEL))
                .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
                .andExpect(jsonPath("$.nom").value(DEFAULT_NOM1))
                .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM1));
    }

    @Test
    public void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteClient() throws Exception {
        // Initialize the database
        clientRepository.save(client);
        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Get the client
        restClientMockMvc.perform(delete("/api/clients/{id}", client.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Client> clientstList = clientRepository.findAll();
        assertThat(clientstList).hasSize(databaseSizeBeforeDelete - 1);
    }
}