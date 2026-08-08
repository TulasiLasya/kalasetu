package com.example.kalasetu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.kalasetu.features.onboarding.ExperienceScreen
import com.example.kalasetu.features.onboarding.InterestsScreen
import com.example.kalasetu.features.onboarding.OnboardingBasicInfoScreen
import com.example.kalasetu.features.onboarding.OnboardingDoneScreen
import com.example.kalasetu.features.onboarding.OnboardingLocationScreen
import com.example.kalasetu.features.onboarding.OnboardingWelcomeScreen
import com.example.kalasetu.features.onboarding.OrganizerIntentScreen
import com.example.kalasetu.features.onboarding.OrganizerTypeScreen
import com.example.kalasetu.features.profile.ProfilePresenter
import com.example.kalasetu.features.profile.ProfileRepository
import com.example.kalasetu.features.profile.ProfileScreen
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
                onBack = { screen = Screen.OnboardingWelcome }
            )

            Screen.OnboardingLocation -> OnboardingLocationScreen(
                onNext = {
                    screen = when (selectedRole) {
                        "Artist"          -> Screen.ArtistExperience
                        "Event Organizer" -> Screen.OrganizerType
                        else              -> Screen.AudienceInterests
                    }
                },
                onBack = { screen = Screen.OnboardingBasicInfo }
            )

            Screen.ArtistExperience -> ExperienceScreen(
                onNext = { screen = Screen.OnboardingDone },
                onBack = { screen = Screen.OnboardingLocation }
            )

            Screen.OrganizerType -> OrganizerTypeScreen(
                onNext = { screen = Screen.OrganizerIntent },
                onBack = { screen = Screen.OnboardingLocation }
            )

            Screen.OrganizerIntent -> OrganizerIntentScreen(
                onNext = { screen = Screen.OnboardingDone },
                onBack = { screen = Screen.OrganizerType }
            )

            Screen.AudienceInterests -> InterestsScreen(
                onNext = { screen = Screen.OnboardingDone },
                onBack = { screen = Screen.OnboardingLocation }
            )

            Screen.OnboardingDone -> OnboardingDoneScreen(
                onFinish = {
                    // TODO: replace "123" with the real userId returned after registration
                    screen = Screen.Profile(userId = "123")
                }
            )

            is Screen.Profile -> {

                val presenter = remember(currentScreen.userId) {
                    ProfilePresenter(repository = ProfileRepository())
                }
                ProfileScreen(
                    presenter = presenter,
                    userId = currentScreen.userId,
                    onEditProfile = { /* screen = Screen.EditProfile */ },
                    onShare = { /* share sheet */ }
                )
            }
        }
    }
}