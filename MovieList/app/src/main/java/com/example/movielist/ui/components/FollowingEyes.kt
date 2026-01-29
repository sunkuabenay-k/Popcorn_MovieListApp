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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedEyes(
    modifier: Modifier = Modifier,
    pointerOffset: Offset?
) {
    val infiniteTransition = rememberInfiniteTransition(label = "eyePulse")


    var eyePosition by remember { mutableStateOf(Offset.Zero) }

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
        modifier = modifier.onGloballyPositioned { coordinates ->
            eyePosition = coordinates.positionInRoot()
        },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            EyeWithEyebrow(pointerOffset, alpha, eyePosition)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            EyeWithEyebrow(pointerOffset, alpha, eyePosition)
        }
    }
}

@Composable
private fun EyeWithEyebrow(
    pointerOffset: Offset?,
    pupilAlpha: Float,
    eyeGroupPosition: Offset
) {
    val targetOffset = remember(pointerOffset, eyeGroupPosition) {
        if (pointerOffset != null) {
            val sensitivity = 14f

            val dx = pointerOffset.x - (eyeGroupPosition.x)
            val dy = pointerOffset.y - (eyeGroupPosition.y)

            Offset(
                x = (dx / 20f).coerceIn(-sensitivity, sensitivity),
                y = (dy / 20f).coerceIn(-sensitivity, sensitivity)
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

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(Color.Black, RoundedCornerShape(50))
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Eye Sclera (White part)
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Pupil
            Box(
                modifier = Modifier
                    .offset(animatedPupilOffset.x.dp, animatedPupilOffset.y.dp)
                    .fillMaxSize(0.45f)
                    .alpha(pupilAlpha)
                    .background(Color.Black, CircleShape)
            )
        }
    }
}