package es.licitaciones.estado.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Estado {

	PRE("Anuncio Previo"), 
	PUB("En plazo"), 
	EV("Pendiente de adjudicación"),
	ADJ("Adjudicada"), 
	RES("Resuelta"), 
	ANUL("Anulada");

	private String nombre;
}
