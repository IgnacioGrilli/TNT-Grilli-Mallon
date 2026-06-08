package unpsjb.tnt.gestion_gastos.data.model

import java.util.Date
import java.util.UUID

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val amount: Double,
    val description: String,
    val category: Category,
    val date: Date = Date(),
    val type: CategoryType
)