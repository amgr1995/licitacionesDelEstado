package es.licitaciones.estado.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessAtomDTO implements Serializable {

	private static final long serialVersionUID = -330530749089426188L;

	private String ficheroEntrada;
}
