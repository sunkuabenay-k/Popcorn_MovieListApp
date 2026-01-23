package com.example.movielist.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnimatedEyes(
    modifier: Modifier = Modifier,
    pointerOffset: Offset?
) {
    val infiniteTransition = rememberInfiniteTransition(label = "eyePulse")

    // Pulsing alpha for the pupils
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pupilAlpha"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EyeWithEyebrow(pointerOffset, alpha)
        EyeWithEyebrow(pointerOffset, alpha)
    }
}

@Composable
private fun EyeWithEyebrow(pointerOffset: Offset?, pupilAlpha: Float) {
    // Calculate pupil movement
    // We limit the movement to 12dp so the pupil stays inside the eye socket
    val pupilMovementLimit = 12f

    val targetOffset = remember(pointerOffset) {
        if (pointerOffset != null) {
            // Simple logic to determine direction towards the pointer
            // We use a small portion of the offset to simulate "looking"
            Offset(
                x = (pointerOffset.x / 100f).coerceIn(-pupilMovementLimit, pupilMovementLimit),
                y = (pointerOffset.y / 100f).coerceIn(-pupilMovementLimit, pupilMovementLimit)
            )
        } else {
            Offset.Zero
        }
    }

    val animatedPupilOffset by animateOffsetAsState(
        targetValue = targetOffset,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "pupilTracking"
    )

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Eyebrow
        Box(
            modifier = Modifier
                .width(45.dp)
                .height(6.dp)
                .background(Color.Black, RoundedCornerShape(3.dp))
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Eye Socket
        Box(
            modifier = Modifier
                .size(55.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Pupil
            Box(
                modifier = Modifier
                    .offset(animatedPupilOffset.x.dp, animatedPupilOffset.y.dp)
                    .size(22.dp)
                    .alpha(pupilAlpha)
                    .background(Color.Black, CircleShape)
            )
        }
    }
}