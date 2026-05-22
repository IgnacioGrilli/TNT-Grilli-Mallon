package unpsjb.tnt.gestion_gastos.ui.data.model
enum class CategoryType { GASTO, INGRESO }

data class Category(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val icon: String,
    val type: CategoryType
)