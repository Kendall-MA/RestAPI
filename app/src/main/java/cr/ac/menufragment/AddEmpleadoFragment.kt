package cr.ac.menufragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toBitmap
import com.squareup.picasso.Picasso
import cr.ac.menufragment.entity.Empleado
import cr.ac.menufragment.repository.EmpleadoRepository
import java.io.ByteArrayOutputStream

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val PICK_IMAGE = 200

/**
 * A simple [Fragment] subclass.
 * Use the [AddEmpleadoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEmpleadoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    lateinit var img_avatar : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view : View =  inflater.inflate(R.layout.fragment_add_empleado, container, false)

        val idEmpleado = view.findViewById<TextView>(R.id.id_empleado_nuevo)
        val nombre = view.findViewById<TextView>(R.id.nombre_empleado_nuevo)
        val puesto = view.findViewById<TextView>(R.id.puesto_empleado_nuevo)
        val departamento = view.findViewById<TextView>(R.id.departamento_empleado_nuevo)
        img_avatar = view.findViewById<ImageView>(R.id.add_empleado_image)

        val imagen = view.findViewById<ImageView>(R.id.add_empleado_image)
        imagen.setImageResource(R.drawable.ic_launcher_foreground)

        view.findViewById<Button>(R.id.agregar_empleado).setOnClickListener{

            val builder = AlertDialog.Builder(context)
            builder.setMessage("¿Desea añadir este registro?")
                .setCancelable(false)
                .setPositiveButton("Sí") { dialog, id ->
                    // Lógica si


                    val nuevoEmpleado = Empleado(0, idEmpleado.text.toString(), nombre.text.toString(), puesto.text.toString(), departamento.text.toString(), encodeImage(img_avatar.drawable.toBitmap())!!)
                    EmpleadoRepository.instance.save(nuevoEmpleado)

                    // Redirigir después de añadir
                    val fragmento = CameraFragment.newInstance(getString(R.string.menu_camera))
                    fragmentManager
                        ?.beginTransaction()
                        ?.replace(R.id.home_content, fragmento)
                        ?.commit()
                    activity?.setTitle("Lista de Empleados")
                }
                .setNegativeButton("No") { dialog, id ->
                    // Lógica del no
                }
            val alert = builder.create()
            alert.show()
        }

        img_avatar.setOnClickListener{
            var gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, PICK_IMAGE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check if the activity is
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {

            var imageUri = data?.data

            // Resize Image
            Picasso.get()
                .load(imageUri)
                .resize(120,120)
                .centerCrop()
                .into(img_avatar)
        }
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragment AddEmpleadoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            AddEmpleadoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}