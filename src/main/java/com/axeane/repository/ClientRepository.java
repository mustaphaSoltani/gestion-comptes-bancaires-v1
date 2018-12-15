package com.axeane.repository;

import com.axeane.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findClientByCin(String cin);

    Client findClientById(Long id);

    List<Client> findAllByNom(String nom);
}
