package asw.participants.webService.request;

public class PeticionInfoREST {

	private String login;
	private String password;
	private String tipoAgente;

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PeticionInfoREST() {

	}
	
	public PeticionInfoREST(String login,String password){
		this.login = login;
		this.password = password;
	}
	
	public PeticionInfoREST(String login, String password, String tipoAgente){
		this.login = login;
		this.password = password;
		this.tipoAgente = tipoAgente;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}
	
	public String getTipoAgente(){
		return tipoAgente;
	}

}
