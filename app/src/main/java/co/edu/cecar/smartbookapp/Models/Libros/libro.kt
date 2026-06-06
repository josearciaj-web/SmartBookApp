

package co.edu.cecar.smartbookapp.Models.Libros

import co.edu.cecar.smartbookapp.Models.Inventario.Inventario
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class Libro(
    @SerialName("id") val id: Int? = null,
    @SerialName("nombre") val nombre: String = "",
    @SerialName("nivel") val nivel: String = "",
    @Serializable(with = TipoLibroSerializer::class)
    @SerialName("tipo") val tipo: Int = 0,
    @SerialName("edicion") val edicion: String = "",
    @SerialName("unidades") val unidades: Int = 0,
    @SerialName("lote") val lote: Int = 0,
    @SerialName("stockTotal") val stock: Int = 0,
    @SerialName("valorCompa") val valorCompra: Double = 0.0,
    @SerialName("valorVentaPulico") val valorVentaPublico: Double = 0.0,
    @SerialName("inventario") val inventario: List<Inventario>? = null
)

object TipoLibroSerializer : KSerializer<Int> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("TipoLibro", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Int {
        val valor = decoder.decodeString()

        return when (valor) {
            "1", "Student's Book", "StudentBook", "StudentsBook" -> 1
            "2", "Workbook" -> 2

            else -> valor.toIntOrNull() ?: 0
        }
    }

    override fun serialize(encoder: Encoder, value: Int) {
        encoder.encodeInt(value)
    }
}