package unpsjb.tnt.gestion_gastos.data

import androidx.lifecycle.MutableLiveData
import unpsjb.tnt.gestion_gastos.data.model.Category
import unpsjb.tnt.gestion_gastos.data.model.CategoryType
import unpsjb.tnt.gestion_gastos.data.model.Icon
import unpsjb.tnt.gestion_gastos.data.model.Transaction

/**
 * Repositorio central en memoria (sin persistencia).
 * Singleton compartido entre todos los ViewModels.
 */
object AppRepository {

    val categories = MutableLiveData<MutableList<Category>>(
        mutableListOf(
            Category(name = "Alimentación",   icon = "🍔", type = CategoryType.GASTO),
            Category(name = "Transporte",     icon = "🚌", type = CategoryType.GASTO),
            Category(name = "Servicios",      icon = "💡", type = CategoryType.GASTO),
            Category(name = "Salud",          icon = "🏥", type = CategoryType.GASTO),
            Category(name = "Ocio",           icon = "🎮", type = CategoryType.GASTO),
            Category(name = "Sueldo",         icon = "💼", type = CategoryType.INGRESO),
            Category(name = "Freelance",      icon = "💻", type = CategoryType.INGRESO),
            Category(name = "Otros ingresos", icon = "💰", type = CategoryType.INGRESO)
        )
    )

    val icons = MutableLiveData<MutableList<Icon>>(
        mutableListOf(
            Icon(icon = "🍔"),
            Icon(icon = "🛒"),
            Icon(icon = "🚗"),
            Icon(icon = "⛽"),
            Icon(icon = "💊"),
            Icon(icon = "📚"),
            Icon(icon = "🎬"),
            Icon(icon = "⚽"),
            Icon(icon = "✈️"),
            Icon(icon = "🏠"),
            Icon(icon = "💡"),
            Icon(icon = "🌐"),
            Icon(icon = "📱"),
            Icon(icon = "👕"),
            Icon(icon = "🐶"),
            Icon(icon = "🎁"),
            Icon(icon = "💰"),
            Icon(icon = "📈"),
            Icon(icon = "💵"),
            Icon(icon = "📦"),
            Icon(icon = "☕"),
            Icon(icon = "🍕"),
            Icon(icon = "🚕"),
            Icon(icon = "🚌"),
            Icon(icon = "🚆"),
            Icon(icon = "🚲"),
            Icon(icon = "🏥"),
            Icon(icon = "🎵"),
            Icon(icon = "🎮"),
            Icon(icon = "🍿"),
            Icon(icon = "🏦"),
            Icon(icon = "💳"),
            Icon(icon = "🧾"),
            Icon(icon = "💼"),
            Icon(icon = "🎓"),
            Icon(icon = "🌱"),
            Icon(icon = "🔧"),
            Icon(icon = "🔒"),
            Icon(icon = "📷"),
            Icon(icon = "🎨"),
            Icon(icon = "📺"),
            Icon(icon = "🖥️"),
            Icon(icon = "⌚"),
            Icon(icon = "💍"),
            Icon(icon = "🧹"),
            Icon(icon = "🛠️"),
            Icon(icon = "📍"),
            Icon(icon = "🌍"),
            Icon(icon = "📖"),
            Icon(icon = "🧠"),
            Icon(icon = "🎤"),
            Icon(icon = "🎻"),
            Icon(icon = "🏋️"),
            Icon(icon = "🥗"),
            Icon(icon = "🥐"),
            Icon(icon = "🍣"),
            Icon(icon = "🌮"),
            Icon(icon = "🍰"),
            Icon(icon = "🧃"),
            Icon(icon = "🚀")
        )
    )
    val transactions = MutableLiveData<MutableList<Transaction>>(mutableListOf())

    fun addCategory(category: Category) {
        val list = categories.value ?: mutableListOf()
        list.add(category)
        categories.value = list
    }

    fun removeCategory(category: Category) {
        val list = categories.value ?: return
        list.remove(category)
        categories.value = list
    }

    fun addTransaction(transaction: Transaction) {
        val list = transactions.value ?: mutableListOf()
        list.add(transaction)
        transactions.value = list
    }

    fun categoriasByType(type: CategoryType): List<Category> =
        categories.value?.filter { it.type == type } ?: emptyList()

    fun getIcons(): List<Icon> =
        icons.value?: emptyList()
}