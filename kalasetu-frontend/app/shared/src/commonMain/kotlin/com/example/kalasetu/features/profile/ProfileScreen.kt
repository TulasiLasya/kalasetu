package com.example.kalasetu.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BrandPurple   = Color(0xFF6100FD)   // buttons, toggle, verified badge
private val LightPurple   = Color(0xFF9D60FF)   // avatar bg, gradient end
private val Purple100     = Color(0xFFEDE7F6)   // light icon circle bg
private val Purple50      = Color(0xFFF3E5F5)   // likes icon bg
private val SurfaceWhite  = Color(0xFFFAFAFA)   // screen background
private val CardWhite     = Color.White
private val TextPrimary   = Color(0xFF1A1A2E)
private val TextSecondary = Color(0xFF6B7280)
private val DividerGray   = Color(0xFFE5E7EB)


@Composable
fun ProfileScreen(
    presenter: ProfilePresenter,
    userId: String,
    onEditProfile: () -> Unit = {},
    onShare: () -> Unit = {}
) {
    var uiState by remember { mutableStateOf(ProfileUiState()) }

    val view = remember {
        object : ProfileContract.View {
            override fun showLoading(isLoading: Boolean) {
                uiState = uiState.copy(isLoading = isLoading)
            }
            override fun showProfile(profile: Profile) {
                uiState = uiState.copy(profile = profile, error = null)
            }
            override fun showError(message: String) {
                uiState = uiState.copy(error = message)
            }
        }
    }

    LaunchedEffect(userId) {
        presenter.attachView(view)
        presenter.loadProfile(userId)
    }

    DisposableEffect(Unit) {
        onDispose { presenter.detach() }
    }

    when {
        uiState.isLoading       -> LoadingContent()
        uiState.error != null   -> ErrorContent(message = uiState.error!!)
        uiState.profile != null -> ProfileContent(
            profile = uiState.profile!!,
            selectedTab = uiState.selectedTab,
            onTabSelected = { tab ->
                uiState = uiState.copy(selectedTab = tab)
                presenter.onTabSelected(tab)
            },
            onEditProfile = onEditProfile,
            onShare = onShare
        )
    }
}


@Composable
private fun ProfileContent(
    profile: Profile,
    selectedTab: ProfileTab,
    onTabSelected: (ProfileTab) -> Unit,
    onEditProfile: () -> Unit,
    onShare: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite)
            .verticalScroll(scrollState)
    ) {
        ProfileHeader(profile = profile, onEditProfile = onEditProfile, onShare = onShare)
        ProfileInfo(profile = profile)
        Spacer(Modifier.height(16.dp))
        StatCardsRow(profile = profile)
        Spacer(Modifier.height(16.dp))
        ProfileTabBar(selected = selectedTab, onTabSelected = onTabSelected)
        Spacer(Modifier.height(12.dp))
        when (selectedTab) {
            ProfileTab.POSTS        -> PostsTabContent(profile)
            ProfileTab.SKILLS       -> SkillsTabContent(profile.skills)
            ProfileTab.ACHIEVEMENTS -> AchievementsTabContent(profile.achievements)
        }
        Spacer(Modifier.height(16.dp))
        ContactSocialCard(profile = profile)
        Spacer(Modifier.height(32.dp))
    }
}


@Composable
private fun ProfileHeader(
    profile: Profile,
    onEditProfile: () -> Unit,
    onShare: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(
                    brush = Brush.linearGradient(listOf( LightPurple, BrandPurple))
                )
        )

        // Avatar
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp)
        ) {
            ProfileAvatar(
                initials = profile.name.toInitials(),
                imageUrl = profile.avatarUrl
            )
        }

        // Share + Edit Profile
        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Share
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(CardWhite)
                    .border(1.dp, DividerGray, CircleShape)
                    .clickable(onClick = onShare),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share profile",
                    tint = TextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Edit Profile
            Button(
                onClick = onEditProfile,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text("Edit Profile", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}


@Composable
private fun ProfileAvatar(initials: String, imageUrl: String?) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .border(3.dp, CardWhite, CircleShape)
            .background(LightPurple),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        // TODO: replace Text with AsyncImage once Coil is added
    }
}


@Composable
private fun ProfileInfo(profile: Profile) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(top = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = profile.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            if (profile.isVerified) {
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Verified",
                    tint = BrandPurple,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(Modifier.height(2.dp))
        Text(text = "@${profile.username}", fontSize = 14.sp, color = TextSecondary)

        Spacer(Modifier.height(6.dp))
        if (profile.location.isNotBlank()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = TextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(text = profile.location, fontSize = 13.sp, color = TextSecondary)
            }
        }

        Spacer(Modifier.height(10.dp))
        if (profile.bio.isNotBlank()) {
            Text(
                text = profile.bio,
                fontSize = 14.sp,
                color = TextPrimary,
                lineHeight = 20.sp
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            InlineStat(value = profile.followers.toDisplayCount(), label = "Followers")
            InlineStat(value = profile.following.toDisplayCount(), label = "Following")
            InlineStat(value = profile.artworksCount.toDisplayCount(), label = "Artworks")
        }
    }
}

@Composable
private fun InlineStat(value: String, label: String) {
    Column {
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Text(text = label, fontSize = 12.sp, color = TextSecondary)
    }
}


@Composable
private fun StatCardsRow(profile: Profile) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Star,
            iconBg = Purple100,
            iconTint = BrandPurple,
            value = profile.artworksCount.toDisplayCount(),
            label = "Artworks"
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Person,
            iconBg = BrandPurple,
            iconTint = Color.White,
            value = profile.followers.toDisplayCount(),
            label = "Followers"
        )
        StatCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.FavoriteBorder,
            iconBg = Purple50,
            iconTint = LightPurple,
            value = profile.totalLikes.toDisplayCount(),
            label = "Likes"
        )
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    value: String,
    label: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = label, fontSize = 12.sp, color = TextSecondary)
        }
    }
}


@Composable
private fun ProfileTabBar(
    selected: ProfileTab,
    onTabSelected: (ProfileTab) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProfileTab.entries.forEach { tab ->
            val isSelected = tab == selected
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) BrandPurple else Color.Transparent)
                    .border(
                        width = if (isSelected) 0.dp else 1.dp,
                        color = if (isSelected) Color.Transparent else DividerGray,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onTabSelected(tab) }
                    .padding(horizontal = 18.dp, vertical = 8.dp)
            ) {
                Text(
                    text = tab.name.lowercase().replaceFirstChar { it.uppercase() },
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (isSelected) Color.White else TextPrimary
                )
            }
        }
    }
}


data class DraftPost(
    val timeAgo: String,
    val content: String,
    val likes: Int,
    val comments: Int,
    val hasImage: Boolean
)

@Composable
private fun PostsTabContent(profile: Profile) {
    val draftPosts = listOf(
        DraftPost(
            timeAgo = "2 days ago",
            content = "Just finished a new character design for an upcoming indie game project. Exploring darker color palettes this time — what do you think?",
            likes = 124,
            comments = 18,
            hasImage = true
        ),
        DraftPost(
            timeAgo = "1 week ago",
            content = "Thrilled to share that my artwork has been selected for the Digital Arts Monthly showcase! Thank you all for the support 🎨✨",
            likes = 342,
            comments = 47,
            hasImage = false
        ),
        DraftPost(
            timeAgo = "2 weeks ago",
            content = "Working on a new series of illustrations inspired by classical Indian art forms. Here's a sneak peek at the first piece in the series.",
            likes = 89,
            comments = 12,
            hasImage = true
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        draftPosts.forEach { post ->
            PostCard(profile = profile, post = post)
        }
    }
}

@Composable
private fun PostCard(profile: Profile, post: DraftPost) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // Header: avatar + name + time
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(LightPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = profile.name.toInitials(),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = profile.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Text(
                        text = post.timeAgo,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }
            }

            // Post text
            Text(
                text = post.content,
                fontSize = 14.sp,
                color = TextPrimary,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 14.dp),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            // Image placeholder
            if (post.hasImage) {
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(Purple100),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Post image",
                        tint = LightPurple,
                        modifier = Modifier.size(40.dp)
                    )
                    // TODO: replace with AsyncImage once Coil is added
                }
            }

            Spacer(Modifier.height(10.dp))

            // Like + comment counts
            Row(
                modifier = Modifier.padding(horizontal = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = BrandPurple,
                    modifier = Modifier.size(13.dp)
                )
                Text(text = "${post.likes} likes", fontSize = 12.sp, color = TextSecondary)
                Text(text = "·", fontSize = 12.sp, color = TextSecondary)
                Text(text = "${post.comments} comments", fontSize = 12.sp, color = TextSecondary)
            }

            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = DividerGray, thickness = 1.dp)

            // Action row: Like / Comment / Share
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                PostActionButton(icon = Icons.Default.ThumbUp, label = "Like")
                PostActionButton(icon = Icons.Default.Star, label = "Comment")
                PostActionButton(icon = Icons.Default.Share, label = "Share")
            }
        }
    }
}

@Composable
private fun PostActionButton(icon: ImageVector, label: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = TextSecondary, modifier = Modifier.size(16.dp))
        Text(text = label, fontSize = 13.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
    }
}


@Composable
private fun SkillsTabContent(skills: List<String>) {
    if (skills.isEmpty()) { EmptyTabMessage("No skills listed yet"); return }
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        skills.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { skill ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Purple100)
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(text = skill, fontSize = 13.sp, color = BrandPurple, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}


@Composable
private fun AchievementsTabContent(achievements: List<Achievement>) {
    if (achievements.isEmpty()) { EmptyTabMessage("No achievements yet"); return }
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        achievements.forEach { AchievementCard(it) }
    }
}

@Composable
private fun AchievementCard(achievement: Achievement) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape).background(Purple100),
                contentAlignment = Alignment.Center
            ) {
                val icon = when (achievement.iconType) {
                    AchievementIcon.TOP_CREATOR -> Icons.Default.CheckCircle
                    AchievementIcon.FOLLOWERS   -> Icons.Default.Person
                    AchievementIcon.FEATURED    -> Icons.Default.Star
                }
                Icon(imageVector = icon, contentDescription = achievement.title, tint = BrandPurple, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(text = achievement.title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                Text(text = achievement.description, fontSize = 13.sp, color = TextSecondary)
            }
        }
    }
}


@Composable
private fun ContactSocialCard(profile: Profile) {
    if (profile.email.isBlank()) return
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(text = "Contact & Social", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = DividerGray, thickness = 1.dp)
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Email, contentDescription = "Email", tint = TextSecondary, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(10.dp))
                Text(text = profile.email, fontSize = 14.sp, color = TextPrimary)
            }
        }
    }
}


@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = BrandPurple)
    }
}

@Composable
private fun ErrorContent(message: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message, color = Color.Red, textAlign = TextAlign.Center)
    }
}

@Composable
private fun EmptyTabMessage(text: String) {
    Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
        Text(text = text, color = TextSecondary)
    }
}