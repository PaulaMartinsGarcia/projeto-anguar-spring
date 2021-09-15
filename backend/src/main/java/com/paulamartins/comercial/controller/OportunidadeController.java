package com.paulamartins.comercial.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.paulamartins.comercial.model.Oportunidade;
import com.paulamartins.comercial.repository.OportunidadeRepository;

@RestController
@RequestMapping("/oportunidades")
public class OportunidadeController {
	
	@Autowired
	private OportunidadeRepository oportunidades;
	
	@GetMapping
	public List<Oportunidade> listar() {
		return oportunidades.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Oportunidade> buscar(@PathVariable Long id) {
		Optional<Oportunidade> oportunidade = oportunidades.findById(id);
		
		if (oportunidade.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(oportunidade.get());
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Oportunidade adicionar(@Valid @RequestBody Oportunidade oportunidade) {
		Optional<Oportunidade> oportunidadeExistente = oportunidades.
				findByDescricaoAndNomeProspecto(oportunidade.getDescricao(), oportunidade.getNomeProspecto());
		
		if (oportunidadeExistente.isPresent()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Já existe uma oportunidade para este prospecto com a mesma descrição");
			
		}
		
		return oportunidades.save(oportunidade);
	}
	
	@PutMapping("/{Id}")
	public ResponseEntity<Oportunidade> atualizar(@PathVariable Long Id, 
			@RequestBody Oportunidade oportunidade){
			
		if (!oportunidades.existsById(Id)) {
				return ResponseEntity.notFound().build();
			}
			
		oportunidade.setId(Id);
		oportunidade = oportunidades.save(oportunidade);
		
			return ResponseEntity.ok(oportunidade);
		}
	
	@DeleteMapping("/{Id}")
	public ResponseEntity<Void> excluir(@PathVariable Long Id) {
		if (!oportunidades.existsById(Id)) {
			return ResponseEntity.notFound().build();
		}
		
		oportunidades.deleteById(Id);
		
		return ResponseEntity.noContent().build();
		
	}
}

