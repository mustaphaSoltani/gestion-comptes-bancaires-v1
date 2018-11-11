package com.axeane.web.rest.testContainer;

import com.axeane.GestionCompteBancaireApplication;
import com.axeane.domain.Compte;
import com.axeane.domain.Mouvement;
import com.axeane.domain.enumuration.TypeMouvementEnum;
import com.axeane.repository.CompteRepository;
import com.axeane.repository.MouvementRepository;
import com.axeane.service.MouvementService;
import com.axeane.web.rest.MouvementResource;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import com.axeane.web.errors.ExceptionTranslator;
import com.axeane.web.rest.config.TestUtil;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = {MouvementResourceTestContainer.Initializer.class})
@SpringBootTest(classes = GestionCompteBancaireApplication.class)
@TestPropertySource("/application-test-container.properties")
@ComponentScan({"com.axeane.domain.util","com.axeane.service"})
public class MouvementResourceTestContainer {

    private static final TypeMouvementEnum DEFAULT_TYPE_MOUVEMENT = TypeMouvementEnum.RETRAIT;
    private static final TypeMouvementEnum UPDATED_TYPE_MOUVEMENT= TypeMouvementEnum.VERSEMENT;

    private static final BigDecimal DEFAULT_SOMME =new BigDecimal(200);
    private static final BigDecimal UPDATED_SOMME = new BigDecimal(201);

    private static final Date DEFAULT_DATE = new Date(11/12/2019);
    private static final Date UPDATED_DATE = new Date(11/12/2019);

    @Autowired
    private MouvementService mouvementService;

    @Autowired
    private MouvementRepository mouvementRepository;

    @Autowired
    private CompteRepository compteRepository;

    private MappingJackson2HttpMessageConverter jacksonMessageConverter=new MappingJackson2HttpMessageConverter();

    private PageableHandlerMethodArgumentResolver pageableArgumentResolver= new PageableHandlerMethodArgumentResolver();

    private ExceptionTranslator exceptionTranslator=new ExceptionTranslator();

    @Autowired
    private EntityManager em;

    private MockMvc restMouvementMockMvc;

    private Mouvement mouvement;

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
        MouvementResource mouvementResource = new MouvementResource(mouvementService);
        this.restMouvementMockMvc = MockMvcBuilders.standaloneSetup(mouvementResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .setControllerAdvice(exceptionTranslator)
                .setMessageConverters(jacksonMessageConverter).build();
    }

    public Mouvement createEntity(EntityManager em) {
        Mouvement mouvement = new Mouvement();
        Compte compte=new Compte();
        compte.setNumCompte(123);
        compteRepository.saveAndFlush(compte);
        mouvement.setCompteId(1L);
        mouvement.setSomme(DEFAULT_SOMME);
        mouvement.setTypeMouvement(DEFAULT_TYPE_MOUVEMENT);
        mouvement.setDate(DEFAULT_DATE);

        return mouvement;
    }

    @Before
    public void initTest() {
        mouvement = createEntity(em);
    }

    @Test
    @Transactional
    public void createMouvement() throws Exception {
        int databaseSizeBeforeCreate = mouvementRepository.findAll().size();
        restMouvementMockMvc.perform(post("/api/mouvements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mouvement)))
                .andExpect(status().isCreated());
        // Validate the Mouvement in the database
        List<Mouvement> mouvementList = mouvementRepository.findAll();
        assertThat(mouvementList).hasSize(databaseSizeBeforeCreate + 1);
        Mouvement testMouvement = mouvementList.get(mouvementList.size() - 1);
        assertThat(testMouvement.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testMouvement.getSomme()).isEqualTo(DEFAULT_SOMME);
        assertThat(testMouvement.getTypeMouvement()).isEqualTo(DEFAULT_TYPE_MOUVEMENT);
    }

    @Test
    @Transactional
    public void updateMouvement() throws Exception {
        mouvementRepository.save(mouvement);
        int databaseSizeBeforeUpdate = mouvementRepository.findAll().size();

        Mouvement updatedMouvement = mouvementRepository.getOne(mouvement.getId());
        updatedMouvement.setDate(UPDATED_DATE);
        updatedMouvement.setSomme(UPDATED_SOMME);
        updatedMouvement.setTypeMouvement(UPDATED_TYPE_MOUVEMENT);

        restMouvementMockMvc.perform(put("/api/mouvements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMouvement)))
                .andExpect(status().isOk());

        List<Mouvement> mouvementList = mouvementRepository.findAll();
        assertThat(mouvementList).hasSize(databaseSizeBeforeUpdate);
        Mouvement testMouvement = mouvementList.get(mouvementList.size() - 1);
        assertThat(testMouvement.getTypeMouvement()).isEqualTo(UPDATED_TYPE_MOUVEMENT);
        assertThat(testMouvement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testMouvement.getSomme()).isEqualTo(UPDATED_SOMME);
    }

    @Test
    public void getAllMouvementByNumCompte() throws Exception {
        // Initialize the database
        Mouvement mouvementSaved = mouvementService.saveMouvement(mouvement);
        // Get all the mouvementList
        restMouvementMockMvc.perform(get("/api/mouvements/numCte/{numC}?sort=numC,desc", mouvementSaved.getCompte().getNumCompte()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(mouvementSaved.getId().intValue())))
                .andExpect(jsonPath("$.[*].somme").value(hasItem(DEFAULT_SOMME)))
                .andExpect(jsonPath("$.[*].typeMouvement").value(hasItem(DEFAULT_TYPE_MOUVEMENT)))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    public void getMouvementById() throws Exception {
        // Initialize the database
        Mouvement mouvementSaved = mouvementRepository.saveAndFlush(mouvement);
        // Get the clients
        restMouvementMockMvc.perform(get("/api/mouvements/{id}", mouvementSaved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(mouvementSaved.getId()))
                .andExpect(jsonPath("$.somme").value(DEFAULT_SOMME.doubleValue()))
                .andExpect(jsonPath("$.typeMouvement").value(DEFAULT_TYPE_MOUVEMENT.toString()));
    }

    @Test
    public void getNonExistingMouvement() throws Exception {
        // Get the mouvement
        restMouvementMockMvc.perform(get("/api/mouvements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteMouvement() throws Exception {
        mouvementRepository.save(mouvement);
        int databaseSizeBeforeDelete = mouvementRepository.findAll().size();
        restMouvementMockMvc.perform(delete("/api/mouvements/{id}", mouvement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        List<Mouvement> mouvementtList = mouvementRepository.findAll();
        assertThat(mouvementtList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
