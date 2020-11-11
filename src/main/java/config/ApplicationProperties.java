package config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * Proprietes specifiques du Client Batch Loader
 * 
 * Les proprietets sont configures dans les fichiers application.yml.
 *
 */


@ConfigurationProperties(prefix="application", ignoreUnknownFields = false)
public class ApplicationProperties {

}
