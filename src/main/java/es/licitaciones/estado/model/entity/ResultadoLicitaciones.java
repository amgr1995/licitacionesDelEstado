package es.licitaciones.estado.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "RESULTADO_LICITACIONES")
public class ResultadoLicitaciones implements Serializable {

	private static final long serialVersionUID = 4740528902563400141L;

	@Id
	@Column(name = "IDENTIFICADOR")
	private Long identificador;

	@Column(name = "NUMERO_EXPEDIENTE")
	private String numeroExpediente;

	@Column(name = "RESULTADO_LICITACION")
	private String resultadoLicitacion;

	@Column(name = "LOTE")
	private String lote;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "FECHA_ACUERDO_LICITACION")
	private LocalDateTime fechaAcuerdoLicitacion;

	@Column(name = "NUMERO_OFERTAS_RECIBIDAS_POR_LICITACION")
	private BigDecimal numeroOfertasRecibidasPorLicitacion;

	@Column(name = "PRECIO_OFERTA_MAS_BAJA_POR_LICITACION")
	private BigDecimal precioOfertaMasBajaPorLicitacion;

	@Column(name = "PRECIO_OFERTA_MAS_ALTA_POR_LICITACION")
	private BigDecimal precioOfertaMasAltaPorLicitacion;

	@Column(name = "EXCLUIDO_OFERTAS_POR_ANORMALMENTE_BAJAS")
	private Boolean excluidoOfertasPorAnormalmenteBajas;

	@Column(name = "NUMERO_CONTRATO")
	private String numeroContrato;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "FECHA_FORMALIZACION_CONTRATO")
	private LocalDateTime fechaFormalizacionContrato;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "FECHA_ENTRADA_EN_VIGOR_CONTRATO")
	private LocalDateTime fechaEntradaEnVigorContrato;

	@Column(name = "ADJUDICATARIO_LICITACION")
	private String adjudicatarioLicitacion;

	@Column(name = "TIPO_IDENTIFICADOR_ADJUDICATARIO")
	private String tipoIdentificadorAdjudicatario;

	@Column(name = "IDENTIFICADOR_ADJUDICATARIO")
	private String identificadorAdjudicatario;

	@Column(name = "ADJUDICATARIO_ES_O_NO_PYME")
	private Boolean adjudicatarioEsONoPyme;

	@Column(name = "IMPORTE_ADJUDICACION_SIN_IMPUESTOS")
	private BigDecimal importeAdjudicacionSinImpuestos;

	@Column(name = "IMPORTE_ADJUDICACION_CON_IMPUESTOS")
	private BigDecimal importeAdjudicacionConImpuestos;

	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "FECHA_CREACION_REGISTRO_BBDD")
	private LocalDateTime fechaCreacionRegistroBBDD;

	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "FECHA_ACTUALIZACION_REGISTRO_BBDD")
	private LocalDateTime fechaActualizacionRegistroBBDD;
}
