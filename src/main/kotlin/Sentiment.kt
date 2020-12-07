data class Sentiment(val value: Double, val mainForm: String, val allForms: List<String>) {
    init {
        require(mainForm.isNotBlank())
        require(mainForm in allForms)
        require(value in -1.0..1.0)
    }
}
