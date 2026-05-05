package cat.hajoya.piratasdeandromeda.ui.preparacio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import cat.hajoya.piratasdeandromeda.R
import cat.hajoya.piratasdeandromeda.databinding.PersonajesPartidaBinding
import cat.hajoya.piratasdeandromeda.ui.joc.MenuJuegoFragment
import cat.hajoya.piratasdeandromeda.ui.main.MainActivity
import cat.hajoya.piratasdeandromeda.viewmodels.GameViewModel

class PersonatgesFragment : Fragment() {

    private var _binding: PersonajesPartidaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by activityViewModels {
        (requireActivity() as MainActivity).gameViewModelFactory
    }

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

        observeViewModel()
        setupListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() {
        binding.btnCancel.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnEmpezar.setOnClickListener {
            viewModel.iniciarPartida()
            openMenuJuegoScreen()
        }
    }

    private fun observeViewModel() {
        viewModel.partidaActual.observe(viewLifecycleOwner) { partida ->
            binding.btnPartidaCode.text = partida?.codiPartida ?: getString(R.string.players_sample_code)
        }
    }

    private fun openMenuJuegoScreen() {
        parentFragmentManager.commit {
            setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out,
            )
            replace(R.id.fragment_container, MenuJuegoFragment())
            addToBackStack(null)
        }
    }
}


