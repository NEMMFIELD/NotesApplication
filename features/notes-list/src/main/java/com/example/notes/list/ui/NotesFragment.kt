package com.example.notes.list.ui


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.list.R
import com.example.notes.list.databinding.FragmentNotesBinding
import com.example.state.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NotesFragment : Fragment() {
    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding
    private var notesAdapter: NotesAdapter? = null
    private var recyclerView: RecyclerView? = null
    private val viewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        val view = binding?.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        notesCollect()
        binding?.floatingActionButton?.setOnClickListener { viewModel.addNote() }
    }

    private fun setupRecyclerView() {
        recyclerView = binding!!.notesList
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())

        notesAdapter = NotesAdapter(
            onItemClick = { itemId ->
                Log.d("Notes Adapter", "ItemId is $itemId")
                val request = NavDeepLinkRequest.Builder
                    .fromUri(requireContext().getString(R.string.nav_deep_link, itemId).toUri())
                    .build()
                findNavController().navigate(request)
            },
            onIconClick = { note ->
                viewModel.deleteNote(note)
            })

        recyclerView?.adapter = notesAdapter
    }

    private fun notesCollect() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notes.collect { state ->
                    when (state) {
                        is State.Success -> {
                            notesAdapter?.submitList(state.data)
                        }

                        is State.Failure -> {
                            Log.d("Error", "During load random notes ${state.message}")
                        }

                        else -> {}
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NotesFragment()
    }
}
