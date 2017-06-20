package com.neutrino.project.accountant.parser.to

data class VictoriaDTO(
        var operatorId: String = "-1",
        var operatorEmail: String = "-1",
        var maleName: String = "-1",
        var maleId: String = "-1",
        var femaleName: String = "-1",
        var femaleId: String = "-1",
        var orderDate: String = "-1",
        var orderItems: MutableList<String> = mutableListOf<String>(),
        var totalPrice: String = "-1"
) {

    /**
     * For table
     */
    override fun toString(): String {
        //TODO("Replace with normal #toString() when will be refactoring")
        return "$operatorId\t" +
                "$operatorEmail\t" +
                "$maleName\t" +
                "$maleId\t" +
                "$femaleName\t" +
                "$orderDate\t" +
                "$femaleId\t" +
                "\t" +                  //empty
                "\t" +                  // real translator name
                "\t" +                  // real woman name
                "${orderItems()}\t" +
                totalPrice
    }

    private fun orderItems(): String {
        val items = StringBuilder("")
        val prices = StringBuilder("")
        orderItems.forEach {
            val itemValues = it.replace(" ", "").split(":")
            items.append(itemValues[0])
            items.append(":")
            items.append(itemValues[1])
            items.append(" / ")

            prices.append(itemValues[2])
            prices.append(" / ")
        }

        items.delete(items.length - 3, items.length -1)
        prices.delete(prices.length - 3, prices.length -1)

        return "${items.trim()}\t${prices.trim()}"
    }
}