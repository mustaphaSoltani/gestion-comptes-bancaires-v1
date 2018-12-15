package com.axeane.service;

import com.axeane.GestionCompteBancaireApplication;
import com.axeane.domain.Compte;
import com.axeane.domain.Mouvement;
import com.axeane.domain.dto.MouvementDTO;
import com.axeane.domain.enumuration.TypeMouvementEnum;
import com.axeane.repository.CompteRepository;
import com.axeane.repository.MouvementRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GestionCompteBancaireApplication.class)
@DataJpaTest
@ComponentScan("com.axeane")
@TestPropertySource("/application.properties")
public class MouvementServiceTest {

    @Autowired
    private MouvementService mouvementService;

    @Autowired
    private MouvementRepository mouvementRepository;

    @Autowired
    private CompteRepository compteRepository;

    @Test
    public void saveMouvementTest() {
        MouvementDTO mouvement1 = new MouvementDTO();
        Compte compte1 = new Compte();
        compte1.setNumCompte(123);
        compteRepository.saveAndFlush(compte1);
        mouvement1.setCompteId(compte1.getId());
        mouvement1.setSomme(new BigDecimal(10400));
        mouvement1.setDate(new Date(2019, 10, 10));
        mouvement1.setTypeMouvement(TypeMouvementEnum.RETRAIT);

        mouvementService.saveMouvement(mouvement1);
        Mouvement mouvementResult1 = mouvementRepository.findAll().get(mouvementRepository.findAll().size() - 1);
        assertThat(mouvementResult1.getCompteId(), is(compte1.getId()));
        assertThat(mouvementResult1.getSomme(), is(new BigDecimal(10400)));
    }

    @Test
    public void getMouvementByIdTest() {
        Compte compte2 = new Compte();
        compte2.setNumCompte(456);
        compteRepository.saveAndFlush(compte2);
        MouvementDTO mouvement = new MouvementDTO();
        mouvement.setCompteId(compte2.getId());
        mouvement.setSomme(new BigDecimal(10000));
        mouvement.setTypeMouvement(TypeMouvementEnum.RETRAIT);
        mouvement.setDate(new Date(2019, 10, 10));
        mouvementService.saveMouvement(mouvement);
        List<MouvementDTO> listMouvementAfterSave = mouvementService.findMouvementByCompte((long) 456);
        MouvementDTO mouvementtSaved = mouvementService.getMouvementById(listMouvementAfterSave.get(listMouvementAfterSave.size() - 1).getId());
        assertThat(mouvementtSaved.getCompteId(), is(compte2.getId()));
        assertThat(mouvementtSaved.getSomme(), is(new BigDecimal(10000)));
    }

    @Test
    public void findAllMouvementTest(){
        int sizeListMouvementBeforeSave = mouvementService.findMouvementByCompte((long) 456).size();
        Compte compte3 = new Compte();
        compte3.setNumCompte(521);
        compteRepository.saveAndFlush(compte3);
        Mouvement mouvement = new Mouvement();
        mouvement.setCompteId(compte3.getId());
        mouvement.setDate(new Date(2019, 10, 10));
        mouvement.setSomme(new BigDecimal(1000));
        mouvement.setTypeMouvement(TypeMouvementEnum.RETRAIT);

        List<MouvementDTO> listMouvementAfterSave = new ArrayList<>();
        boolean throwException = false;
        try {
            mouvementRepository.save(mouvement);
            listMouvementAfterSave = mouvementService.findMouvementByCompte((long) 521);
        } catch (Exception e) {
            throwException = true;
        }
        assertThat(listMouvementAfterSave.size(), is(sizeListMouvementBeforeSave));
        assertThat(throwException, is(false));
    }

    @Test
    public void deleteMouvementTest() throws Exception {
        Compte compte4 = new Compte();
        compte4.setNumCompte(214);
        compteRepository.saveAndFlush(compte4);
        int sizeListMouvementBeforeDelete = mouvementService.findMouvementByCompte((long) 214).size();
        MouvementDTO mouvement = new MouvementDTO();
        mouvement.setCompteId(compte4.getId());
        mouvement.setSomme(new BigDecimal(1000));
        mouvement.setTypeMouvement(TypeMouvementEnum.RETRAIT);
        mouvement.setDate(new Date(2019, 10, 10));
        mouvementService.saveMouvement(mouvement);
        MouvementDTO mouvement1 = mouvementService.findMouvementByCompte((long) 214).get(mouvementService.findMouvementByCompte((long) 214).size() - 1);
        mouvementService.delete(mouvement1.getId());
        int sizeListMouvementAfterDelete = mouvementService.findMouvementByCompte((long) 214).size();
        assertThat(sizeListMouvementBeforeDelete, is(sizeListMouvementAfterDelete));
    }

    @Test
    public void checkCompteIdIsNotNull() throws Exception {
        int sizeListMouvemntBeforeSave = mouvementRepository.findAll().size();
        Compte compte3 = new Compte();
        compte3.setNumCompte(521);
        compteRepository.saveAndFlush(compte3);
        MouvementDTO mouvement = new MouvementDTO();
        mouvement.setDate(new Date(2019, 10, 10));
        mouvement.setCompteId(null);
        List<MouvementDTO> listMouvementAfterSave = new ArrayList<>();
        boolean throwException = false;
        try {
            mouvementService.saveMouvement(mouvement);
            listMouvementAfterSave = mouvementService.findMouvementByCompte((long) 521);
        } catch (Exception e) {
            throwException = true;
        }
        assertThat(listMouvementAfterSave.size(), is(sizeListMouvemntBeforeSave));
        assertThat(throwException, is(true));
    }
}
