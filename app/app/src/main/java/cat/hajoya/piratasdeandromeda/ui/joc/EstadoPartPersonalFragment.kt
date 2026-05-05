package cat.hajoya.piratasdeandromeda.ui.joc

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cat.hajoya.piratasdeandromeda.R
import cat.hajoya.piratasdeandromeda.databinding.EstadoPartPersonalBinding
import cat.hajoya.piratasdeandromeda.ui.main.MainActivity
import cat.hajoya.piratasdeandromeda.viewmodels.GameViewModel

class EstadoPartPersonalFragment : Fragment() {

    private var _binding: EstadoPartPersonalBinding? = null
    private val binding get() = _binding!!

    private val adapter = PlayerStatusAdapter(::showPlayerInfoDialog)
    private val viewModel: GameViewModel by activityViewModels {
        (requireActivity() as MainActivity).gameViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = EstadoPartPersonalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
        setupStaticUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.rvPlayersProgress.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPlayersProgress.adapter = adapter
        adapter.submitList(
            listOf(
                PlayerStatusUi(1L, "Capitana Aurora", "aurora@piratas.cat", 82),
                PlayerStatusUi(2L, "Barbanegra", "barbanegra@piratas.cat", 57),
                PlayerStatusUi(3L, "Loba del Mar", "loba@piratas.cat", 39),
                PlayerStatusUi(4L, "Corsario K", "corsario@piratas.cat", 91),
            ),
        )
    }

    private fun observeViewModel() {
        viewModel.partidaActual.observe(viewLifecycleOwner) { partida ->
            val code = partida?.codiPartida ?: "-"
            binding.tvGameCode.text = getString(R.string.game_code_value_format, code)
        }
    }

    private fun setupStaticUi() {
        binding.tvTimeRemaining.text = "Tiempo restante: 09:42"

        binding.btnClose.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.btnMenu.setOnClickListener { }
    }

    private fun showPlayerInfoDialog(player: PlayerStatusUi) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_player_info, null, false)

        val ivProfile = dialogView.findViewById<ImageView>(R.id.ivPlayerProfile)
        val tvName = dialogView.findViewById<TextView>(R.id.tvPlayerInfoName)
        val tvEmail = dialogView.findViewById<TextView>(R.id.tvPlayerInfoEmail)
        val btnCloseDialog = dialogView.findViewById<Button>(R.id.btnClosePlayerInfo)

        ivProfile.contentDescription = "Imagen de perfil de ${player.username}"
        tvName.text = "Nombre: ${player.username}"
        tvEmail.text = "Correo: ${player.email}"

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())

        btnCloseDialog.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}


