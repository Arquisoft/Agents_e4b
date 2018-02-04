package asw.dbManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import asw.dbManagement.model.Agent;

@Repository
public interface ParticipantRepository extends JpaRepository<Agent, Long> {
	
	/**
	 * Método que devuelve el Participante el cual es buscado por email
	 * en la base de datos
	 * @param email del Partipante
	 * @return El Participante con dicho email
	 */
	public Agent findByEmail(String email);
	
}
