package asw.dbManagement.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Participant")
public class Agent {

	// Id generado automÃ¡ticamente para diferenciar cada uno (para mapear)
	@Id
	@GeneratedValue
	private Long id;

	// Atributos del participante
	private String nombre;
	private String apellidos;
	private String password;
	private Date fechaNacimiento;
	@Column(unique = true, nullable = false)
	private String email;
	@Column(unique = true)
	private String identificador;
	private String direccion;
	
	private String kind;
	private int kindCode;


	/**
	 * Constructor vacio (ya que es para mapear)
	 */
	Agent() {
	}

	/**
	 * Constructor año pasado en el se construyen agentes de tipo persona, no tienen el campo tipo ni codigo de tipo
	 * 
	 * @param nombre
	 * @param apellidos
	 * @param password
	 * @param fechaNacimiento
	 * @param email
	 * @param dNI
	 * @param direccion
	 * @param nacionalidad
	 */
//	public Agent(String nombre, String apellidos, String password, Date fechaNacimiento, String email, String dNI,
//			String direccion, String nacionalidad, boolean isAdmin, boolean isPolitician) {
//		super();
//		this.nombre = nombre;
//		this.apellidos = apellidos;
//		this.password = password;
//		this.fechaNacimiento = fechaNacimiento;
//		this.email = email;
//		this.identificador = dNI;
//		this.direccion = direccion;
//		this.nacionalidad = nacionalidad;
//		this.isAdmin = isAdmin;
//		this.isPolitician = isPolitician;
//	}
	/**
	 * 
	 * @param nombre
	 * @param apellidos
	 * @param password
	 * @param email
	 * @param ID tambien puede tomarse como el DNI para las personas
	 * @param direccion
	 * @param kind
	 * @param kindCode
	 */
	public Agent(String nombre, String password, String email, String identificador,
			String direccion, String kind, int kindCode) {
		super();
		this.nombre = nombre; //El nombre va en conjunto con los apellidos
		this.password = password;
		this.email = email;
		this.identificador = identificador;
		this.direccion = direccion;
		this.kindCode = kindCode;
		this.kind = kind;
	}

	public Long getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdentificador() {
		return identificador;
	}

	public String getDireccion() {
		return direccion;
	}


	public String getKind() {
		return kind;
	}

	public int getKindCode() {
		return kindCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Agent other = (Agent) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Agent [id=" + id + ", nombre=" + nombre + ", password=" + password + ", email=" + email
				+ ", identificador=" + identificador + ", direccion=" + direccion + ", kind=" + kind + ", kindCode="
				+ kindCode + "]";
	}


}
