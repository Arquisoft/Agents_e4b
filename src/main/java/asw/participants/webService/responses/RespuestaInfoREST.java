package asw.participants.webService.responses;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import asw.dbManagement.model.Agent;

@XmlRootElement(name = "agent")
public class RespuestaInfoREST {	
	
	private String firstName;
	private String ID;
	private String email;
	private String direccion;
	private String kind;
	private int kindCode;
	
	
	
	public RespuestaInfoREST() {}
	
	public RespuestaInfoREST(Agent agent){
		setFirstName(agent.getNombre());
		setID(agent.getIdentificador());
		setEmail(agent.getEmail());
		setDireccion(agent.getDireccion());
		setKind(agent.getKind());
		setKindCode(agent.getKindCode());
	}
	
	
	public String getFirstName() {
		return firstName;
	}

	@XmlElement
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getID() {
		return ID;
	}

	@XmlElement
	public void setID(String iD) {
		ID = iD;
	}

	public String getEmail() {
		return email;
	}

	@XmlElement
	public void setEmail(String email) {
		this.email = email;
	}

	public String getDireccion() {
		return direccion;
	}
	
	@XmlElement
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getKind() {
		return kind;
	}
	
	@XmlElement
	public void setKind(String kind) {
		this.kind = kind;
	}

	public int getKindCode() {
		return kindCode;
	}
	
	@XmlElement
	public void setKindCode(int kindCode) {
		this.kindCode = kindCode;
	}

}
