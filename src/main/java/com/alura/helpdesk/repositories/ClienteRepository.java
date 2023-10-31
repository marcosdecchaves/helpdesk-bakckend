package com.alura.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alura.helpdesk.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{

}
