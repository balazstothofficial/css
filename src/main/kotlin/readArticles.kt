import java.util.*
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun readArticles() = ResourcesManager
    .list("articles")
    .map { article ->
        val nameAndDate = article.name.split('.').dropLast(1)
        require(nameAndDate.size == 3) { "$nameAndDate" }

        @Suppress("DEPRECATION")
        Article(
            title = nameAndDate[0].dropLast(2),
            date = Date(nameAndDate[2].toInt() - 1900, nameAndDate[1].toInt() - 1, nameAndDate[0].takeLast(2).toInt()),
            content = article.readText()
        )
    }

data class Article @OptIn(ExperimentalTime::class) constructor(
    val title: String,
    val date: Date,
    val content: String
)
