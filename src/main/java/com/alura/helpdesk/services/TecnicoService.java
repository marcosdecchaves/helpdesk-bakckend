package com.alura.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alura.helpdesk.domain.Pessoa;
import com.alura.helpdesk.domain.Tecnico;
import com.alura.helpdesk.domain.dtos.TecnicoDTO;
import com.alura.helpdesk.repositories.PessoaRepository;
import com.alura.helpdesk.repositories.TecnicoRepository;
import com.alura.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.alura.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class TecnicoService {
	
	@Autowired
	private TecnicoRepository tecnicoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	public Tecnico findById(Integer id)
	{
		Optional<Tecnico> obj = tecnicoRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não Encontrado para o id = " + id));
	}

	public List<Tecnico> findAll() {
		return tecnicoRepository.findAll();
	}

	public Tecnico create(TecnicoDTO tecDTO) {
		tecDTO.setId(null);
		tecDTO.setSenha(encoder.encode(tecDTO.getSenha()));
		validarPorCpfEEmail(tecDTO);
		
		Tecnico tec = new Tecnico(tecDTO);
		
		return tecnicoRepository.save(tec);

	}
	
	private void validarPorCpfEEmail(TecnicoDTO dto)
	{
		Optional<Pessoa> obj = pessoaRepository.findByCpf(dto.getCpf());
		
		if(obj.isPresent() && obj.get().getId() != dto.getId())
		{
			throw new DataIntegrityViolationException("CPF já cadastrado no sistema");
		}
		
		obj = pessoaRepository.findByEmail(dto.getEmail());
		
		if(obj.isPresent() && obj.get().getId() != dto.getId())
		{
			throw new DataIntegrityViolationException("Email já cadastrado no sistema");
		}
	}

	public Tecnico update(Integer id, @Valid TecnicoDTO objDTO) {
		objDTO.setId(id);
		
		Tecnico oldObj = findById(id);
		validarPorCpfEEmail(objDTO);
		oldObj = new Tecnico(objDTO);
		
		return tecnicoRepository.save(oldObj);
		
	}

	public void delete(Integer id) {
		Tecnico obj = findById(id);
		
		if(obj.getChamados().size() > 0)
		{
			throw new DataIntegrityViolationException("Não é possível excluir. Técnico possui ordens de serviços!");
		}
		
		tecnicoRepository.deleteById(id);
	}
	

}
