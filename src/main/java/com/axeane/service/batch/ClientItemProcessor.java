package com.axeane.service.batch;

import com.axeane.domain.Client;
import com.axeane.domain.Compte;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

import java.util.Set;

public class ClientItemProcessor implements ItemProcessor<Client, Client> {

    private static final Logger log = LoggerFactory.getLogger(ClientItemProcessor.class);

    @Override
    public Client process(final Client client) throws Exception {
        final String cin = client.getCin();
        final String nom = client.getNom();
        final String prenom =client.getPrenom();
        final String adresse =client.getAdresse();
        final String email =client.getEmail();
        final String numTel =client.getNumTel();
        Compte compte= new Compte();
        client.getComptes().forEach(compete -> {
            compete.setClient(client);
        });
        final Set<Compte> comptes =  client.getComptes();

        final Client transformedClient = new Client(cin, nom,prenom,adresse,email,numTel,comptes);

        log.info("Converting (" + client + ") into (" + transformedClient + ")");

        return transformedClient;
    }

}
