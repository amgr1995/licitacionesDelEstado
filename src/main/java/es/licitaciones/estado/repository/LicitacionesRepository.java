package es.licitaciones.estado.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.licitaciones.estado.model.entity.Licitaciones;

public interface LicitacionesRepository extends JpaRepository<Licitaciones, Long> {

	Optional<Licitaciones> findByNumeroExpediente(String numExpediente);
}
