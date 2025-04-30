package com.example.notes.list.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.list.data.NoteModel
import com.example.notes.list.databinding.NotesItemLayoutBinding

class NotesAdapter(
    private val onItemClick: (String) -> Unit,
    private val onIconClick: (NoteModel) -> Unit
) :
    ListAdapter<NoteModel, NotesAdapter.ViewHolder>(DiffCallback()) {
    inner class ViewHolder(private val binding: NotesItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: NoteModel) {
            with(binding) {
                noteAuthor.text = model.author
                noteText.text = model.noteText
                deleteButton?.setOnClickListener { onIconClick(model) }
            }
            itemView.setOnClickListener {
                onItemClick(model.id.toString())
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NotesAdapter.ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val binding = NotesItemLayoutBinding.inflate(inflater, p0, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: NotesAdapter.ViewHolder, p1: Int) {
        p0.bind(getItem(p1))
    }
}

private class DiffCallback : DiffUtil.ItemCallback<NoteModel>() {
    private val payLoad: Any = Any()
    override fun areItemsTheSame(p0: NoteModel, p1: NoteModel): Boolean {
        return p0.id == p1.id
    }

    override fun areContentsTheSame(p0: NoteModel, p1: NoteModel): Boolean {
        return p0 == p1
    }

    override fun getChangePayload(oldItem: NoteModel, newItem: NoteModel): Any {
        return payLoad
    }
}
