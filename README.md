Hello there!

This repository contains my submission for the Mable backend code test.

When implementing this solution I have made some assumptions and judgment calls listed here:
* A malformed CSV file, for example wrong number of columns or characters where digits are expected, should be treated
  as an input error.
* A CSV line with correct but invalid data, such as too low balance or an account number that does not exist, should be
  treated as ok input but will result in a failed transaction.
* A failed transaction does not prevent following transactions from succeeding. 
* The program should output the number of successfully processed transactions, the account balances after processing and
  all the failed transactions including the reason of failure.
* I decided to just use standard Kotlin and no application framework such as Spring Boot. I made this decision 
  because 1. Spring Boot is not relevant for this role and 2. I thought it would be more valuable to show
  that I understand how to bootstrap an app without a framework, even if this is a very simple example of that. 
* I wanted to focus on building an extensible program with good structure and solid test coverage. For that reason I 
  decided to keep it simple and just pass the files in as arguments to the program which then runs the transactions 
  ones and prints the result.
* I have not added validations for every failure case, there are plenty of bad bugs with the right 
  transaction file (for example negative amounts or same sender and receiver). My goal was to build a 
  framework where adding more validations would be easy, not to cover every case. 

Project package structure:

```
src/main/kotlin/
├── Main.kt                              # Application entry point, wires dependencies and prints results
├── account/
│   ├── Account.kt                       # Immutable data class representing a bank account
│   ├── AccountLineParser.kt             # Parses a CSV line into an Account
│   ├── AccountNumber.kt                 # Value class with 16-digit validation
│   ├── AccountRepository.kt             # Interface for loading accounts
│   └── InMemoryAccountRepository.kt     # Map-backed AccountRepository implementation
├── core/csv/
│   ├── CsvLineParser.kt                 # Functional interface for parsing a single CSV line into a type
│   └── CsvReader.kt                     # Generic CSV reader that combines a source with a line parser
└── transaction/
    ├── Transaction.kt                   # Immutable data class representing a transfer between accounts
    ├── TransactionFailure.kt            # Sealed class of failure reasons (e.g. InsufficientFunds)
    ├── TransactionLineParser.kt         # Parses a CSV line into a Transaction
    ├── TransactionResult.kt             # Data class containing updated accounts and any failures
    ├── TransactionService.kt            # Processes transactions against accounts with validation
    └── TransactionValidation.kt         # Functional interface for validation rules and their implementations
```

Here are some instructions on how to build and run the project:

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
