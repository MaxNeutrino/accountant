package com.neutrino.project.accountant.client.to

data class VictoriaDTO(
        var operatorId: String = "-1",
        var operatorEmail: String = "-1",
        var maleName: String = "-1",
        var maleId: String = "-1",
        var femaleName: String = "-1",
        var femaleId: String = "-1",
        var orderItems: MutableList<String> = mutableListOf<String>(),
        var totalPrice: String = "-1"
)
{
    override fun toString(): String
    {
        return "operatorId: $operatorId \n" +
                "operatorEmail: $operatorEmail \n" +
                "maleName: $maleName \n" +
                "maleId : $maleId \n" +
                "femaleName: $femaleName \n" +
                "femaleId: $femaleId \n" +
                "orderItems: $orderItems \n" +
                "totalPrice: $totalPrice \n"
    }
}