package es.licitaciones.estado.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SistemaContratacion {

	NO_APLICA(0, "No aplica"), 
	ESTABLECIMIENTO_ACUERDO_MARCO(1, "Establecimiento del Acuerdo Marco"), 
	ESTABLECIMIENTO_SISTEMA_DINAMICO_DE_ADQUISICION(2, "Establecimiento del Sistema Dinámico de Adquisición"),
	CONTRATO_BASADO_ACUERDO_MARCO(3, "Contrato basado en un Acuerdo Marco"), 
	CONTRATO_BASADO_SISTEMA_DINAMICO_ADQUISICION(4, "Contrato basado en un Sistema Dinámico de Adquisición");

	private Integer code;
	private String nombre;
	
	public static String retrieveByCode(String code) {
        for (SistemaContratacion sistemaContratacion : SistemaContratacion.values()) {
            if (sistemaContratacion.getCode() == Integer.valueOf(code)) {
                return sistemaContratacion.getNombre();
            }
        }
        return null;
    }
}
