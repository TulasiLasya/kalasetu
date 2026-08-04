package com.example.kalasetu

import androidx.compose.runtime.*
import com.example.kalasetu.features.onboarding.*
import com.example.kalasetu.navigation.Screen
import com.example.kalasetu.theme.KalasetuTheme

@Composable
fun App() {
    var screen by remember { mutableStateOf<Screen>(Screen.OnboardingWelcome) }
    var selectedRole by remember { mutableStateOf("") }

    KalasetuTheme {
        when (screen) {
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
                        "Artist" -> Screen.ArtistExperience
                        "Event Organizer" -> Screen.OrganizerType
                        else -> Screen.AudienceInterests
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
                onFinish = { /* Navigate to main app */ }
            )

            else -> {}
        }
    }
}
