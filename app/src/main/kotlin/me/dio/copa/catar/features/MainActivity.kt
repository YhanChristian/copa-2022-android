package me.dio.copa.catar.features

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint
import me.dio.copa.catar.extensions.observe
import me.dio.copa.catar.notification.scheduler.extensions.NotificationMatcherWorker
import me.dio.copa.catar.ui.theme.Copa2022Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Copa2022Theme {
                val state by viewModel.state.collectAsState()
                Log.d(TAG, "onCreate: ${state.matches}")
                MainScreen(matches = state.matches, viewModel::toggleNotification)
                observeActions()
            }
        }
    }

    private fun observeActions() {
        viewModel.action.observe(this) { action ->
            when (action) {
                is MainUiAction.MatchesNotFound -> TODO()
                is MainUiAction.DisableNotification ->
                    NotificationMatcherWorker.cancel(applicationContext, action.match)
                is MainUiAction.EnableNotification ->
                    NotificationMatcherWorker.start(applicationContext, action.match)

               is MainUiAction.UnexpectedError -> TODO()
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
