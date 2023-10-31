package com.alura.helpdesk.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alura.helpdesk.domain.Chamado;
import com.alura.helpdesk.domain.Cliente;
import com.alura.helpdesk.domain.Tecnico;
import com.alura.helpdesk.domain.dtos.ChamadoDTO;
import com.alura.helpdesk.domain.enums.Prioridade;
import com.alura.helpdesk.domain.enums.Status;
import com.alura.helpdesk.repositories.ChamadoRepository;
import com.alura.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class ChamadoService {

	@Autowired
	private ChamadoRepository chamadoRepository;
	
	@Autowired
	private TecnicoService tecnicoService;
	
	@Autowired
	private ClienteService clienteService;
	
	public Chamado findById(Integer id)
	{
		Optional<Chamado> obj = chamadoRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto de ID " + id + " não encontrado!"));
	}

	public List<Chamado> findAll() {
		return chamadoRepository.findAll();
	}

	public Chamado create(@Valid ChamadoDTO objDTO) {
		 
		return chamadoRepository.save(newChamado(objDTO));
	}
	
	public Chamado update(Integer id, @Valid ChamadoDTO dto) {
		dto.setId(id);
		
		Chamado oldObj = findById(id);
		oldObj = newChamado(dto);
		
		return chamadoRepository.save(oldObj);
	}
	
	private Chamado newChamado(ChamadoDTO obj)
	{
		 Tecnico tec = tecnicoService.findById(obj.getIdTecnico());
		 Cliente cli = clienteService.findById(obj.getIdCiente());
		 
		 Chamado chamado = new Chamado();
		 
		 if(obj != null)
		 {
			 chamado.setId(obj.getId());
		 }
		 
		 if(obj.getStatus().equals(2))
		 {
			 chamado.setDataFechamento(LocalDate.now());
		 }
		 
		 chamado.setTecnico(tec);;
		 chamado.setCliente(cli);
		 chamado.setPrioridade(Prioridade.toEnum(obj.getPrioridade()));
		 chamado.setStatus(Status.toEnum(obj.getStatus()));
		 chamado.setTitulo(obj.getTitulo());
		 chamado.setObservacoes(obj.getObservacoes());
		 
		 return chamado;
	}

	
}
