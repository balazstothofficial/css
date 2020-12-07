fun readSentimentsToMap() = readSentiments()
    .flatMap { sentiment -> sentiment.allForms.map { form -> form to sentiment.value } }
    .toMap()
