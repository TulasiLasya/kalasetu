package com.example.kalasetu.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SkillsTabContent(
    skills: List<String>,
) {
    if (skills.isEmpty()) {
        EmptyTabMessage("No skills listed yet")
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        skills.chunked(3).forEach { row ->

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { skill ->

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Purple100)
                            .padding(
                                horizontal = 14.dp,
                                vertical = 8.dp
                            )
                    ) {
                        Text(
                            text = skill,
                            fontSize = 13.sp,
                            color = BrandPurple,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}