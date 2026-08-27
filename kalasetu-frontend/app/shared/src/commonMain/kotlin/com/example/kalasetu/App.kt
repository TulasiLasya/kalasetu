package com.example.kalasetu

import androidx.compose.runtime.*
import androidx.compose.material3.*
import com.example.kalasetu.features.onboarding.*
import com.example.kalasetu.features.profile.*
import com.example.kalasetu.navigation.Screen
import com.example.kalasetu.theme.KalasetuTheme

@Composable
fun App() {
    var screen by remember { mutableStateOf<Screen>(Screen.OnboardingWelcome) }
    var selectedRole by remember { mutableStateOf("") }

    KalasetuTheme {
        when (val currentScreen = screen) {

            Screen.OnboardingWelcome -> OnboardingWelcomeScreen(
                onNext = { screen = Screen.OnboardingBasicInfo }
            )

            Screen.OnboardingBasicInfo -> OnboardingBasicInfoScreen(
                onNext = { role ->
                    selectedRole = role
                    screen = Screen.OnboardingLocation
                },
                onBack = { screen = Screen.OnboardingWelcome },
            )

            Screen.OnboardingLocation -> OnboardingLocationScreen(
                onNext = {
                    screen = when (selectedRole) {
                        "Artist"          -> Screen.ArtistExperience
                        "Event Organizer" -> Screen.OrganizerType
                        else              -> Screen.AudienceInterests
                    }
                },
                onBack = { screen = Screen.OnboardingBasicInfo },
            )

            Screen.ArtistExperience -> ExperienceScreen(
                onNext = { screen = Screen.OnboardingDone },
                onBack = { screen = Screen.OnboardingLocation },
            )

            Screen.OrganizerType -> OrganizerTypeScreen(
                onNext = { screen = Screen.OrganizerIntent },
                onBack = { screen = Screen.OnboardingLocation },
            )

            Screen.OrganizerIntent -> OrganizerIntentScreen(
                onNext = { screen = Screen.OnboardingDone },
                onBack = { screen = Screen.OrganizerType },
            )

            Screen.AudienceInterests -> InterestsScreen(
                onNext = { screen = Screen.OnboardingDone },
                onBack = { screen = Screen.OnboardingLocation },
            )

            Screen.OnboardingDone -> OnboardingDoneScreen(
                onFinish = {
                    // Temporary mock user ID for Profile UI development.
                    // Replace with the authenticated user ID when registration/auth is integrated.
                    screen = Screen.Profile(userId = "123")
                }
            )

            is Screen.Profile -> {

                val presenter = remember(currentScreen.userId) {
                    ProfilePresenter(repository = FakeProfileRepository())
                }
                ProfileScreen(
                    presenter = presenter,
                    userId = currentScreen.userId,
                    onEditProfile = { screen = Screen.EditProfile(currentScreen.userId) },
                    onShare = { /* Handle share */ },
                )
            }

            is Screen.EditProfile -> {
                // Placeholder for EditProfileScreen
                Text("Edit Profile for user ${currentScreen.userId}")
            }
        }
    }
}
