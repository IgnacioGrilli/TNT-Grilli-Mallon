package unpsjb.tnt.gestion_gastos.ui.data.model

import java.util.UUID
data class Icon(
    val id: String = UUID.randomUUID().toString(),
    val icon: String
)