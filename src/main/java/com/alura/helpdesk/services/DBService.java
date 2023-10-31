package com.alura.helpdesk.services;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alura.helpdesk.domain.Chamado;
import com.alura.helpdesk.domain.Cliente;
import com.alura.helpdesk.domain.Tecnico;
import com.alura.helpdesk.domain.enums.Perfil;
import com.alura.helpdesk.domain.enums.Prioridade;
import com.alura.helpdesk.domain.enums.Status;
import com.alura.helpdesk.repositories.ChamadoRepository;
import com.alura.helpdesk.repositories.ClienteRepository;
import com.alura.helpdesk.repositories.TecnicoRepository;

@Service
public class DBService {

	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private ChamadoRepository chamadoRepository;
	
	//criar um encode da senha, para ela nao ficar visivel no banco de dados
	@Autowired
	private BCryptPasswordEncoder encoder;
	
//	@Bean
	public void instanciaDB()
	{
		Tecnico tec1 = new Tecnico(null, "Marcos de Castro", "99290078065", "marcos.chaves@sysmap.com.br", encoder.encode("1234"));
		tec1.addtPerfil(Perfil.ADMIN);
		
		Cliente cli1 = new Cliente(null, "Linus Torvalds", "15127627094", "torvalds@mail.com", encoder.encode("1234"));
		
		Chamado chamado1 = new Chamado(null, Prioridade.MEDIA, Status.ANDAMENTO, "Chamado 01", "Primeiro Chamado", tec1, cli1);
		
		Tecnico tec2 = new Tecnico(null, "Jose Moreira", "21307696082", "jmoreira@sysmap.com.br", encoder.encode("1234"));
		tec2.addtPerfil(Perfil.CLIENTE);
		
		tecnicoRepository.saveAll(Arrays.asList(tec1));
		clienteRepository.saveAll(Arrays.asList(cli1));
		chamadoRepository.saveAll(Arrays.asList(chamado1));
		
		tecnicoRepository.saveAll(Arrays.asList(tec2));
	}
}
