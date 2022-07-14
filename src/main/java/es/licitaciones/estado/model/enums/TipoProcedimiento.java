package es.licitaciones.estado.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TipoProcedimiento {

	ABIERTO(1, "Abierto"), 
	RESTRINGIDO(2, "Restringido"), 
	NEGOCIADO_SIN_PUBLICIDAD(3, "Negociado sin publicidad"),
	NEGOCIADO_CON_PUBLICIDAD(4, "Negociado con publicidad"), 
	DIALOGO_COMPETITIVO(5, "Diálogo competitivo"),
	CONTRATO_MENOR(6, "Contrato menor"), 
	DERIVADO_ACUERDO_MARCO(7, "Derivado de acuerdo marco"), 
	CONCURSO_DE_PROYECTOS(8, "Concurso de proyectos"), 
	ABIERTO_SIMPLIFICADO(9, "Abierto simplificado"),
	ASOCIACION_PARA_LA_INNOVACION(10, "Asociación para la innovación"), 
	DERIVADO_ASOCIACION_PARA_INNOVACION(11, "Derivado de asociación para la innovación"), 
	BASADO_EN_UN_SISTEMA_DINAMICO_DE_ADQUISICION(12, "Basado en un sistema dinámico de adquisición"), 
	LICITACION_CON_NEGOCIACION(13, "Licitación con negociación"),
	NORMAS_INTERNAS(100, "Normas internas"), 
	OTROS(999, "Otros");
	
	private Integer code;
	private String nombre;
	
	public static String retrieveByCode(String code) {
        for (TipoProcedimiento tipoProcedimiento : TipoProcedimiento.values()) {
            if (tipoProcedimiento.getCode() == Integer.valueOf(code)) {
                return tipoProcedimiento.getNombre();
            }
        }
        return null;
    }
}