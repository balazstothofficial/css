fun calculateAverageSentiment(): Double  {
    val sentiments = readSentimentsToMap()

    val words = Regex("[a-zA-Z]+").findAll(readArticles()).map(MatchResult::value).toList()

    val matches = words.map { word -> sentiments.getOrDefault(word, 0.0) }

    println("Words: ${words.size}, Matches: ${matches.filter { it != 0.0 }.size}")

    return words.map { word -> sentiments.getOrDefault(word, 0.0) }.average()
}
