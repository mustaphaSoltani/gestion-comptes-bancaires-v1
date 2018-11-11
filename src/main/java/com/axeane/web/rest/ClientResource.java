package com.axeane.web.rest;

import com.axeane.domain.Client;
import com.axeane.domain.Mail;
import com.axeane.domain.Views;
import com.axeane.domain.util.ResponseUtil;
import com.axeane.service.ClientService;
import com.axeane.service.business.ExtraitCompteBancaireService;
import com.axeane.service.business.MailExtraitService;
import com.axeane.web.util.HeaderUtil;
import com.fasterxml.jackson.annotation.JsonView;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Contact;
import net.sf.jasperreports.engine.JRException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
public class ClientResource {
    private final Logger log = LoggerFactory.getLogger(ClientResource.class);

    private static final String ENTITY_NAME = "client";

    private final ClientService clientService;
    private final ExtraitCompteBancaireService extraitCompteBancaireService;
    private final MailExtraitService mailExtraitService;

    public ClientResource(ClientService clientService, ExtraitCompteBancaireService extraitCompteBancaireService, MailExtraitService mailExtraitService) {
        this.clientService = clientService;

        this.extraitCompteBancaireService = extraitCompteBancaireService;
        this.mailExtraitService = mailExtraitService;
    }

    @PostMapping
    @JsonView(value = {Views.ClientView.class})
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) throws URISyntaxException {
        log.debug("REST request to save Client : {}", client.toString());
        if (client.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new client cannot already have an ID")).body(null);
        }
        Client result = clientService.createClient(client);
        return ResponseEntity.created(new URI("/api/clients/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    @PutMapping
    @JsonView(value = {Views.ClientView.class})
    public ResponseEntity<Client> updateClient(@Valid @RequestBody Client client) throws URISyntaxException {
        log.debug("REST request to update Client : {}", client.toString());
        if (client.getId() == null) {
            return createClient(client);
        }
        Client result = clientService.createClient(client);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, client.getId().toString()))
                .body(result);
    }

    @GetMapping
    @JsonView(value = {Views.ClientView.class})
    public ResponseEntity<List<Client>> getAllClient() {
        log.debug("REST request to get a page of Clients");
        List<Client> page = clientService.findAllClient();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @JsonView(value = {Views.ClientView.class})
    public ResponseEntity getClientById(@PathVariable Long id) {
        log.debug("REST request to get Client : {}", id);
        Optional<Client> client = Optional.ofNullable(clientService.getClientById(id));
        return ResponseUtil.wrapOrNotFound(client);
    }

    @GetMapping("cin/{cin}")
    @JsonView(value = {Views.ClientView.class})
    public ResponseEntity getClientByCin(@PathVariable String cin) {
        log.debug("REST request to get Client : {}", cin);
        Optional<Client> client = Optional.ofNullable(clientService.getClientBynCin(cin));
        return ResponseUtil.wrapOrNotFound(client);
    }

    @GetMapping("nom/{nom}")
    @JsonView(value = {Views.ClientView.class})
    public ResponseEntity<List<Client>> getClientByNom(@PathVariable String nom) {
        log.debug("REST request to get Client : {}", nom);
        List<Client> page = clientService.getClientByNom(nom);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("numCpte/{numCompte}")
    @JsonView(value = Views.ClientView.class)
    public ResponseEntity getClientBynumCompte(@PathVariable Integer numCompte) {
        log.debug("REST request to get Client : {}", numCompte);
        Optional<Client> client = Optional.ofNullable(clientService.getClientBynNumCompte(numCompte));
        return ResponseUtil.wrapOrNotFound(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        log.debug("REST request to delete Client : {}", id);
        clientService.deleteClient(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/extraitBancairepdf/{numC}")
    public void entreprisesPdf(HttpServletResponse response, @PathVariable Integer numC) throws JRException, IOException {
        log.debug("REST request to Extrait file pdf : {}");
        ByteArrayInputStream byteArrayInputStream=extraitCompteBancaireService.exportextraitBancaireToPdf(numC);
        OutputStream os = response.getOutputStream();

        response.setContentType("application/pdf; name=\"MyFile.pdf\"");
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Cache-Control", "private, must-revalidate, post-check=0, pre-check=0, max-age=1");
        response.setHeader("Pragma", "public");
        response.setHeader("Content-Disposition", "attachment; filename=\"file_from_server.pdf\"");
        byte[] pdfAsStream = new byte[byteArrayInputStream.available()];
        byteArrayInputStream.read(pdfAsStream);
        os.write(pdfAsStream);
        os.close();
    }

    @PostMapping("/sendMail/{numCompte}")
    public void sendByMail(@RequestBody String destinations, @PathVariable("numCompte") int numCompte) throws Exception {
        ByteArrayInputStream byteArrayInputStream = extraitCompteBancaireService.exportextraitBancaireToPdf(numCompte);
        String[] des = destinations.split(";");
        JSONArray recipients = new JSONArray();
        for (String de : des) {
            recipients.put(new JSONObject().put(Contact.EMAIL, de));
        }
        mailExtraitService.sendEmailWithMailJet(recipients, "extrait", "extrait",false,
                true, true, byteArrayInputStream );
    }
}
