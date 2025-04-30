package com.example.notes.edit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.notes.edit.databinding.FragmentNotesEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNotesFragment : Fragment() {
    private var _binding: FragmentNotesEditBinding? = null
    private val binding get() = _binding
    private val editNotesViewModel: EditNotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesEditBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            this?.btnSaveEdit?.setOnClickListener {
                if (!this.editAuthor.text.isNullOrEmpty() && !this.editNote.text.isNullOrEmpty()) {
                    editNotesViewModel.editNote()

                } else {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
        editNotesViewModel.editFinished.observe(viewLifecycleOwner) { success ->
            if (success) findNavController().navigateUp()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = EditNotesFragment()
    }
}
