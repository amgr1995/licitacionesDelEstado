package es.licitaciones.estado.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.licitaciones.estado.model.entity.ResultadoLicitaciones;

public interface ResultadoLicitacionesRepository extends JpaRepository<ResultadoLicitaciones, Long> {

	Optional<ResultadoLicitaciones> findByNumeroExpediente(String numExpediente);
}
