@file:Suppress("DEPRECATION")

import java.util.*

fun main() {
    val sentiments = calculateSentiments()
    sentiments.byMonth()
}

fun List<SentimentsByDate>.byMonth() = fold(SentimentsByDate(emptyList(), Date(0, 0, 1))) { average, sentiments ->
    if (sentiments.date.month == average.date.month) average.copy(values = average.values + sentiments.values)
    else {
        listOf(average).byDay().forEach { sentiments ->
            println(
                "${sentiments.date}\t\t${sentiments.value.format(digits = 16)}"
            )
        }
        sentiments
    }
}.also {
    listOf(it).byDay().forEach { sentiments ->
        println(
            "${sentiments.date}\t\t${sentiments.value.format(digits = 16)}"
        )
    }
}

fun List<SentimentsByDate>.byDay() = map { sentiments ->
    AverageSentiments(sentiments.values.average(), date = sentiments.date)
}

fun List<SentimentsByDate>.byYear(): List<AverageSentiments> {
    val sentiments2016 = filter { sentiments -> sentiments.date.year <= 117 }
        .fold(SentimentsByDate(emptyList(), Date(116, 1, 1))) { average, sentiments ->
            average.copy(values = average.values + sentiments.values)
        }

    val sentiments2020 = filter { sentiments -> sentiments.date.year <= 121 }
        .fold(SentimentsByDate(emptyList(), Date(120, 0, 1))) { average, sentiments ->
            average.copy(values = average.values + sentiments.values)
        }

    return listOf(sentiments2016, sentiments2020).byDay()
}

fun List<SentimentsByDate>.beforeAndAfter(): List<AverageSentiments> {
    require(filter { sentiments -> sentiments.date < Date(116, 10, 8) }.isNotEmpty()) { this }
    val sentiments2016Before = filter { sentiments -> sentiments.date < Date(116, 10, 8) }
        .fold(SentimentsByDate(emptyList(), Date(116, 6, 8))) { average, sentiments ->
            average.copy(values = average.values + sentiments.values)
        }

    val sentiments2016After =
        filter { sentiments -> sentiments.date >= Date(116, 10, 8) && sentiments.date < Date(120, 1, 1) }
            .fold(SentimentsByDate(emptyList(), Date(116, 10, 8))) { average, sentiments ->
                average.copy(values = average.values + sentiments.values)
            }

    val sentiments2020Before =
        filter { sentiments -> sentiments.date < Date(120, 10, 3) && sentiments.date > Date(120, 1, 1) }
            .fold(SentimentsByDate(emptyList(), Date(120, 6, 3))) { average, sentiments ->
                average.copy(values = average.values + sentiments.values)
            }

    val sentiments2020After = filter { sentiments -> sentiments.date >= Date(120, 10, 3) }
        .fold(SentimentsByDate(emptyList(), Date(120, 10, 3))) { average, sentiments ->
            average.copy(values = average.values + sentiments.values)
        }

    return listOf(sentiments2016Before, sentiments2016After, sentiments2020Before, sentiments2020After).byDay()
}

data class AverageSentiments(val value: Double, val date: Date)
