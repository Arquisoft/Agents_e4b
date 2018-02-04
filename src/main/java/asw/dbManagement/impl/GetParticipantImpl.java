package asw.dbManagement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import asw.dbManagement.GetParticipant;
import asw.dbManagement.model.Agent;
import asw.dbManagement.repository.ParticipantRepository;


@Service
public class GetParticipantImpl implements GetParticipant {
	
	private ParticipantRepository repository;
	
	@Autowired
	public GetParticipantImpl(ParticipantRepository repository) {
		this.repository = repository;
	}
	
	
	/**
	 * Método que devuelve el Participante buscado por email
	 * Hace uso del método findByEmail (mapeador)
	 */
	@Override
	public Agent getParticipant(String email) {
		
		return this.repository.findByEmail(email);
	}

}
