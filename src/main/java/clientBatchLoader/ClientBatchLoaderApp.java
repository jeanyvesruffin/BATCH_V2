package clientBatchLoader;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.context.properties.ConfigurationPropertiesReportEndpoint.ApplicationConfigurationProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import config.Constants;
import config.DefaultProfileUtil;

/**
 * 
 * Classe principal d'execution de l'application Spring Boot
 *
 */
@SpringBootApplication
@EnableConfigurationProperties({ LiquibaseProperties.class, ApplicationConfigurationProperties.class })
public class ClientBatchLoaderApp {

	private Environment environment;
	private static final Logger log = LoggerFactory.getLogger(ClientBatchLoaderApp.class);

	public ClientBatchLoaderApp(Environment environment) {
		super();
		this.environment = environment;
	}

	/**
	 * 
	 * @param args
	 * 				arguments de ligne de commande
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ClientBatchLoaderApp.class);
		DefaultProfileUtil.addDefaultProfile(app);
		Environment environment =app.run(args).getEnvironment();
		log.info("\n----------------------------------------------------------\n\t"
				+ "Spring Batch Application '{}' is running!"
				+ "Profile(s): \t{}\n----------------------------------------------------------",
				environment.getProperty("spring.application.name"), environment.getActiveProfiles());
				
	}

	@PostConstruct
	public void initApplication() {
		Collection<String> profilActif = Arrays.asList(environment.getActiveProfiles());
		if (profilActif.contains(Constants.PROFIL_DEVELOPPEMENT_SPRING)  && profilActif.contains(Constants.PROFIL_PRODUCTION_SPRING)) {
			log.error("Vous avez mal configuré votre application! Il ne doit pas fonctionner avec les profils «dev» et «prod» en même temps.");
		}
	}
	
}
