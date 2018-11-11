package com.axeane.repository;

import com.axeane.domain.Client;
import com.axeane.domain.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client getClientByCin(String cin);

    Client getClientById(Long id);

    Client getClientByComptes(Compte c);

    List<Client> getAllByNom(String nom);
}
