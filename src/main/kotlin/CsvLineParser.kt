fun interface CsvLineParser<T> {
    fun parse(line: String): T
}
