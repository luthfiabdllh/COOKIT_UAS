package com.example.cookit.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookit.util.NotificationAdapter
import com.example.cookit.data.database.NotificationRoomDatabase
import com.example.cookit.databinding.FragmentNotificationsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize the adapter with an empty list
        adapter = NotificationAdapter(emptyList())
        binding.rvNotification.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotification.adapter = adapter

        // Load the notifications from the database
        val db = NotificationRoomDatabase.getDatabase(requireContext())
        lifecycleScope.launch {
            val notificationHistory = withContext(Dispatchers.IO) {
                db.notificationDao().getAllNotifications()
            }
            val messages = notificationHistory.map { it.message }
            adapter.updateData(messages)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}