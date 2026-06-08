package unpsjb.tnt.gestion_gastos.ui.categories

import android.app.AlertDialog
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import unpsjb.tnt.gestion_gastos.R
import unpsjb.tnt.gestion_gastos.data.AppRepository
import unpsjb.tnt.gestion_gastos.data.model.Category
import unpsjb.tnt.gestion_gastos.data.model.CategoryType

class CategoriesFragment : Fragment() {

    companion object {
        fun newInstance() = CategoriesFragment()
    }

    private val viewModel: CategoriesViewModel by viewModels()

    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ── Referencias a vistas ──────────────────────────────────────────
        val tabsGastoIngreso = view.findViewById<TabLayout>(R.id.tabs_gasto_ingreso)
        val rvCategories = view.findViewById<RecyclerView>(R.id.rv_categories)
        val tvEmpty        = view.findViewById<TextView>(R.id.tv_empty)
        val fab            = view.findViewById<FloatingActionButton>(R.id.fab_add_transaction)

        // ── RecyclerView ─────────────────────────────────────────────────
        categoriesAdapter = CategoriesAdapter()
        rvCategories.layoutManager = LinearLayoutManager(requireContext())
        rvCategories.adapter = categoriesAdapter

        // ── Tabs Gastos / Ingresos ────────────────────────────────────────
        tabsGastoIngreso.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val type = if (tab.position == 0) CategoryType.GASTO else CategoryType.INGRESO
                viewModel.setType(type)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            categoriesAdapter.submitList(state.categories)
            tvEmpty.visibility        = if (state.isEmpty) View.VISIBLE else View.GONE
            rvCategories.visibility = if (state.isEmpty) View.GONE    else View.VISIBLE

            // Actualizar texto vacío según tipo activo
            // val tipo = if (viewModel.activeType.value == CategoryType.GASTO) "gastos" else "ingresos"
            // tvEmpty.text = "No hubo $tipo $periodo"
        }
        // ── FAB: agregar transacción ─────────────────────────────────────
        fab.setOnClickListener {
            val type = viewModel.activeType.value ?: CategoryType.GASTO
            showAddTransactionDialog(type)
        }

    }

    private fun showAddTransactionDialog(type: CategoryType) {
        val icons = AppRepository.getIcons()

        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_add_category, null)

        val etDesc    = dialogView.findViewById<EditText>(R.id.et_tx_description)
        val spinner   = dialogView.findViewById<Spinner>(R.id.spinner_tx_category)

        val catNames = icons.map { "${it.icon}" }
        spinner.adapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_spinner_item, catNames)
            .also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        val title = if (type == CategoryType.GASTO) "Registrar gasto" else "Registrar ingreso"

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                AppRepository.addCategory(
                    Category(
                        name = etDesc.text.toString(),
                        icon    = icons[spinner.selectedItemPosition].icon,
                        type        = type
                    )
                )
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}