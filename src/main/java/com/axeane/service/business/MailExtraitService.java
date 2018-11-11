package com.axeane.service.business;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Email;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Service
public class MailExtraitService {

    private final ExtraitCompteBancaireService extraitCompteBancaireService;
    private final MailjetClient client;

    public MailExtraitService(ExtraitCompteBancaireService extraitCompteBancaireService) {
        this.extraitCompteBancaireService = extraitCompteBancaireService;
        this.client = new MailjetClient("cbaf40b17a7417bce03a5df70a163043", "59bd260cb89b775dec4ea67d2287b831");// TDODO get keys from properties;
    }

    public void sendEmailWithMailJet(JSONArray recipients, String subject, String content,
                                     boolean isMultipart, boolean isHtml, boolean attachement, ByteArrayInputStream byteArrayInputStream) {
        MailjetResponse response;

        try {

            MailjetRequest request = new MailjetRequest(Email.resource)
                    .property(Email.FROMEMAIL, "komptacloud@psyscertifies.com")
                    .property(Email.FROMNAME, "Extrait en ligne")
                    .property(Email.SUBJECT, subject)
                    .property(Email.HTMLPART, content)
                    .property(Email.HTMLPART, content)
                    .property(Email.RECIPIENTS, recipients);
            if (attachement) {
                byte[] pdfAsStream = new byte[byteArrayInputStream.available()];
                byteArrayInputStream.read(pdfAsStream);

                request.property(Email.ATTACHMENTS, new JSONArray()
                        .put(new JSONObject()
                                .put("Content-type", "application/pdf")
                                .put("Filename", "extraitCompte.pdf")
                                .put("content", Base64.getEncoder().encodeToString(pdfAsStream))));
            }
            response = client.post(request);
            System.out.println(response.getStatus());
            System.out.println(response.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}