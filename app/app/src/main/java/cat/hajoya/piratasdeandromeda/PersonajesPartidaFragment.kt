package cat.hajoya.piratasdeandromeda

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cat.hajoya.piratasdeandromeda.databinding.PersonajesPartidaBinding
import cat.hajoya.piratasdeandromeda.ui.joc.MenuJuegoFragment

class PersonajesPartidaFragment : Fragment() {

    private var _binding: PersonajesPartidaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = PersonajesPartidaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Navegar al menú del juego al hacer click en "Comenzar"
        binding.btnEmpezar.setOnClickListener {
            navigateToGameMenu()
        }
    }

    /**
     * Navega al menú del juego reemplazando este fragment
     */
    private fun navigateToGameMenu() {
        val menuJuegoFragment = MenuJuegoFragment()
        
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, menuJuegoFragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

