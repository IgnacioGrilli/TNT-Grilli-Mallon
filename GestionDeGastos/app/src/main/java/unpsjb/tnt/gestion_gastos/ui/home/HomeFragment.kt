package unpsjb.tnt.gestion_gastos.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import unpsjb.tnt.gestion_gastos.R
import unpsjb.tnt.gestion_gastos.ui.data.AppRepository
import unpsjb.tnt.gestion_gastos.ui.data.model.CategoryType
import unpsjb.tnt.gestion_gastos.ui.data.model.Transaction
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var txAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fmt = NumberFormat.getNumberInstance(Locale("es", "AR"))
            .apply { maximumFractionDigits = 2; minimumFractionDigits = 0 }

        // ── Referencias a vistas ──────────────────────────────────────────
        val tvBalance      = view.findViewById<TextView>(R.id.tv_balance)
        val tabsGastoIngreso = view.findViewById<TabLayout>(R.id.tabs_gasto_ingreso)
        val chipDay        = view.findViewById<TextView>(R.id.chip_day)
        val chipWeek       = view.findViewById<TextView>(R.id.chip_week)
        val chipMonth      = view.findViewById<TextView>(R.id.chip_month)
        val chipYear       = view.findViewById<TextView>(R.id.chip_year)
        val tvPeriodLabel  = view.findViewById<TextView>(R.id.tv_period_label)
        val rvTransactions = view.findViewById<RecyclerView>(R.id.rv_transactions)
        val tvEmpty        = view.findViewById<TextView>(R.id.tv_empty)
        val fab            = view.findViewById<FloatingActionButton>(R.id.fab_add_transaction)

        // ── RecyclerView ─────────────────────────────────────────────────
        txAdapter = TransactionAdapter()
        rvTransactions.layoutManager = LinearLayoutManager(requireContext())
        rvTransactions.adapter = txAdapter

        // ── Tabs Gastos / Ingresos ────────────────────────────────────────
        tabsGastoIngreso.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val type = if (tab.position == 0) CategoryType.GASTO else CategoryType.INGRESO
                viewModel.setType(type)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // ── Chips de período ──────────────────────────────────────────────
        val chips = listOf(chipDay, chipWeek, chipMonth, chipYear)
        val periods = listOf(PeriodFilter.DAY, PeriodFilter.WEEK, PeriodFilter.MONTH, PeriodFilter.YEAR)

        // Colores definidos por código — evita depender de res/color/selector_chip_text.xml
        val colorActive   = androidx.core.content.ContextCompat.getColor(requireContext(), R.color.accent_yellow)
        val colorInactive = androidx.core.content.ContextCompat.getColor(requireContext(), R.color.text_secondary)

        fun selectChip(selected: TextView) {
            chips.forEach { chip ->
                chip.isSelected = (chip == selected)
                chip.setTextColor(if (chip == selected) colorActive else colorInactive)
            }
        }

        chipDay.setOnClickListener   { selectChip(chipDay);   viewModel.setPeriod(PeriodFilter.DAY)   }
        chipWeek.setOnClickListener  { selectChip(chipWeek);  viewModel.setPeriod(PeriodFilter.WEEK)  }
        chipMonth.setOnClickListener { selectChip(chipMonth); viewModel.setPeriod(PeriodFilter.MONTH) }
        chipYear.setOnClickListener  { selectChip(chipYear);  viewModel.setPeriod(PeriodFilter.YEAR)  }

        // Estado inicial: chip Semana seleccionado
        selectChip(chipWeek)

        // ── Observers ────────────────────────────────────────────────────
        viewModel.balance.observe(viewLifecycleOwner) { b ->
            tvBalance.text = "$ ${fmt.format(b)}"
        }

        viewModel.activePeriod.observe(viewLifecycleOwner) { period ->
            tvPeriodLabel.text = viewModel.periodLabel(period)
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            txAdapter.submitList(state.transactions)
            tvEmpty.visibility        = if (state.isEmpty) View.VISIBLE else View.GONE
            rvTransactions.visibility = if (state.isEmpty) View.GONE    else View.VISIBLE

            // Actualizar texto vacío según tipo activo
            val tipo = if (viewModel.activeType.value == CategoryType.GASTO) "gastos" else "ingresos"
            val periodo = when (viewModel.activePeriod.value) {
                PeriodFilter.DAY   -> "hoy"
                PeriodFilter.WEEK  -> "esta semana"
                PeriodFilter.MONTH -> "este mes"
                PeriodFilter.YEAR  -> "este año"
                else               -> "este período"
            }
            tvEmpty.text = "No hubo $tipo $periodo"
        }

        // ── FAB: agregar transacción ─────────────────────────────────────
        fab.setOnClickListener {
            val type = viewModel.activeType.value ?: CategoryType.GASTO
            showAddTransactionDialog(type)
        }
    }

    private fun showAddTransactionDialog(type: CategoryType) {
        val categories = AppRepository.categoriasByType(type)

        if (categories.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "No hay categorías de ${type.name.lowercase()}. Agregá una en el menú de Categorías.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_transaction, null)

        val etAmount  = dialogView.findViewById<EditText>(R.id.et_tx_amount)
        val etDesc    = dialogView.findViewById<EditText>(R.id.et_tx_description)
        val spinner   = dialogView.findViewById<Spinner>(R.id.spinner_tx_category)

        val catNames = categories.map { "${it.icon} ${it.name}" }
        spinner.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, catNames)
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        val title = if (type == CategoryType.GASTO) "Registrar gasto" else "Registrar ingreso"

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val amount = etAmount.text.toString().toDoubleOrNull()
                if (amount == null || amount <= 0) {
                    Toast.makeText(requireContext(), "Ingresá un monto válido", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                AppRepository.addTransaction(
                    Transaction(
                        amount      = amount,
                        description = etDesc.text.toString(),
                        category    = categories[spinner.selectedItemPosition],
                        type        = type
                    )
                )
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}