package unpsjb.tnt.gestion_gastos.data.model

import java.util.UUID

enum class CategoryType { GASTO, INGRESO }

data class Category(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val icon: String,
    val type: CategoryType
)