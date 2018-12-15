package com.axeane.web.rest;

import com.axeane.GestionCompteBancaireApplication;
import com.axeane.domain.Client;
import com.axeane.domain.dto.ClientDTO;
import com.axeane.domain.mapper.ClientMapper;
import com.axeane.repository.ClientRepository;
import com.axeane.service.ClientService;
import com.axeane.service.business.ExtraitCompteBancaireService;
import com.axeane.service.business.MailExtraitService;
import com.axeane.web.errors.ExceptionTranslator;
import com.axeane.web.rest.config.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application.properties")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestionCompteBancaireApplication.class)
@DataJpaTest
@ComponentScan({"com.axeane.domain.util", "com.axeane.service"})
public class ClientResourceTest {
    private ClientMapper mapper = Mappers.getMapper(ClientMapper.class);
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

    private MappingJackson2HttpMessageConverter jacksonMessageConverter = new MappingJackson2HttpMessageConverter();

    private PageableHandlerMethodArgumentResolver pageableArgumentResolver = new PageableHandlerMethodArgumentResolver();

    private ExceptionTranslator exceptionTranslator = new ExceptionTranslator();

    @Autowired
    private EntityManager em;

    private MockMvc restClientMockMvc;

    private ClientDTO clientDTO;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ClientResource clientResource = new ClientResource(clientService, extraitCompteBancaireService, mailExtraitService);
        this.restClientMockMvc = MockMvcBuilders.standaloneSetup(clientResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    public ClientDTO createEntity(EntityManager em) {
        ClientDTO client = new ClientDTO();
        client.setName(DEFAULT_NOM1);
        client.setCin(DEFAULT_CIN);
        client.setPrenom(DEFAULT_PRENOM1);
        client.setEmail(DEFAULT_EMAIL);
        client.setNumTel(DEFAULT_NUM_TEL);
        client.setAdresse(DEFAULT_ADRESSE);
        return client;
    }

    @Before
    public void initTest() {
        clientDTO = createEntity(em);
    }

    @Test
    public void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();
        restClientMockMvc.perform(post("/api/clients")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
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
        /*Client client=clientService.createClient(clientDTO);
        int databaseSizeBeforeUpdate = clientService.findAllClient().size();

        ClientDTO updatedClient = clientService.getClientById(client.getId());
        updatedClient.setClientEmail(UPDATED_EMAIL);
        updatedClient.setClientNumTel(UPDATED_NUM_TEL);
        updatedClient.setClientName(UPDATED_NOM1);
        updatedClient.setClientPrenom(UPDATED_PRENOM);
        updatedClient.setClientAdresse(UPDATED_ADRESSE);
        updatedClient.setClientCin(UPDATED_CIN);

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
        assertThat(testClient.getAdresse()).isEqualTo(UPDATED_ADRESSE);*/
    }

    @Test
    public void getAllClient() throws Exception {
        // Initialize the database
        //Client client=mapper.clientDTOToClient(clientDTO);
        ;
       /* Client clientSaved = clientRepository.saveAndFlush(clientService.createClient(clientDTO));
        // Get all the clientList
        restClientMockMvc.perform(get("/api/clients", clientSaved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
               .andExpect(jsonPath("$.[*].clientId").value(hasItem(clientSaved.getId().intValue())))
                .andExpect(jsonPath("$.[*].clientName").value(hasItem(DEFAULT_NOM1)))
                .andExpect(jsonPath("$.[*].clientNumTel").value(hasItem(DEFAULT_NUM_TEL)))
                .andExpect(jsonPath("$.[*].clientEmail").value(hasItem(DEFAULT_EMAIL)))
                .andExpect(jsonPath("$.[*].clientPrenom").value(hasItem(DEFAULT_PRENOM1)))
                .andExpect(jsonPath("$.[*].clientAdresse").value(hasItem(DEFAULT_ADRESSE)))
                .andExpect(jsonPath("$.[*].clientCin").value(hasItem(DEFAULT_CIN)))
        ;*/
    }

    @Test
    public void getClientById() throws Exception {
        // Initialize the database
        /*Client clientSaved = clientService.createClient(clientDTO);
        // Get the clients
        restClientMockMvc.perform(get("/api/clients/{id}", clientSaved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.clientId").value(clientSaved.getId()))
                .andExpect(jsonPath("$.clientNumTel").value(DEFAULT_NUM_TEL))
                .andExpect(jsonPath("$.clientEmail").value(DEFAULT_EMAIL))
                .andExpect(jsonPath("$.clientAdresse").value(DEFAULT_ADRESSE))
                .andExpect(jsonPath("$.clientName").value(DEFAULT_NOM1))
                .andExpect(jsonPath("$.clientPrenom").value(DEFAULT_PRENOM1));*/
    }

    @Test
    public void deleteClient() throws Exception {
        // Initialize the database
        Client client=mapper.clientDTOToClient(clientDTO);
        Client clientSaved= clientRepository.save(client);
        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Get the client
        restClientMockMvc.perform(delete("/api/clients/{id}", clientSaved.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Client> clientstList = clientRepository.findAll();
        assertThat(clientstList).hasSize(databaseSizeBeforeDelete - 1);
    }
}