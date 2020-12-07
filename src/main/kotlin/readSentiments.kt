import java.io.File

fun readSentiments() = ResourcesManager.list("sentiments")
    .map(File::readLines)
    .flatten()
    .filter(String::isNotBlank)
    .map(String::toSentiment)

private fun String.toSentiment(): Sentiment {
    // get mainForm:
    val mainFormAndRest = split('|')
    require(mainFormAndRest.size == 2)

    val mainForm = mainFormAndRest[0]
    val rest = mainFormAndRest[1]

    //println("mainFormAndRest: $mainFormAndRest")

    // split value and other forms
    val valueAndOtherForms = rest.split('\t')
        // Remove pos tags for now
        .drop(1)

    //println("ValueAndOtherForms: $valueAndOtherForms")

    require(valueAndOtherForms.size == 2 || valueAndOtherForms.size == 1)

    val value = valueAndOtherForms[0].toDouble()
    val otherForms = if (valueAndOtherForms.size == 2) valueAndOtherForms[1].split(',') else emptyList()

    return Sentiment(value = value, mainForm = mainForm, allForms = listOf(mainForm) + otherForms)
}
