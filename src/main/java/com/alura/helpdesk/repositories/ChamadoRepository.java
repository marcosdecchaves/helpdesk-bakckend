package com.alura.helpdesk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alura.helpdesk.domain.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Integer>{

}
