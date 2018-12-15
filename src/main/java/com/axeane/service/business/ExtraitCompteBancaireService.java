package com.axeane.service.business;

import com.axeane.domain.dto.ClientDTO;
import com.axeane.domain.dto.MouvementDTO;
import com.axeane.models.MouvementModel;
import com.axeane.service.ClientService;
import com.axeane.service.MouvementService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExtraitCompteBancaireService {
    private final ClientService clientService;
    private final MouvementService mouvementService;

    public ExtraitCompteBancaireService(ClientService clientService, MouvementService mouvementService) {
        this.clientService = clientService;
        this.mouvementService = mouvementService;
    }

    public ByteArrayInputStream exportextraitBancaireToPdf(Long numCompte) throws JRException {

        InputStream inputStream = getClass().getResourceAsStream("/reports/extrait.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        Map<String, Object> parametreMap = new HashMap<>();

        ClientDTO client = clientService.getClientBynNumCompte(numCompte);

        List<MouvementDTO> mouvements = mouvementService.findMouvementByCompte(numCompte);
        BigDecimal solde = mouvements.iterator().next().getSomme();
        List<MouvementModel> mouvementModels = new ArrayList<>();
        for (MouvementDTO detailsMouvement : mouvementService.findMouvementByCompte(numCompte)) {
            MouvementModel detailsModel = new MouvementModel();
            detailsModel.setDate(detailsMouvement.getDate());
            detailsModel.setSomme(detailsMouvement.getSomme());
            detailsModel.setTypeMouvement(detailsMouvement.getTypeMouvement());
            BigDecimal resultSolde;

            switch (detailsMouvement.getTypeMouvement().toString()) {
                case "RETRAIT":
                    resultSolde = solde.subtract(detailsMouvement.getSomme());
                    solde = resultSolde;
                    break;
                case "VERSEMENT":
                    resultSolde = solde.add(detailsMouvement.getSomme());
                    solde = resultSolde;
                    break;
                case "VIREMENT":
                    resultSolde = solde.add(detailsMouvement.getSomme());
                    solde = resultSolde;
                    break;
            }
            detailsModel.setSolde(solde);
            mouvementModels.add(detailsModel);
        }
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(mouvementModels);
        parametreMap.put("numCompte", numCompte);
        parametreMap.put("nom", client.getClientName());
        parametreMap.put("prenom", client.getClientPrenom());
        parametreMap.put("adresse", client.getClientAdresse());
        parametreMap.put("email", client.getClientEmail());
        parametreMap.put("datasource", jrDataSource);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametreMap, jrDataSource);
        byte[] bytes;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
        bytes = byteArrayOutputStream.toByteArray();
        return new ByteArrayInputStream(bytes);
    }
}