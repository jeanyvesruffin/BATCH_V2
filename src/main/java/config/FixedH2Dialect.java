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
