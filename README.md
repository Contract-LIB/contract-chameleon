# contract-chameleon

A translation tool for `Contract-LIB`.

## Executing the tool

The tool with all default adapters can be run with:

```sh
java -jar contract-chameleon-exe.jar
```

## Add own Adapters

Place the `JAR` of the additional adapter next to the `contract-chameleon-exe.jar`

```sh
java -cp '*' org.contract_lib.ContractChameleon # load all available classpaths from this position
```

The additional `JAR` must have a file with the name `org.contract_lib.contract_chameleon.Adapter`
 in `src/main/ressources/META-INF/services`
containing the full class name (including package) of the additional adapter.
Compare the following [example](./contract-chameleon.adapter.key.import/).

## Developing the tool

- Cloning the git directory

```sh
git clone git@github.com:Contract-LIB/contract-chameleon.git
```

- Install git submodules

```sh
cd contract-chameleon
git submodule update --init --recursive # to fetch and initialize the submodules the first time 
git pull --rebase --recurse-submodules # to pull changes from submodules
```

- Publish custom JML-Parser

```
# Ensure to be at root of contract chameleon
# cd contract-chameleon (or your folder name)
cd libs/jmlparser
./mvnw clean install -DskipTests
ls ~/.m2/repository/io/github/jmltoolkit/jmlparser-core/ #check that snapshot exists
```

- Build with gradle

```sh
# Ensure to be at root of contract chameleon
# cd contract-chameleon (or your folder name)
gradle clean build
gradle run --args="<adapter-name> <file_path>"
gradle run-integration-tests #Simple way to check if the adapter do what they should
```

## Accessing the JavaDoc

For each module the `JavaDoc` can be found in `<module>/build/docs/javadoc/org/contract_lib/contract_chameleon/package-summary.html`.

### JavaDoc of Dependencies

#### jmlparser

```sh
# build Javadoc
./mvnw javadoc:javadoc
```

The `JavaDoc` can be found in for the different packages: `<jmlparser-module>/javaparser-core/target/reports/apidocs/index.html`

## Run integration tests with tools

### KeY

```sh
# check if proofs can be found in key (see folder `tests.integration`)
gradle key_ci-check-all --continue
```
