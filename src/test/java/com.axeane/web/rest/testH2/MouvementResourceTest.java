package com.axeane.web.rest.testH2;

import com.axeane.GestionCompteBancaireApplication;
import com.axeane.domain.Compte;
import com.axeane.domain.Mouvement;
import com.axeane.domain.enumuration.TypeMouvementEnum;
import com.axeane.repository.CompteRepository;
import com.axeane.repository.MouvementRepository;
import com.axeane.service.MouvementService;
import com.axeane.web.errors.ExceptionTranslator;
import com.axeane.web.rest.MouvementResource;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestionCompteBancaireApplication.class)
@DataJpaTest
@ComponentScan({"com.axeane.domain.util","com.axeane.service"})
public class MouvementResourceTest {

    private static final TypeMouvementEnum DEFAULT_TYPE_MOUVEMENT = TypeMouvementEnum.RETRAIT;
    private static final TypeMouvementEnum UPDATED_TYPE_MOUVEMENT= TypeMouvementEnum.RETRAIT;

    private static final BigDecimal DEFAULT_SOMME = new BigDecimal(200);
    private static final BigDecimal UPDATED_SOMME = new BigDecimal(300);

    private static final Date DEFAULT_DATE = new Date(13/12/2019);
    private static final Date UPDATED_DATE = new Date();

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

    @Before
    public void setUp(){
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
        compte.setNumCompte(456);
        compteRepository.saveAndFlush(compte);
        mouvement.setCompteId(compte.getId());
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
    }

    @Test
    @Transactional
    public void updateMouvement() throws Exception {
        // Initialize the database
        mouvementRepository.save(mouvement);
        int databaseSizeBeforeUpdate = mouvementRepository.findAll().size();

        // Update the Mouvement
        Mouvement updatedMouvement = mouvementRepository.getOne(mouvement.getId());
        updatedMouvement.setDate(UPDATED_DATE);
        updatedMouvement.setSomme(UPDATED_SOMME);
        updatedMouvement.setTypeMouvement(UPDATED_TYPE_MOUVEMENT);

        restMouvementMockMvc.perform(put("/api/mouvements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMouvement)))
                .andExpect(status().isOk());

        // Validate the Client in the database
        List<Mouvement> mouvementList = mouvementRepository.findAll();
        assertThat(mouvementList).hasSize(databaseSizeBeforeUpdate);
        Mouvement testMouvement = mouvementList.get(mouvementList.size() - 1);
        assertThat(testMouvement.getTypeMouvement()).isEqualTo(UPDATED_TYPE_MOUVEMENT);
        assertThat(testMouvement.getSomme()).isEqualTo(UPDATED_SOMME);
        assertThat(testMouvement.getDate()).isEqualTo(UPDATED_DATE);
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
                .andExpect(jsonPath("$.somme").value(DEFAULT_SOMME))
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
    public void deleteMouvementTest() throws Exception {
        // Initialize the database
        mouvementRepository.save(mouvement);
        int databaseSizeBeforeDeleted = mouvementRepository.findAll().size();

        // Get the mouvement
        restMouvementMockMvc.perform(delete("/api/mouvements/{id}", mouvement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Mouvement> mouvementtListt = mouvementRepository.findAll();
        assertThat(mouvementtListt).hasSize(databaseSizeBeforeDeleted - 1);
    }
}