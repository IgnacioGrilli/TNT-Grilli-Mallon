package unpsjb.tnt.gestion_gastos.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import unpsjb.tnt.gestion_gastos.ui.data.AppRepository
import unpsjb.tnt.gestion_gastos.ui.data.model.CategoryType
import unpsjb.tnt.gestion_gastos.ui.data.model.Transaction
import java.util.Calendar
import java.util.Date

enum class PeriodFilter { DAY, WEEK, MONTH, YEAR }

data class HomeUiState(
    val transactions: List<Transaction> = emptyList(),
    val total: Double = 0.0,
    val isEmpty: Boolean = true
)

class HomeViewModel : ViewModel() {

    // Tab activo: GASTO o INGRESO
    val activeType = MutableLiveData(CategoryType.GASTO)

    // Período activo
    val activePeriod = MutableLiveData(PeriodFilter.WEEK)

    // Estado derivado que reacciona a cambios en tipo, período y transacciones
    val uiState: LiveData<HomeUiState> = MediatorLiveData<HomeUiState>().apply {
        fun recompute() {
            val type   = activeType.value   ?: CategoryType.GASTO
            val period = activePeriod.value ?: PeriodFilter.WEEK
            val all    = AppRepository.transactions.value ?: emptyList()

            val from = periodStart(period)
            val filtered = all.filter { it.type == type && it.date >= from }

            value = HomeUiState(
                transactions = filtered.sortedByDescending { it.date },
                total        = filtered.sumOf { it.amount },
                isEmpty      = filtered.isEmpty()
            )
        }

        addSource(AppRepository.transactions) { recompute() }
        addSource(activeType)                 { recompute() }
        addSource(activePeriod)               { recompute() }
    }

    // Totales globales para el header (independiente del tab)
    val totalGastos: LiveData<Double> = MediatorLiveData<Double>().apply {
        addSource(AppRepository.transactions) { list ->
            value = list.filter { it.type == CategoryType.GASTO }.sumOf { it.amount }
        }
    }

    val totalIngresos: LiveData<Double> = MediatorLiveData<Double>().apply {
        addSource(AppRepository.transactions) { list ->
            value = list.filter { it.type == CategoryType.INGRESO }.sumOf { it.amount }
        }
    }

    val balance: LiveData<Double> = MediatorLiveData<Double>().apply {
        fun calc() {
            val i = totalIngresos.value ?: 0.0
            val g = totalGastos.value   ?: 0.0
            value = i - g
        }
        addSource(totalIngresos) { calc() }
        addSource(totalGastos)   { calc() }
    }

    fun setType(type: CategoryType)     { activeType.value   = type   }
    fun setPeriod(period: PeriodFilter) { activePeriod.value = period }

    /** Devuelve el inicio del período seleccionado */
    private fun periodStart(period: PeriodFilter): Date {
        val cal = Calendar.getInstance()
        when (period) {
            PeriodFilter.DAY  -> cal.set(Calendar.HOUR_OF_DAY, 0)
                .also { cal.set(Calendar.MINUTE, 0) }
                .also { cal.set(Calendar.SECOND, 0) }
            PeriodFilter.WEEK -> {
                cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
                cal.set(Calendar.HOUR_OF_DAY, 0)
            }
            PeriodFilter.MONTH -> {
                cal.set(Calendar.DAY_OF_MONTH, 1)
                cal.set(Calendar.HOUR_OF_DAY, 0)
            }
            PeriodFilter.YEAR -> {
                cal.set(Calendar.DAY_OF_YEAR, 1)
                cal.set(Calendar.HOUR_OF_DAY, 0)
            }
        }
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }

    /** Texto descriptivo del rango actual (ej: "30 mar – 5 abr") */
    fun periodLabel(period: PeriodFilter): String {
        val sdf = java.text.SimpleDateFormat("d MMM", java.util.Locale("es"))
        val today = Calendar.getInstance()
        return when (period) {
            PeriodFilter.DAY  -> sdf.format(today.time)
            PeriodFilter.WEEK -> {
                val start = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                }
                val end = Calendar.getInstance().apply {
                    set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
                    add(Calendar.DAY_OF_WEEK, 6)
                }
                "${sdf.format(start.time)} – ${sdf.format(end.time)}"
            }
            PeriodFilter.MONTH -> {
                val sdfMonth = java.text.SimpleDateFormat("MMMM yyyy", java.util.Locale("es"))
                sdfMonth.format(today.time)
                    .replaceFirstChar { it.uppercase() }
            }
            PeriodFilter.YEAR -> today.get(Calendar.YEAR).toString()
        }
    }
}