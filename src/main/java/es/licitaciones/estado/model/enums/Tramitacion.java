package es.licitaciones.estado.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Tramitacion {

	ORDINARIA(1, "Ordinaria"), 
	URGENTE(2, "Urgente"),
	EMERGENCIA(3, "Emergencia");
	
	private Integer code;
	private String nombre;
	
	public static String retrieveByCode(String code) {
        for (Tramitacion tramitacion : Tramitacion.values()) {
            if (tramitacion.getCode() == Integer.valueOf(code)) {
                return tramitacion.getNombre();
            }
        }
        return null;
    }
}
