package asw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import asw.dbManagement.model.Agent;
import asw.dbManagement.repository.AgentsRepository;

import java.text.ParseException;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public CommandLineRunner initDB(AgentsRepository repository) throws ParseException {

		return (args) -> {

			// Insercion en la base de datos
			repository.save(new Agent("Paco Gomez", "123456", "paco@hotmail.com", "12345678B", "Calle Uria", "Person", 1));

			// Insercion en la base de datos
			repository.save(new Agent("Pepe Fernandez", "123456", "pepe@gmail.com", "87654321B", "Calle Principal", "Person", 1));
		

			// Insercion en la base de datos
			repository.save(new Agent("Carmen Lopez", "123456", "carmen@yahoo.com", "11223344C", "Calle Calvo Sotelo", "Person", 1));



			// Insercion en la base de datos
			repository.save(new Agent("Isabel Rodriguez", "123456", "isabel@gmail.com", "22334455D", "Avenida Galicia", "Person", 1));




			// Insercion en la base de datos
			repository.save(new Agent("Marta Sanchez", "123456", "maria@gmai.com", "33445566E", "Avenida Santander", "Person", 1));

		


			// Insercion en la base de datos
			repository.save(new Agent("Jose Ballesteros", "123456", "jose@gmail.com", "44556677F", "Calle Asturias", "Person", 1));

		
		};
	}
}