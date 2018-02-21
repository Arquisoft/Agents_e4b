package asw.dbManagement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import asw.dbManagement.GetParticipant;
import asw.dbManagement.model.Agent;
import asw.dbManagement.repository.AgentsRepository;


@Service
public class GetParticipantImpl implements GetParticipant {
	
	private AgentsRepository repository;
	
	@Autowired
	public GetParticipantImpl(AgentsRepository repository) {
		this.repository = repository;
	}
	
	
	/**
	 * Método que devuelve el Agente buscado por identificador
	 * Hace uso del método findByIdentificador (mapeador)
	 */
	@Override
	public Agent getParticipant(String identificador) {
		
		return this.repository.findByIdentificador(identificador);
	}

}
