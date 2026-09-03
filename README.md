## Prerequisites

Install Java 21 using [SDKMAN](https://sdkman.io):

```bash
curl -s "https://get.sdkman.io" | zsh
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 21.0.12+1.1-tem
```
Note: If you are using bash you have to install sdkman using this command:
```bash
curl -s "https://get.sdkman.io" | bash
```
## Build

```bash
./gradlew jar
```

This produces a fat JAR at `build/libs/MableBackendTest-1.0-SNAPSHOT.jar`.

## Run

```bash
java -jar build/libs/MableBackendTest-1.0-SNAPSHOT.jar <balances.csv> <transactions.csv>
```

For example, using the included sample data:

```bash
java -jar build/libs/MableBackendTest-1.0-SNAPSHOT.jar mable_account_balances.csv mable_transactions.csv
```

## Tests

```bash
./gradlew test
```
