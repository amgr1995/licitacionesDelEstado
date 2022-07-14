package es.licitaciones.estado.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.licitaciones.estado.model.ProcessAtomDTO;
import es.licitaciones.estado.service.BidProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(LicitacionesEstadoController.BASE_MAPPING)
@RequiredArgsConstructor
public class LicitacionesEstadoController {

	static final String BASE_MAPPING = "/api/licEstado";

	private final BidProcessService bidProcessService;

	@PostMapping
	public ResponseEntity saveLicitacion(@RequestBody ProcessAtomDTO processAtomDTO) {
		log.info("REST saveLicitacion ficheroEntrada {}", processAtomDTO.getFicheroEntrada());

		bidProcessService.procesarDirectorio(processAtomDTO.getFicheroEntrada());
		return ResponseEntity.ok(HttpStatus.CREATED);
	}
}
