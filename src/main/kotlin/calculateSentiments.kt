import java.util.*

fun calculateSentiments(): List<SentimentsByDate> {
    val sentiments = readSentimentsToMap()

    return readArticles()
        .sortedBy(Article::date)
        .fold(emptyList()) { sentimentsByDate, article ->
            val words = Regex("[a-zA-Z]+").findAll(article.content).map(MatchResult::value).toList()

            @Suppress("NAME_SHADOWING")
            val sentiments = words.map { word -> sentiments.getOrDefault(word, 0.0) }

            if (article.date == sentimentsByDate.lastOrNull()?.date) {
                val last = sentimentsByDate.last()
                sentimentsByDate.dropLast(1) + last.copy(values = last.values + sentiments)
            } else {
                sentimentsByDate + SentimentsByDate(sentiments, article.date)
            }
        }
}

data class SentimentsByDate(val values: List<Double>, val date: Date)
