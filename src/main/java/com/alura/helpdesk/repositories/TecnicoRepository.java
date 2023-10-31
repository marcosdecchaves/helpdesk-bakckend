package com.alura.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alura.helpdesk.domain.Tecnico;

public interface TecnicoRepository extends JpaRepository<Tecnico, Integer>{

}
