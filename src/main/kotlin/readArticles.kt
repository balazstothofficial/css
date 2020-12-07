import java.io.File

fun readArticles() = ResourcesManager
    .list("articles")
    .map(File::readText)
    .reduce(String::plus)
