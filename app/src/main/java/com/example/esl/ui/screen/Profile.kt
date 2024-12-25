import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.esl.ui.component.BottomNavBar
import com.example.esl.ui.component.Screen
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen(
    navController: NavController,

) {
    val viewModel: ProfileViewModel = viewModel()
    // Get username from viewModel or shared preferences
    val username by remember { mutableStateOf("Username") } // Ganti dengan data aktual dari viewModel
    val profileImageUri by viewModel.profileImageUri

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        // Box untuk memberikan padding dari Scaffold
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFB2EBF2))
                .padding(innerPadding)
        ) {
            ProfileContent(
                username = username,
                profileImageUri = profileImageUri,
                onImageSelected = { uri -> viewModel.updateProfileImage(uri) },
                onLogout = {
                    // Implementasi logout
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                }
            )
        }
    }
}

@Composable
private fun ProfileContent(
    username: String,
    profileImageUri: Uri?,
    onImageSelected: (Uri) -> Unit,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Profile Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Image with default icon if no image is selected
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                ) {
                    if (profileImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(data = profileImageUri)
                                    .build()
                            ),
                            contentDescription = "Profile Picture",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Default Profile Picture",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxSize(),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    // Camera icon
                    val imagePickerLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.GetContent()
                    ) { uri: Uri? ->
                        uri?.let { onImageSelected(it) }
                    }

                    IconButton(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = "Change profile picture",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = username,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        // Settings Options
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF40E0D0)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                SettingsItem(
                    icon = Icons.Default.Lock,
                    text = "Ubah Password",
                    onClick = { /* Handle password change */ }
                )

                Divider(color = Color.White.copy(alpha = 0.2f))

                SettingsItem(
                    icon = Icons.Default.Favorite,
                    text = "Favorit",
                    onClick = { /* Handle favorites */ }
                )

                Divider(color = Color.White.copy(alpha = 0.2f))

                SettingsItem(
                    icon = Icons.Default.ExitToApp,
                    text = "Logout",
                    onClick = onLogout
                )
            }
        }
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = Color.White
            )
        }
    }
}


// ViewModel untuk mengelola state profil
class ProfileViewModel : ViewModel() {
    private val _profileImageUri = mutableStateOf<Uri?>(null)
    val profileImageUri: State<Uri?> = _profileImageUri

    fun updateProfileImage(uri: Uri) {
        viewModelScope.launch {
            _profileImageUri.value = uri
            // Implementasi untuk menyimpan URI gambar ke backend
        }
    }
}