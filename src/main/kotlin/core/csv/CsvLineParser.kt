package core.csv

fun interface CsvLineParser<T> {
    fun parse(line: String): T
}
