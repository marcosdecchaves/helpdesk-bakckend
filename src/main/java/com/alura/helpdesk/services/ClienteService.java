package com.alura.helpdesk.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.hibernate.internal.CriteriaImpl.Subcriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alura.helpdesk.domain.Cliente;
import com.alura.helpdesk.domain.Pessoa;
import com.alura.helpdesk.domain.dtos.ClienteDTO;
import com.alura.helpdesk.repositories.ClienteRepository;
import com.alura.helpdesk.repositories.PessoaRepository;
import com.alura.helpdesk.services.exceptions.DataIntegrityViolationException;
import com.alura.helpdesk.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	public Cliente findById(Integer id)
	{
		Optional<Cliente> obj = clienteRepository.findById(id);
		
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não Encontrado para o id = " + id));
	}

	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}

	public Cliente create(ClienteDTO cliDTO) {
		cliDTO.setId(null);
		cliDTO.setSenha(encoder.encode(cliDTO.getSenha()));;
		validarPorCpfEEmail(cliDTO);
		
		Cliente cli = new Cliente(cliDTO);
		
		return clienteRepository.save(cli);

	}
	
	private void validarPorCpfEEmail(ClienteDTO dto)
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

	public Cliente update(Integer id, @Valid ClienteDTO objDTO) {
		objDTO.setId(id);
		
		Cliente oldObj = findById(id);
		validarPorCpfEEmail(objDTO);
		oldObj = new Cliente(objDTO);
		
		return clienteRepository.save(oldObj);
		
	}

	public void delete(Integer id) {
		Cliente obj = findById(id);
		
		if(obj.getChamados().size() > 0)
		{
			throw new DataIntegrityViolationException("Não é possível excluir. Técnico possui ordens de serviços!");
		}
		
		clienteRepository.deleteById(id);
	}
	

}
