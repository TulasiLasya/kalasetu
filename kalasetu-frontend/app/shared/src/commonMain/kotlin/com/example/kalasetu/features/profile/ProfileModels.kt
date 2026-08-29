package com.example.kalasetu.features.profile

data class Profile(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val location: String = "",
    val bio: String = "",
    val avatarUrl: String? = null,
    val followers: Int = 0,
    val following: Int = 0,
    val artworksCount: Int = 0,
    val totalLikes: Int = 0,
    val email: String = "",
    val isVerified: Boolean = false,
    val skills: List<String> = emptyList(),
    val artworksImages: List<String> = emptyList(),
    val achievements: List<Achievement> = emptyList()
)

data class Achievement(
    val title: String,
    val description: String,
    val iconType: AchievementIcon = AchievementIcon.TOP_CREATOR
)

enum class AchievementIcon {
    TOP_CREATOR, FOLLOWERS, FEATURED
}

enum class ProfileTab {
    POSTS, SKILLS, ACHIEVEMENTS
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: Profile? = null,
    val error: String? = null,
    val selectedTab: ProfileTab = ProfileTab.ACHIEVEMENTS
)

fun String.toInitials(): String {
    if (isBlank()) return ""
    val parts = trim()
        .split(Regex("[\\s-]+"))
        .filter { it.isNotBlank() }

    if (parts.isEmpty()) return ""

    val firstInitial = parts.first().first().uppercaseChar()
    if (parts.size == 1) return firstInitial.toString()

    val lastInitial = parts.last().first().uppercaseChar()
    return "$firstInitial$lastInitial"
}
fun Int.toDisplayCount(): String = when {
    this >= 1_000_000 -> {
        val m = this / 1_000_000.0
        val s = (m * 10).toInt()
        if (s % 10 == 0) "${s / 10}M" else "${s / 10}.${s % 10}M"
    }
    this >= 1_000 -> {
        val k = this / 1_000.0
        val s = (k * 10).toInt()
        if (s % 10 == 0) "${s / 10}K" else "${s / 10}.${s % 10}K"
    }
    else -> this.toString()
}

interface ProfileRepositoryContract {
    suspend fun fetchProfile(userId: String): Profile
}