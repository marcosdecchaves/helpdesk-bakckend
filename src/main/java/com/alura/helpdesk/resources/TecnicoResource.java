package com.alura.helpdesk.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.alura.helpdesk.domain.Tecnico;
import com.alura.helpdesk.domain.dtos.TecnicoDTO;
import com.alura.helpdesk.services.TecnicoService;


@RestController
@RequestMapping(value = "/tecnicos")
public class TecnicoResource {
	
	@Autowired
	private TecnicoService tecnicoService;

	@GetMapping(value = "/{id}")
	public ResponseEntity<TecnicoDTO> findById(@PathVariable Integer id)
	{
		Tecnico obj = this.tecnicoService.findById(id);
		
		return ResponseEntity.ok().body(new TecnicoDTO(obj));
	}
	
	@GetMapping
	public ResponseEntity<List<TecnicoDTO>> findAll()
	{
		List<Tecnico> listaTecnicos = tecnicoService.findAll();
		
		List<TecnicoDTO> listaDTO = listaTecnicos.stream().map(obj -> new TecnicoDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listaDTO);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
	public ResponseEntity<TecnicoDTO> create(@Valid @RequestBody TecnicoDTO tecDTO)
	{
		Tecnico newTecnico = tecnicoService.create(tecDTO);
		
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newTecnico.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<TecnicoDTO> update(@PathVariable Integer id, @Valid @RequestBody TecnicoDTO objDTO) {
		Tecnico obj = tecnicoService.update(id, objDTO);
		
		return ResponseEntity.ok().body(new TecnicoDTO(obj));
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<TecnicoDTO> delete(@PathVariable Integer id)
	{
		tecnicoService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
}
