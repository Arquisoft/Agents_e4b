package asw.dbManagement;

import asw.dbManagement.model.Agent;

public interface UpdateInfo {
	/**
	 * Permite la solicitud de cambio de contraseña
	 */
	public void updatePassword(Agent participant, String password, String newPassword);
	
	public void updateEmail(Agent participant, String email);
}
