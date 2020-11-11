# BATCH_V2

# Prerecquis

Java version 15.0.1




# Spring batch ?

Spring Batch permet l'execution de traitement programmme appele job.

Il prend en entree un fichier csv, par exemple.

Puis, execute periodiquement un traitement sur ces donnees.

Pour enfin, ecrire en base de donnees ou declencher d'autre job.

# Exemple de scenarios

**Transformation de donnees**

* Processus comptable de fin de mois
* Processus quotidien de commande au détail des processus
* Journal horaire des transactions

**Integration system**

* Importation en masse de donnees externes
* Importation en masse de donnees entre les systemes internes
* exportation en masse de donnees


# Flux d'execution

* Donnees d'entrees sont structurees en XML ou JSON, proviennent generalements d'une file (Queue) de message.
* Le job, s'execute presque en temps reel pour traiter les messages entrants.
* La sortie est alors l'ecriture de donnees en base de donnee ou alors le declenchement d'un autre job.


## Avantages

* Gerer avec succes de gros volumes de donnees
* Peut arreter et redemarrer le travail a tout moment
* Approche mature

## Inconvenients

* Ensemble de donnees d'entree fini
* Peut avoir un impact sur le traitement en temps reel car tres gourmand en ressources systeme.
* Exagere pour des calculs plus simples

# Initialisation du projet

1. Creation d'un projet Maven

2. Ajout d'un fichier Readme et gitignore

3. Initialisation depot git

```cmd
cd [projet]
git init
git add .
git commit -am "init projet"
```

4. Creation d'un repository dans github

5. Faire le lien entre le repository local et Github

```cmd
git branch -M main
git remote add origin https://github.com/jeanyvesruffin/BATCH_V2.git
git push -u origin main
```

# Creation du projet Maven, pom.XML et choix des dependences utilisees

Nous utiliserons les dependences suivantes dans notre projet:
* **Jackson**: permettent de propose plusieurs approches pour travailler avec JSON, y compris l'utilisation d'annotations de liaison sur les classes POJO pour des cas d'utilisation simples.
* **Hibernate**: est un framework open source gerant la persistance des objets en base de donnees relationnelle.
* **Spring**: framework java.
* **HikariCP**: est un pool de connexions est un cache des connexions de base de donnees, maintenu afin que les connexions puissent etre reutilisees lorsque de futures demandes a la base de donnees sont requises
* **Commons-lang3**: fournit une foule d'utilitaires d'aide pour l'API java.lang, notamment les methodes de manipulation de chaines, les methodes numeriques de base, la reflexion d'objets, la concurrence, la creation et la serialisation et les proprietes systeme. En outre, il contient des ameliorations de base de java.util.Date et une serie d'utilitaires dedies a l'aide a la creation	de methodes, telles que hashCode, toString et equals.
* **Commons-io**: est une bibliotheque d'utilitaires pour aider au developpement de la fonctionnalite IO.
* **Javax.transaction-api**: L'API de transaction Java (JTA) specifie les interfaces Java standard entre un gestionnaire de transactions et les parties impliquees dans un systeme de transaction distribue: le gestionnaire de ressources, le serveur d'applications et les applications transactionnelles.
* **Liquibase**: est une solution open-source de gestion des changements de schema de base de donnees qui vous permet de gerer facilement les revisions de vos changements de base de donnees.
* **Assertj**: est une bibliotheque java fournissant un riche ensemble d'assertions, des messages d'erreur vraiment utiles, ameliore la lisibilite du code de test et est conçue pour etre tres facile a utiliser dans votre IDE prefere.
* **Junit-jupiter-api**: Contrairement aux versions precedentes de JUnit, JUnit 5 est compose de plusieurs modules differents issus de trois sous-projets differents.JUnit 5 = Plateforme JUnit + JUnit Jupiter + JUnit Vintage
* **Mockito**: permet la realisation de tests unitaire a l'aide de mock (objet simule)
* **hamcrest**: permet la realisation de tests unitaire a l'aide de matcher. C'est une bibliotheque de correspondance, qui peut etre combinee pour creer des expressions d'intention flexibles dans les tests.

Avec les versions suivantes:

	<properties>
		<jackson.version>2.12.0-rc1</jackson.version>
		<hibernate.version>6.0.0.Alpha6</hibernate.version>
		<hibernate.type>pom</hibernate.type>
		<spring.version>2.3.5.RELEASE</spring.version>
	</properties>

# Definition du projet

**Les besoins**: 

* creation application spring batch
* utilise un fichier .csv en entre
* alimente une base de donnees MySQL
* comporte des tests unitaires 

**SPRING BATCH**

![SPRING BATCH](https://spring.io/images/diagram-batch-5001274a87227c34b690542c45ca0c9d.svg)

Lors de la sequence d'execution du batch:

* la lecture de l'input sera sur un fichier csv contenant un certain nombre de champs, d'ont un champs "pays".
* Lors de la phase processing, une fonction permettra la creation de deux fichiers csv, representant pour l'un, les clients habitants en France et pour l'autre les cllietns habitant a l'etrange.


# Configuration du projet

## Configuration environnement spring

### ClientBatchLoaderApp.java

Cette classe sera le point d'entre de l'application Spring Boot est sera annote:

```java
@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationConfigurationProperties.class})
public class ClientBatchLoaderApp{

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ClientBatchLoaderApp.class);
	}
}

```

### Ajout de la configuration a l'aide du gestionnaire d'environnement Spring

1 . Ajout attribut environnement membre de la classe spring Environnement
2 . Creation constructeur avec le parametre environnement 
3 . Execution de l'application Spring

```java
@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationConfigurationProperties.class})
public class ClientBatchLoaderApp{
	
	private Environment environment; 
	
	public ClientBatchLoaderApp(Environment environment) {
		super();
		this.environment = environment;
	}
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ClientBatchLoaderApp.class);
		Environment environment =app.run(args).getEnvironment();
				
	}

}

```

4 . Definition du fichier de configuration necessaire au demarrage de l'application Spring Boot


```java
package config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="application", ignoreUnknownFields = false)
public class ApplicationProperties {

}
```

5 . Creation des fichiers de configuration d'environnement (fichier .yml) et bugFix H2


*/batch_v2/src/main/resources/config/application.yml*


```yml

spring:
    application:
        name: ClientBatchLoader
    profiles:Dspring.profiles.active` set in `JAVA_OPTS`
        active: #spring.profiles.active#
    jackson:
        serialization.write_dates_as_timestamps: false
    jpa:
        open-in-view: false
        hibernate:
            ddl-auto: none
            naming:
                physical-strategy: org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
                implicit-strategy: org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy

info:
    project:
        version: #project.version#

management:
  endpoints:
    enabled-by-default: true
    web:
      exposure:
        include: '*'

```


*/batch_v2/src/main/resources/config/application-dev.yml*

```yml

spring:
    profiles:
        active: dev
    devtools:
        restart:
            enabled: true
    jackson:
        serialization.indent_output: true
    datasource:
        type: com.zaxxer.hikari.HikariDataSource
        url: jdbc:h2:file:./h2db/db/clientbatchloader-dev;
        username: ClientBatchLoader
        password:
    h2:
        console:
            enabled: true
    jpa:
        database-platform: com.pluralsight.springbatch.patientbatchloader.config.FixedH2Dialect
        database: H2
        show-sql: true
        properties:
            hibernate.id.new_generator_mappings: true
            hibernate.cache.use_second_level_cache: false
            hibernate.cache.use_query_cache: false
            hibernate.generate_statistics: true
    liquibase:
        contexts: dev
        drop-first: true

server:
    port: 8080

```


*/batch_v2/src/main/resources/config/application-prod.yml*

```yml
logging:
    level:
        ROOT: INFO
        clientBatchLoader: INFO

server:
    port: 8080
    compression:
        enabled: true
        mime-types: text/html,text/xml,text/plain,text/css, application/javascript, application/json
        min-response-size: 1024

spring:
    devtools:
        restart:
            enabled: false
    datasource:
        type: com.zaxxer.hikari.HikariDataSource
        url: jdbc:h2:file:./h2db/db/clientbatchloader-prod;
        username: ClientBatchLoader
        password:
    h2:
        console:
            enabled: true
    jpa:
        database-platform: clientbatchloader.config.FixedH2Dialect
        database: H2
        show-sql: true
        properties:
            hibernate.id.new_generator_mappings: true
            hibernate.cache.use_second_level_cache: false
            hibernate.cache.use_query_cache: false
            hibernate.generate_statistics: true
    liquibase:
        contexts: prod

```

*/batch_v2/src/main/java/config/FixedH2Dialect.java*

```java
package config;

import java.sql.Types;

import org.hibernate.dialect.H2Dialect;

/**
 * Resout un probleme avec les types de colonnes dans H2, en particulier autour des nouvelles API de temps. 
 */
public class FixedH2Dialect extends H2Dialect{
	
	public FixedH2Dialect() {
		super();
		registerColumnType(Types.FLOAT, "real");
		registerColumnType(Types.BINARY, "varbinary");
	}

}
```

6 . Creation de la methode de selection de l'environnement à l'aide de la classes de constante

*/batch_v2/src/main/java/config/Constants.java*

```java
package config;


/**
 * 
 * Constantes de l'application
 *
 */
public class Constants {

	public static final String COMPTE_SYSTEME = "system";
	public static final String CLE_DEFAULT_LANG = "fr";
	public static final String PROFIL_DEVELOPPEMENT_SPRING = "dev";
	public static final String PROFIL_PRODUCTION_SPRING = "prod";
	public static final String PROFIL_NO_LIQUIBASE_SPRING = "no-liquibase";
	
	private Constants() {
		
	}
	
}
```


7 . Creation du fichier de configuration par default, initilisation de l'application et choix de l'environnement.

*/batch_v2/src/main/java/config/DefaultProfileUtil.java*

```java
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

	public static void addDefaultProfile(SpringApplication app) {
		Map<String, Object> defaultProperties = new HashMap<String, Object>();
		defaultProperties.put(PROFIL_DEFAULT_SPRING, Constants.PROFIL_DEVELOPPEMENT_SPRING);
		app.setDefaultProperties(defaultProperties);
	}

	public static String[] getActiveProfiles(Environment environment) {
		String[] profiles = environment.getActiveProfiles();
		if (profiles.length == 0) {
			return environment.getDefaultProfiles();
		}
		return profiles;
	}

}
```


*/batch_v2/src/main/java/clientBatchLoader/ClientBatchLoaderApp.java* 
 
```java
@PostConstruct
public void initApplication() {
	Collection<String> profilActif = Arrays.asList(environment.getActiveProfiles());
	if (profilActif.contains(Constants.PROFIL_DEVELOPPEMENT_SPRING)  && profilActif.contains(Constants.PROFIL_PRODUCTION_SPRING)) {
		log.error("Vous avez mal configuré votre application! Il ne doit pas fonctionner avec les profils «dev» et «prod» en même temps.");
	}
}
```

9 . Et enfin, allons creer une classe AppllicationWebXML necessaire au demarrage de l'application.

Il s'agit d'une classe Java d'assistance qui fournit une alternative à la création d'un fichier web.xml.
Cela ne sera appelé que lorsque l'application est déployée sur un conteneur de servlet comme Tomcat, JBoss, etc.


*/batch_v2/src/main/java/clientBatchLoader/ApplicationWebXML.java*

```java
package clientBatchLoader;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import config.DefaultProfileUtil;
public class ApplicationWebXML extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {

		DefaultProfileUtil.addDefaultProfile(application.application());
		return application.sources(ClientBatchLoaderApp.class);
	}
}

```



# Tricks et TIPS

ERROR:

```cmd
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/C:/Users/admin/.p2/pool/plugins/org.eclipse.m2e.maven.runtime.slf4j.simple_1.16.0.20200610-1735/jars/slf4j-simple-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [file:/C:/Users/admin/eclipse/jee-2020-09/eclipse/configuration/org.eclipse.osgi/5/0/.cp/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.SimpleLoggerFactory]
SLF4J: Class path contains multiple SLF4J bindings.
SLF4J: Found binding in [jar:file:/C:/Users/admin/.p2/pool/plugins/org.eclipse.m2e.maven.runtime.slf4j.simple_1.16.0.20200610-1735/jars/slf4j-simple-1.7.5.jar!/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: Found binding in [file:/C:/Users/admin/eclipse/jee-2020-09/eclipse/configuration/org.eclipse.osgi/5/0/.cp/org/slf4j/impl/StaticLoggerBinder.class]
SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
SLF4J: Actual binding is of type [org.slf4j.impl.SimpleLoggerFactory]
```

Solve:

1 . Rechercher les Jar en conflit:

```cmd
mvn dependency:tree
```

Plan B qui fonctionne car le problemee viens de l'IDE

https://stackoverflow.com/questions/63755390/multiple-slf4j-bindings-with-m2e-in-eclipse-2020-06?noredirect=1#comment112749280_63755390
