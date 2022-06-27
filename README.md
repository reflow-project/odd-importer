# odd-importer
the importer component of the ODD, which serves the purpose to address, the ReflowOS-GraphQL-API of the different Pilot´s in order to import their data

# installation guide with docker

- mvn clean package

- docker build -t odd_importer .


_it is highly recommended to only deploy the odd-importer in combination with the other ODD-components. The respective docker-compose.yml for this purpose can be found in a separate repository._