package com.axeane.service.testContainer;

import com.axeane.GestionCompteBancaireApplication;
import com.axeane.domain.Compte;
import com.axeane.domain.Mouvement;
import com.axeane.domain.enumuration.TypeMouvementEnum;
import com.axeane.repository.CompteRepository;
import com.axeane.repository.MouvementRepository;
import com.axeane.service.MouvementService;
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

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = {MouvementServiceTestContainer.Initializer.class})
@SpringBootTest(classes = GestionCompteBancaireApplication.class)
@TestPropertySource("/application-test-container.properties")
@ComponentScan({"com.axeane.domain.util","com.axeane.service"})
public class MouvementServiceTestContainer {

    @Autowired
    private MouvementService mouvementService;

    @Autowired
    private MouvementRepository mouvementRepository;

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
    public void saveMouvementTest() throws Exception {
        Mouvement mouvement = new Mouvement();
        Compte compte = new Compte();
        compte.setNumCompte(123);
        compteRepository.saveAndFlush(compte);
        mouvement.setCompteId(compte.getId());
        mouvement.setSomme(new BigDecimal(10400));
        mouvement.setTypeMouvement(TypeMouvementEnum.RETRAIT);

        mouvementService.saveMouvement(mouvement);
        Mouvement mouvementResult = mouvementRepository.findAll().get(mouvementRepository.findAll().size() - 1);
        assertThat(mouvementResult.getTypeMouvement(), is(TypeMouvementEnum.RETRAIT));
    }

    @Test
    public void getMouvementByIdTest() throws Exception {
        Compte compte = new Compte();
        compte.setNumCompte(456);
        compteRepository.saveAndFlush(compte);
        Mouvement mouvement = new Mouvement();
        mouvement.setCompteId(1L);
        mouvement.setSomme(new BigDecimal(10000));
        mouvement.setTypeMouvement(TypeMouvementEnum.RETRAIT);
        mouvementService.saveMouvement(mouvement);
        List<Mouvement> listMouvementAfterSave = mouvementService.findAllMouvementByCompte(456);
        Mouvement mouvementtSaved = mouvementService.getMouvementById(listMouvementAfterSave.get(listMouvementAfterSave.size() - 1).getId());
        assertThat(mouvementtSaved.getCompteId(), is(1L));
        assertThat(mouvementtSaved.getSomme(), is(new BigDecimal(10000)));
    }

    @Test
    public void findAllMouvementTest() throws Exception {
        int sizeListMouvementBeforeSave = mouvementService.findAllMouvementByCompte(456).size();
        Compte compte = new Compte();
        compte.setNumCompte(521);
        compteRepository.saveAndFlush(compte);
        Mouvement mouvement = new Mouvement();
        mouvement.setCompteId(1L);
        mouvement.setSomme(new BigDecimal(1000));
        mouvement.setTypeMouvement(TypeMouvementEnum.RETRAIT);

        List<Mouvement> listMouvementAfterSave = new ArrayList<>();
        boolean throwException = false;
        try {
            mouvementRepository.save(mouvement);
            listMouvementAfterSave = mouvementService.findAllMouvementByCompte(521);
        } catch (Exception e) {
            throwException = true;
        }
        assertThat(listMouvementAfterSave.size(), is(sizeListMouvementBeforeSave + 1));
        assertThat(throwException, is(false));
    }

    @Test
    public void deleteMouvementTest() throws Exception {
        Compte compte = new Compte();
        compte.setNumCompte(214);
        compteRepository.saveAndFlush(compte);
        int sizeListMouvementBeforeDelete = mouvementService.findAllMouvementByCompte(214).size();
        Mouvement mouvement = new Mouvement();
        mouvement.setCompteId(1L);
        mouvement.setSomme(new BigDecimal(1000));
        mouvement.setTypeMouvement(TypeMouvementEnum.RETRAIT);
        mouvementService.saveMouvement(mouvement);
        Mouvement mouvement1 = mouvementService.findAllMouvementByCompte(214).get(mouvementService.findAllMouvementByCompte(214).size() - 1);
        mouvementService.delete(mouvement1.getId());
        int sizeListMouvementAfterDelete = mouvementService.findAllMouvementByCompte(214).size();
        assertThat(sizeListMouvementBeforeDelete, is(sizeListMouvementAfterDelete));
    }
}
