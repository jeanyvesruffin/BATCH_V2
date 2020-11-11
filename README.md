# BATCH_V2

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
