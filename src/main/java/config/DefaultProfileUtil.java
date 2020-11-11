package config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.core.env.Environment;

/**
 * 
 * Classe utilitaire pour charger un profil Spring à utiliser par défaut
 * lorsqu'il n'y a pas de spring.profiles.active défini dans l'environnement ou
 * pas d'argument de ligne de commande. Si la valeur n'est pas disponible dans
 * application.yml, le profil dev sera utilisé par défaut.
 *
 */
public class DefaultProfileUtil {

	private static final String PROFIL_DEFAULT_SPRING = "spring.profiles.default";

	private DefaultProfileUtil() {

	}

	/**
	 * 
	 * Définissez une valeur par défaut à utiliser lorsqu'aucun profil n'est
	 * configuré.
	 */

	public static void addDefaultProfile(SpringApplication app) {
		Map<String, Object> defaultProperties = new HashMap<String, Object>();
		defaultProperties.put(PROFIL_DEFAULT_SPRING, Constants.PROFIL_DEVELOPPEMENT_SPRING);
		app.setDefaultProperties(defaultProperties);
	}
	/**
	 * Retourne les profils appliqués sinon retourne les profils par défaut.
	 * @param environment
	 * @return profiles
	 */
	public static String[] getActiveProfiles(Environment environment) {
		String[] profiles = environment.getActiveProfiles();
		if (profiles.length == 0) {
			return environment.getDefaultProfiles();
		}
		return profiles;
	}

}
