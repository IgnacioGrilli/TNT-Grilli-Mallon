package unpsjb.tnt.gestion_gastos.ui.data.model

import java.util.Date

data class Transaction(
    val id: String = java.util.UUID.randomUUID().toString(),
    val amount: Double,
    val description: String,
    val category: Category,
    val date: Date = Date(),
    val type: CategoryType
)