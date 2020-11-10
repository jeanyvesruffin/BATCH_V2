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



