package test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import asw.Application;
import asw.dbManagement.GetParticipant;
import asw.dbManagement.model.Agent;
import asw.participants.webService.request.PeticionChangeEmailREST;
import asw.participants.webService.request.PeticionChangePasswordREST;
import asw.participants.webService.request.PeticionInfoREST;

@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainTest {

	@Value("${local.server.port}")
	private int port;

	private URL base;
	private RestTemplate template;

	@Autowired
	private GetParticipant getParticipant;

	@Before
	public void setUp() throws Exception {
		this.base = new URL("http://localhost:" + port + "/");
		template = new TestRestTemplate();
	}

	@Test
	public void t1domainModelEqualsTest() {
		Agent participant1 = getParticipant.getParticipant("12345678B");
		Agent participant2 = getParticipant.getParticipant("12345678555B");
		Agent participant3 = getParticipant.getParticipant("12345678B");
		Agent participant4 = getParticipant.getParticipant("87654321B");
		assertFalse(participant1.equals(participant2));
		assertFalse(participant1.equals(4));
		assertTrue(participant1.equals(participant3));
		assertTrue(participant1.equals(participant1));
		assertFalse(participant1.equals(participant4));
	}

	@Test
	public void t2domainModelToString() {
		Agent participant1 = getParticipant.getParticipant("12345678B");
		assertEquals(participant1.toString(),
				"Agent [id=" + participant1.getId() + ", nombre=" + participant1.getNombre() + ", password=" + participant1.getPassword()
				+ ", email=" + participant1.getEmail()
				+ ", identificador=" + participant1.getIdentificador() + ", direccion=" + participant1.getDireccion() 
				+ ", kind=" + participant1.getKind() + ", kindCode="
				+ participant1.getKindCode() + "]");
	}

	@Test
	public void t3domainModelHashCodeTest() {
		Agent participant1 = getParticipant.getParticipant("12345678B");
		Agent participant3 = getParticipant.getParticipant("12345678B");
		assertEquals(participant1.hashCode(), participant3.hashCode());
	}
	
	@Test
	public void t4participantExistAndCorrectPasssword() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/user";

		response = template.postForEntity(userURI, new PeticionInfoREST("12345678B", "123456", "Person"), String.class);
		assertThat(response.getBody(), equalTo(
				"{\"firstName\":\"Paco Gomez\",\"email\":\"pac@hotmail.com\",\"direccion\":\"Calle Uria\",\"kind\":\"Person\",\"kindCode\":1,\"id\":\"12345678B\"}"));

		response = template.postForEntity(userURI, new PeticionInfoREST("87654321B", "123456", "Person"), String.class);
		assertThat(response.getBody(), equalTo(
				"{\"firstName\":\"Pepe Fernandez\",\"email\":\"pepe@hotmail.com\",\"direccion\":\"Calle Principal\",\"kind\":\"Person\",\"kindCode\":1,\"id\":\"87654321B\"}"));
	
		response = template.postForEntity(userURI, new PeticionInfoREST("11223344C", "123456", "Person"), String.class);
		assertThat(response.getBody(), equalTo(
				"{\"firstName\":\"Carmen Lopez\",\"email\":\"carmen@yahoo.com\",\"direccion\":\"Calle Calvo Sotelo\",\"kind\":\"Person\",\"kindCode\":1,\"id\":\"11223344C\"}"));
		
	}

	@Test
	public void t5participantDoNotExist() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/user";
		String userNotFound = "{\"reason\": \"User not found\"}";

		response = template.postForEntity(userURI, new PeticionInfoREST("136543654E", "ajksdkje", "Person"), String.class);
		assertThat(response.getBody(), equalTo(userNotFound));

		response = template.postForEntity(userURI, new PeticionInfoREST("66666666X", "shcxhqw", "Person"), String.class);
		assertThat(response.getBody(), equalTo(userNotFound));
	}

	@Test
	public void t6incorrectPassword() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/user";
		String incorrectPassword = "{\"reason\": \"Password do not match\"}";
		response = template.postForEntity(userURI, new PeticionInfoREST("12345678B", "1235222226","Person"), String.class);
		assertThat(response.getBody(), equalTo(incorrectPassword));

		response = template.postForEntity(userURI, new PeticionInfoREST("87654321B", "12965346", "Person"), String.class);
		assertThat(response.getBody(), equalTo(incorrectPassword));

		response = template.postForEntity(userURI, new PeticionInfoREST("11223344C", "134ddd56","Person"), String.class);
		assertThat(response.getBody(), equalTo(incorrectPassword));

		response = template.postForEntity(userURI, new PeticionInfoREST("22334455D", "234522222226","Person"), String.class);
		assertThat(response.getBody(), equalTo(incorrectPassword));
	}

	@Test
	public void t7emptyUser() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/user";
		String emptyUser = "{\"reason\": \"Required user id\"}";
		response = template.postForEntity(userURI, new PeticionInfoREST("", "1235222226","Person"), String.class);
		assertThat(response.getBody(), equalTo(emptyUser));

		response = template.postForEntity(userURI,new PeticionInfoREST("", "226","Person"), String.class);
		assertThat(response.getBody(), equalTo(emptyUser));

		response = template.postForEntity(userURI, new PeticionInfoREST("", "1","Person"), String.class);
		assertThat(response.getBody(), equalTo(emptyUser));

		response = template.postForEntity(userURI, new PeticionInfoREST("", "hhh","Person"), String.class);
		assertThat(response.getBody(), equalTo(emptyUser));
	}
/*	
	//Ya no se usa el email como usuario, se usa el DNI (Identificador)
	@Test
	public void T8invalidEmail() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/user";
		String wrongEmailStyle = "{\"reason\": \"Wrong mail style\"}";
		response = template.postForEntity(userURI, new PeticionInfoREST("ajsjc", ""), String.class);
		assertThat(response.getBody(), equalTo(wrongEmailStyle));
<<<<<<< HEAD

		response = template.postForEntity(userURI, new PeticionInfoREST("jxjsjd@", ""), String.class);
		assertThat(response.getBody(), equalTo(wrongEmailStyle));

		response = template.postForEntity(userURI, new PeticionInfoREST("chdgetc@chhsy", ""), String.class);
		assertThat(response.getBody(), equalTo(wrongEmailStyle));

=======

		response = template.postForEntity(userURI, new PeticionInfoREST("jxjsjd@", ""), String.class);
		assertThat(response.getBody(), equalTo(wrongEmailStyle));

		response = template.postForEntity(userURI, new PeticionInfoREST("chdgetc@chhsy", ""), String.class);
		assertThat(response.getBody(), equalTo(wrongEmailStyle));

>>>>>>> parent of 2fdc892... Arreglado algún error menor en los test y cambiado Participants por
		response = template.postForEntity(userURI, new PeticionInfoREST("sjhwuwsc", ""), String.class);
		assertThat(response.getBody(), equalTo(wrongEmailStyle));
	}*/

	@Test
	public void t9emptyPassword() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/user";
		String emptyPassword = "{\"reason\": \"User password is required\"}";

		response = template.postForEntity(userURI, new PeticionInfoREST("12345678B", "","Person"), String.class);
		assertThat(response.getBody(), equalTo(emptyPassword));

		response = template.postForEntity(userURI, new PeticionInfoREST("87654321B", "", "Person"), String.class);
		assertThat(response.getBody(), equalTo(emptyPassword));

		response = template.postForEntity(userURI, new PeticionInfoREST("11223344C", "","Person"), String.class);
		assertThat(response.getBody(), equalTo(emptyPassword));

		response = template.postForEntity(userURI, new PeticionInfoREST("22334455D", "","Person"), String.class);
		assertThat(response.getBody(), equalTo(emptyPassword));
	}
	
	
	@Test
	public void tEmptyKind() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/user";
		String emptyKind = "{\"reason\": \"Kind required\"}";

		response = template.postForEntity(userURI, new PeticionInfoREST("12345678B", "123456", ""), String.class);
		assertThat(response.getBody(), equalTo(emptyKind));

		response = template.postForEntity(userURI, new PeticionInfoREST("87654321B", "123456", ""), String.class);
		assertThat(response.getBody(), equalTo(emptyKind));

		response = template.postForEntity(userURI, new PeticionInfoREST("11223344C", "123456",""), String.class);
		assertThat(response.getBody(), equalTo(emptyKind));

		response = template.postForEntity(userURI, new PeticionInfoREST("22334455D", "123456",""), String.class);
		assertThat(response.getBody(), equalTo(emptyKind));
	}
	

	@Test
	public void t10emailRequiredChange() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changeEmail";
		String emptyEmail = "{\"reason\": \"User email is required\"}";

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("", "", ""), String.class);
		assertThat(response.getBody(), equalTo(emptyEmail));

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("	", "", ""), String.class);
		assertThat(response.getBody(), equalTo(emptyEmail));

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("", "", "shfhs"), String.class);
		assertThat(response.getBody(), equalTo(emptyEmail));
	}

	@Test
	public void t12newEmailRequiredChange() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changeEmail";
		String emptyEmail = "{\"reason\": \"User email is required\"}";

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("paco@hotmail.com", "", ""), String.class);
		assertThat(response.getBody(), equalTo(emptyEmail));

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("paco@hotmail.com", "", "   "),
				String.class);
		assertThat(response.getBody(), equalTo(emptyEmail));

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("paco@hotmail.com", "", "	"),
				String.class);
		assertThat(response.getBody(), equalTo(emptyEmail));
	}

	@Test
	public void t13invalidEmailChange() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changeEmail";
		String wrongEmailStyle = "{\"reason\": \"Wrong mail style\"}";

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("paco", "", ""), String.class);
		assertThat(response.getBody(), equalTo(wrongEmailStyle));

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("paco@", "", "   "), String.class);
		assertThat(response.getBody(), equalTo(wrongEmailStyle));

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("paco@hotmail", "  ", "	"), String.class);
		assertThat(response.getBody(), equalTo(wrongEmailStyle));
	}

	@Test
	public void t14newInvalidEmailChange() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changeEmail";
		String wrongEmailStyle = "{\"reason\": \"Wrong mail style\"}";

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("paco@hotmail.com", "", "xhhuwi"),
				String.class);
		assertThat(response.getBody(), equalTo(wrongEmailStyle));

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("paco@hotmail.com", "", "fhgythf@"),
				String.class);
		assertThat(response.getBody(), equalTo(wrongEmailStyle));

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("paco@hotmail.com", "", "fhfyg@hotmail"),
				String.class);
		assertThat(response.getBody(), equalTo(wrongEmailStyle));
	}

	@Test
	public void t15emailChangeUserNotFound() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changeEmail";
		String userNotFound = "{\"reason\": \"User not found\"}";

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("pao@hotmail.com", "123456", "pac@hotmail.com"),
				String.class);
		assertThat(response.getBody(), equalTo(userNotFound));

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("pee@gmail.com", "123456", "pepe@hotmail.com"),
				String.class);
		assertThat(response.getBody(), equalTo(userNotFound));

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("pa@hotmail.com", "123456", "fhfyg@hotmail.com"),
				String.class);
		assertThat(response.getBody(), equalTo(userNotFound));
	}

	@Test
	public void t16sameEmailErrorChange() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changeEmail";
		String sameEmail = "{\"reason\": \"Same email\"}";

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("paco@hotmail.com", "", "paco@hotmail.com"),
				String.class);
		assertThat(response.getBody(), equalTo(sameEmail));

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("pepe@gmail.com", "", "pepe@gmail.com"),
				String.class);
		assertThat(response.getBody(), equalTo(sameEmail));

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("carmen@yahoo.com", "", "carmen@yahoo.com"),
				String.class);
		assertThat(response.getBody(), equalTo(sameEmail));
	}

	@Test
	public void t17emailRequiredPasswordChange() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changePassword";
		String emptyEmail = "{\"reason\": \"User email is required\"}";

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("", "", ""), String.class);
		assertThat(response.getBody(), equalTo(emptyEmail));

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("	", "chsh", ""), String.class);
		assertThat(response.getBody(), equalTo(emptyEmail));

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("", "dfhe", "dhdgx"), String.class);
		assertThat(response.getBody(), equalTo(emptyEmail));
	}

	@Test
	public void t18inValidRequiredPasswordChange() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changePassword";
		String wrongEmailStyle = "{\"reason\": \"Wrong mail style\"}";

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("shdgr", "", ""), String.class);
		assertThat(response.getBody(), equalTo(wrongEmailStyle));

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("shdgr@", "", ""), String.class);
		assertThat(response.getBody(), equalTo(wrongEmailStyle));

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("shdgr@hotmail", "", ""),
				String.class);
		assertThat(response.getBody(), equalTo(wrongEmailStyle));
	}

	@Test
	public void t19passwordRequiredPasswordChange() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changePassword";
		String passwordRequired = "{\"reason\": \"User password is required\"}";

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("paco@hotmail.com", "", ""),
				String.class);
		assertThat(response.getBody(), equalTo(passwordRequired));

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("paco@hotmail.com", "", "dkdddd"),
				String.class);
		assertThat(response.getBody(), equalTo(passwordRequired));

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("paco@hotmail.com", "", "dkejd"),
				String.class);
		assertThat(response.getBody(), equalTo(passwordRequired));
	}

	@Test
	public void t20newPasswordRequiredPasswordChange() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changePassword";
		String passwordRequired = "{\"reason\": \"User password is required\"}";

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("paco@hotmail.com", "djfhr", ""),
				String.class);
		assertThat(response.getBody(), equalTo(passwordRequired));

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("paco@hotmail.com", "djvhrhc", ""),
				String.class);
		assertThat(response.getBody(), equalTo(passwordRequired));

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("paco@hotmail.com", "dkejd", ""),
				String.class);
		assertThat(response.getBody(), equalTo(passwordRequired));
	}

	@Test
	public void t21samePasswordChange() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changePassword";
		String passwordRequired = "{\"reason\": \"Password Incorrect\"}";

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("paco@hotmail.com", "djfhr", "djfhr"),
				String.class);
		assertThat(response.getBody(), equalTo(passwordRequired));

		response = template.postForEntity(userURI,
				new PeticionChangePasswordREST("paco@hotmail.com", "djvhrhc", "djvhrhc"), String.class);
		assertThat(response.getBody(), equalTo(passwordRequired));

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("paco@hotmail.com", "dkejd", "dkejd"),
				String.class);
		assertThat(response.getBody(), equalTo(passwordRequired));
	}

	@Test
	public void t22notFoundParticipantPasswordChange() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changePassword";
		String userNotFound = "{\"reason\": \"User not found\"}";

		response = template.postForEntity(userURI,
				new PeticionChangePasswordREST("pac@hotmail.com", "djfhr", "djfhrtt"), String.class);
		assertThat(response.getBody(), equalTo(userNotFound));

		response = template.postForEntity(userURI,
				new PeticionChangePasswordREST("martin@hotmail.com", "djvhrhc", "tt"), String.class);
		assertThat(response.getBody(), equalTo(userNotFound));

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("juan@hotmail.com", "dkejd", "tt"),
				String.class);
		assertThat(response.getBody(), equalTo(userNotFound));
	}

	@Test
	public void t23notSamePasswordChange() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changePassword";
		String passwordIncorrect = "{\"reason\": \"Password Incorrect\"}";

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("paco@hotmail.com", "djfhr", "djfhr"),
				String.class);
		assertThat(response.getBody(), equalTo(passwordIncorrect));

		response = template.postForEntity(userURI,
				new PeticionChangePasswordREST("pepe@gmail.com", "djvhrhc", "djvhrhc"), String.class);
		assertThat(response.getBody(), equalTo(passwordIncorrect));

		response = template.postForEntity(userURI, new PeticionChangePasswordREST("carmen@yahoo.com", "dkejd", "dkejd"),
				String.class);
		assertThat(response.getBody(), equalTo(passwordIncorrect));
	}
	
	@Test
	public void emailChangeCorrect() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changeEmail";
		
		String correctChange = "{\"participant\":\"pac@hotmail.com\",\"message\":\"email actualizado correctamente\"}";
		response = template.postForEntity(userURI, new PeticionChangeEmailREST("12345678B", "paco@hotmail.com", "123456", "pac@hotmail.com"),String.class);
		assertThat(response.getBody(), equalTo(correctChange));

		correctChange = "{\"participant\":\"pepe@hotmail.com\",\"message\":\"email actualizado correctamente\"}";
		response = template.postForEntity(userURI, new PeticionChangeEmailREST("87654321B","pepe@gmail.com", "123456", "pepe@hotmail.com"),String.class);
		assertThat(response.getBody(), equalTo(correctChange));

		correctChange = "{\"participant\":\"fhfyg@hotmail.com\",\"message\":\"email actualizado correctamente\"}";
		response = template.postForEntity(userURI, new PeticionChangeEmailREST("11223344C","carmen@yahoo.com", "123456", "fhfyg@hotmail.com"),String.class);
		assertThat(response.getBody(), equalTo(correctChange));
	}
	
	@Test
	public void correctPasswordChange() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changePassword";
		String correctPassword = "{\"participant\":\"isabel@gmail.com\",\"message\":\"contraseña actualizada correctamente\"}";

		response = template.postForEntity(userURI,
				new PeticionChangePasswordREST("22334455D", "isabel@gmail.com", "123456", "djfhr"), String.class);
		assertThat(response.getBody(), equalTo(correctPassword));
	}
	
	@Test
	public void correctPasswordChangeXML() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changePassword";
		String correctChange = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ "<ChangeInfoResponse><message>contraseÃ±a actualizada correctamente</message>"
				+ "<participant>isabel@gmail.com</participant></ChangeInfoResponse>";

		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		interceptors.add(new AcceptInterceptor());

		template.setInterceptors(interceptors);

		response = template.postForEntity(userURI,
				new PeticionChangePasswordREST("22334455D", "isabel@gmail.com", "djfhr", "123456"), String.class);
		assertThat(response.getBody(), equalTo(correctChange));
	}
	
	@Test
	public void emailChangeCorrectXML() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String userURI = base.toString() + "/changeEmail";
		String correctChange = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
				+ "<ChangeInfoResponse><message>email actualizado correctamente</message>"
				+ "<participant>carmen@yahoo.com</participant></ChangeInfoResponse>";

		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		interceptors.add(new AcceptInterceptor());

		template.setInterceptors(interceptors);

		response = template.postForEntity(userURI, new PeticionChangeEmailREST("11223344C","fhfyg@hotmail.com", "123456", "carmen@yahoo.com"),
				String.class);
		assertThat(response.getBody(), equalTo(correctChange));
	}

	// Cabecera HTTP para pedir respuesta en XML
	public class AcceptInterceptor implements ClientHttpRequestInterceptor {
		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
				throws IOException {
			HttpHeaders headers = request.getHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_XML));
			return execution.execute(request, body);
		}
	}
}
