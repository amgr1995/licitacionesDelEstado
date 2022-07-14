package es.licitaciones.estado.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.dgpe.codice.common.caclib.CommodityClassificationType;
import org.dgpe.codice.common.caclib.PartyIdentificationType;

import es.licitaciones.estado.model.enums.Estado;
import es.licitaciones.estado.model.enums.PresentacionOferta;
import es.licitaciones.estado.model.enums.SistemaContratacion;
import es.licitaciones.estado.model.enums.TipoAdministracion;
import es.licitaciones.estado.model.enums.TipoContrato;
import es.licitaciones.estado.model.enums.TipoProcedimiento;
import es.licitaciones.estado.model.enums.Tramitacion;
import ext.place.codice.common.caclib.AdditionalPublicationDocumentReferenceType;
import ext.place.codice.common.caclib.AdditionalPublicationStatusType;
import ext.place.codice.common.caclib.ContractFolderStatusType;
import ext.place.codice.common.caclib.NoticeInfoType;

public enum DatosSeleccionables {
	PRIMERA_PUBLICACION("Primera publicación", EnumFormatos.FECHA_CORTA) {
		@Override
		public GregorianCalendar valorCodice(ContractFolderStatusType contractFolder) {
			GregorianCalendar primeraPublicacion = null;

			try {
				// Se recorren los validnoticeinfo
				for (NoticeInfoType noticeInfo : contractFolder.getValidNoticeInfo()) {
					try {
						// No se tiene en cuenta si es anuncio previo
						if (noticeInfo.getNoticeTypeCode().getValue().compareTo("DOC_PIN") != 0) {
							// Se recorren los medios de publicacion
							for (AdditionalPublicationStatusType additionalPublicationStatus : noticeInfo
									.getAdditionalPublicationStatus()) {
								// Se comprueba si el medio es el perfil de contratante
								if (additionalPublicationStatus.getPublicationMediaName().getValue()
										.equalsIgnoreCase("Perfil del Contratante")) {
									// Se obtiene la fecha m�s antigua
									for (AdditionalPublicationDocumentReferenceType additionalPublicationDocumentReference : additionalPublicationStatus
											.getAdditionalPublicationDocumentReference()) {
										GregorianCalendar fecha = additionalPublicationDocumentReference.getIssueDate()
												.getValue().toGregorianCalendar();
										if (primeraPublicacion == null
												|| primeraPublicacion.compareTo(fecha) == DatatypeConstants.GREATER) {
											primeraPublicacion = fecha;
										}
									}
								}
							}
						}
					} catch (Exception e) {
						// El ATOM cumple con el esquema, pero no con los requisito
					}
				}
				return primeraPublicacion;
			} catch (Exception e) {
				return null;
			}
		}
	},
	ESTADO("Estado") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
//				String estado = GenericodeTypes.ESTADO.getValue(contractFolder.getContractFolderStatusCode().getValue());

				Estado estado = Estado.valueOf(contractFolder.getContractFolderStatusCode().getValue());
				if (estado != null) {
					return estado.getNombre();
				}

			} catch (Exception e) {
			}
			return null;
		}
	},
	NUMERO_EXPEDIENTE("Número de expediente") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
				String numExpediente = contractFolder.getContractFolderID().getValue();
				return numExpediente;
			} catch (Exception e) {
				return null;
			}
		}
	},
	OBJETO_CONTRATO("Objeto del Contrato") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
				String objeto = contractFolder.getProcurementProject().getName().get(0).getValue();
				return objeto;
			} catch (Exception e) {
				return null;
			}
		}
	},
	VALOR_ESTIMADO("Valor estimado del contrato", EnumFormatos.MONEDA) {
		@Override
		public BigDecimal valorCodice(ContractFolderStatusType contractFolder) {
			try {
				BigDecimal valorEstimado = contractFolder.getProcurementProject().getBudgetAmount()
						.getEstimatedOverallContractAmount().getValue();
				return valorEstimado;
			} catch (Exception e) {
				return null;
			}

		}
	},
	PRESUPUESTO_BASE_SIN_IMPUESTOS("Presupuesto base sin impuestos", EnumFormatos.MONEDA) {
		@Override
		public BigDecimal valorCodice(ContractFolderStatusType contractFolder) {
			try {
				BigDecimal presupuestoConImpuestos = contractFolder.getProcurementProject().getBudgetAmount()
						.getTaxExclusiveAmount().getValue();
				return presupuestoConImpuestos;
			} catch (Exception e) {
				return null;
			}
		}
	},
	PRESUPUESTO_BASE_CON_IMPUESTOS("Presupuesto base con impuestos", EnumFormatos.MONEDA) {
		@Override
		public BigDecimal valorCodice(ContractFolderStatusType contractFolder) {
			try {
				BigDecimal presupuestoSinImpuestos = contractFolder.getProcurementProject().getBudgetAmount()
						.getTotalAmount().getValue();
				return presupuestoSinImpuestos;
			} catch (Exception e) {
				return null;
			}
		}
	},
	CPV("CPV") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			String valoresCPV = "";
			try {
				for (CommodityClassificationType commodity : contractFolder.getProcurementProject()
						.getRequiredCommodityClassification()) {
					valoresCPV += commodity.getItemClassificationCode().getValue() + SEPARADOR;
				}
				return valoresCPV;
			} catch (Exception e) {
				return valoresCPV;
			}
		}
	},
	TIPO_CONTRATO("Tipo de contrato") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
//				return GenericodeTypes.TIPO_CONTRATO.getValue(contractFolder.getProcurementProject().getTypeCode().getValue());

				return TipoContrato.retrieveByCode(contractFolder.getProcurementProject().getTypeCode().getValue());

			} catch (Exception e) {
			}
			return null;
		}
	},
	LUGAR_EJECUCION("Lugar de ejecución") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			String codigo = "";
			String descripcion = "";
			try {
				codigo = contractFolder.getProcurementProject().getRealizedLocation().getCountrySubentityCode()
						.getValue();
			} catch (Exception e) {
				codigo = "";
			}
			try {
				descripcion = contractFolder.getProcurementProject().getRealizedLocation().getCountrySubentity()
						.getValue();
			} catch (Exception e) {
				descripcion = "";
			}

			if (codigo == "" && descripcion == "") {
				// Se intenta obtener el codigo del pa�s
				try {
					codigo = contractFolder.getProcurementProject().getRealizedLocation().getAddress().getCountry()
							.getIdentificationCode().getValue();
				} catch (Exception e) {
					codigo = "";
				}
				try {
					descripcion = contractFolder.getProcurementProject().getRealizedLocation().getAddress().getCountry()
							.getName().getValue();
				} catch (Exception e) {
					descripcion = "";
				}

			}

			return codigo + " - " + descripcion;
		}
	},
	ORGANO_CONTRATACION("Órgano de Contratación") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
				return contractFolder.getLocatedContractingParty().getParty().getPartyName().get(0).getName()
						.getValue();
			} catch (Exception e) {
				return null;
			}
		}
	},
	ID_PLATAFORMA_OC("ID OC en PLACSP") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
				String idPlatOC = "";
				for (PartyIdentificationType partyIdentificationType : contractFolder.getLocatedContractingParty()
						.getParty().getPartyIdentification()) {
					if (partyIdentificationType.getID().getSchemeName().compareTo("ID_PLATAFORMA") == 0) {
						idPlatOC = partyIdentificationType.getID().getValue();
					}
				}
				return idPlatOC;
			} catch (Exception e) {
				return null;
			}
		}
	},
	NIF_OC("NIF OC") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
				String nif = "";
				for (PartyIdentificationType partyIdentificationType : contractFolder.getLocatedContractingParty()
						.getParty().getPartyIdentification()) {
					if (partyIdentificationType.getID().getSchemeName().compareTo("NIF") == 0) {
						nif = partyIdentificationType.getID().getValue();
					}
				}
				return nif;
			} catch (Exception e) {
				return null;
			}
		}
	},
	DIR3("DIR3") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
				String dir3 = "";
				for (PartyIdentificationType partyIdentificationType : contractFolder.getLocatedContractingParty()
						.getParty().getPartyIdentification()) {
					if (partyIdentificationType.getID().getSchemeName().compareTo("DIR3") == 0) {
						dir3 = partyIdentificationType.getID().getValue();
					}
				}
				return dir3;
			} catch (Exception e) {
				return null;
			}
		}
	},
	ENLACE_PERFIL_CONTRATANTE("Enlace al Perfil de Contratante del OC") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
				return contractFolder.getLocatedContractingParty().getBuyerProfileURIID().getValue();
			} catch (Exception e) {
				return null;
			}
		}
	},
	TIPO_ADMINISTRACION("Tipo de Administración") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
//				return GenericodeTypes.TIPO_ADMINISTRACION.getValue(contractFolder.getLocatedContractingParty().getContractingPartyTypeCode().getValue());

				return TipoAdministracion.retrieveByCode(
						contractFolder.getLocatedContractingParty().getContractingPartyTypeCode().getValue());

			} catch (Exception e) {
			}
			return null;
		}
	},
	CODIGO_POSTAL("Código Postal") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
				return contractFolder.getLocatedContractingParty().getParty().getPostalAddress().getPostalZone()
						.getValue();
			} catch (Exception e) {
				return null;
			}
		}
	},
	TIPO_PROCEDIMIENTO("Tipo de procedimiento") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
//				return GenericodeTypes.TIPO_PROCEDIMIENTO.getValue(contractFolder.getTenderingProcess().getProcedureCode().getValue());

				return TipoProcedimiento
						.retrieveByCode(contractFolder.getTenderingProcess().getProcedureCode().getValue());

			} catch (Exception e) {
			}
			return null;
		}
	},
	SISTEMA_CONTRATACION("Sistema de contratación") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
//				return GenericodeTypes.SISTEMA_CONTRATACION.getValue(contractFolder.getTenderingProcess().getContractingSystemCode().getValue());

				return SistemaContratacion
						.retrieveByCode(contractFolder.getTenderingProcess().getContractingSystemCode().getValue());

			} catch (Exception e) {
			}
			return null;
		}
	},
	TRAMITACION("Tramitación") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
//				return GenericodeTypes.TRAMITACION.getValue(contractFolder.getTenderingProcess().getUrgencyCode().getValue());

				return Tramitacion.retrieveByCode(contractFolder.getTenderingProcess().getUrgencyCode().getValue());

			} catch (Exception e) {
			}
			return null;
		}
	},
	PRESENTACION_OFERTA("Forma de presentación de la oferta") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
//				return GenericodeTypes.PRESENTACION_OFERTA.getValue(contractFolder.getTenderingProcess().getSubmissionMethodCode().getValue());

				return PresentacionOferta
						.retrieveByCode(contractFolder.getTenderingProcess().getSubmissionMethodCode().getValue());
				
			} catch (Exception e) {
			}
			return null;
		}
	},
	FECHA_PRESENTACION_OFERTAS("Fecha de presentación de ofertas", EnumFormatos.FECHA_LARGA) {
		@Override
		public GregorianCalendar valorCodice(ContractFolderStatusType contractFolder) {
			GregorianCalendar fechaFinal = new GregorianCalendar();
			try {
				// Se intenta recuper la fehca de fin de presentaci�n de ofertas
				XMLGregorianCalendar finPresentacionDia = contractFolder.getTenderingProcess()
						.getTenderSubmissionDeadlinePeriod().getEndDate().getValue();
				XMLGregorianCalendar finPresentacionHora = contractFolder.getTenderingProcess()
						.getTenderSubmissionDeadlinePeriod().getEndTime().getValue();

				LocalDate localDate = LocalDate.of(finPresentacionDia.getYear(), finPresentacionDia.getMonth(),
						finPresentacionDia.getDay());
				LocalTime localTime = LocalTime.of(finPresentacionHora.getHour(), finPresentacionHora.getMinute(),
						finPresentacionHora.getSecond());
				LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

				fechaFinal = GregorianCalendar.from(localDateTime.atZone(ZoneId.of("Europe/Paris")));

				return fechaFinal;
			} catch (Exception e) {
				return null;
			}
		}
	},
	FECHA_PRESENTACION_SOLICITUDES("Fecha de presentación de solicitudes de participacion", EnumFormatos.FECHA_LARGA) {
		@Override
		public GregorianCalendar valorCodice(ContractFolderStatusType contractFolder) {
			GregorianCalendar fechaFinal = new GregorianCalendar();
			try {
				// Se intenta recuperar la fecha de fin de presentaci�n de solicituddes
				XMLGregorianCalendar finPresentacionDia = contractFolder.getTenderingProcess()
						.getParticipationRequestReceptionPeriod().getEndDate().getValue();
				XMLGregorianCalendar finPresentacionHora = contractFolder.getTenderingProcess()
						.getParticipationRequestReceptionPeriod().getEndTime().getValue();

				LocalDate localDate = LocalDate.of(finPresentacionDia.getYear(), finPresentacionDia.getMonth(),
						finPresentacionDia.getDay());
				LocalTime localTime = LocalTime.of(finPresentacionHora.getHour(), finPresentacionHora.getMinute(),
						finPresentacionHora.getSecond());
				LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);

				fechaFinal = GregorianCalendar.from(localDateTime.atZone(ZoneId.of("Europe/Paris")));

				return fechaFinal;

			} catch (Exception e) {
				return null;
			}
		}
	},
	ES_REG_SARA("Directiva de aplicación") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
				return contractFolder.getTenderingTerms().getProcurementLegislationDocumentReference().getID()
						.getValue();
			} catch (Exception e) {
				return null;
			}
		}
	},
	SUBCONTRACION_PERMITIDA_DESC("Subcontratación permitida") {
		@Override
		public String valorCodice(ContractFolderStatusType contractFolder) {
			try {
				return contractFolder.getTenderingTerms().getAllowedSubcontractTerms().get(0).getDescription().get(0)
						.getValue();
			} catch (Exception e) {
				return null;
			}
		}
	},
	SUBCONTRACION_PERMITIDA_RATE("Subcontratación permitida porcentaje") {
		@Override
		public BigDecimal valorCodice(ContractFolderStatusType contractFolder) {
			try {
				return contractFolder.getTenderingTerms().getAllowedSubcontractTerms().get(0).getRate().getValue();
			} catch (Exception e) {
				return null;
			}
		}
	};

	private final static String SEPARADOR = ";";

	private final String titulo;
	private final EnumFormatos formato;

	DatosSeleccionables(String name, EnumFormatos format) {
		this.titulo = name;
		this.formato = format;
	}

	DatosSeleccionables(String name) {
		this.titulo = name;
		this.formato = EnumFormatos.TEXTO;
	}

	public String getTitulo() {
		return titulo;
	}

	public EnumFormatos getFormato() {
		return formato;
	}

	public abstract Object valorCodice(ContractFolderStatusType contractFolder);

}
