package unpsjb.tnt.gestion_gastos.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import unpsjb.tnt.gestion_gastos.ui.data.AppRepository
import unpsjb.tnt.gestion_gastos.ui.data.model.Category
import unpsjb.tnt.gestion_gastos.ui.data.model.CategoryType
import unpsjb.tnt.gestion_gastos.ui.data.model.Transaction
import unpsjb.tnt.gestion_gastos.ui.home.HomeUiState
import unpsjb.tnt.gestion_gastos.ui.home.PeriodFilter


data class CategoriesUiState(
    val categories: List<Category> = emptyList(),
    val isEmpty: Boolean = true
)

class CategoriesViewModel : ViewModel() {

    // Tab activo: GASTO o INGRESO
    val activeType = MutableLiveData(CategoryType.GASTO)

    //val categories = CategoriesUiState()
    val uiState: LiveData<CategoriesUiState> = MediatorLiveData<CategoriesUiState>().apply {
        fun recompute() {
            val type   = activeType.value   ?: CategoryType.GASTO
            val all    = AppRepository.categories.value ?: emptyList()
            val filtered = all.filter { it.type == type }
            value = CategoriesUiState(
                categories = filtered,
                isEmpty      = filtered.isEmpty()
            )
        }
        addSource(AppRepository.categories) { recompute() }
        addSource(activeType)                 { recompute() }
        recompute()   // ← inicializar inmediatamente
    }

    fun setType(type: CategoryType){
        activeType.value   = type
    }


}