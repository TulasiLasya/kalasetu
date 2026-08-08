package com.example.kalasetu.features.profile

class ProfileRepository {
    suspend fun fetchProfile(userId: String): Profile {
        return Profile(
            id = userId,
            name = "Sarah Anderson",
            username = "sarahart",
            location = "San Francisco, CA",
            bio = "Digital artist & illustrator passionate about creating vibrant character designs and exploring new visual narratives. Available for commissions and collaborations.",
            avatarUrl = null,
            followers = 2847,
            following = 892,
            artworksCount = 156,
            totalLikes = 12400,
            email = "sarah.anderson@email.com",
            skills = listOf(
                "Digital Art", "Illustration", "Character Design",
                "Concept Art", "Storyboarding", "Visual Development",
                "Graphic Design", "UI/UX", "Animation"
            ),
            artworksImages = listOf(
                "https://picsum.photos/200/200?random=1",
                "https://picsum.photos/200/200?random=2",
                "https://picsum.photos/200/200?random=3",
                "https://picsum.photos/200/200?random=4",
                "https://picsum.photos/200/200?random=5",
                "https://picsum.photos/200/200?random=6",
                "https://picsum.photos/200/200?random=7",
                "https://picsum.photos/200/200?random=8",
                "https://picsum.photos/200/200?random=9"
            ),
            achievements = listOf(
                Achievement(" Top Creator 2024", "Recognized as top 1% creator"),
                Achievement(" 1K Followers", "Reached 1000 followers milestone"),
                Achievement(" Featured Artist", "Featured in monthly showcase")
            )
        )
    }
}