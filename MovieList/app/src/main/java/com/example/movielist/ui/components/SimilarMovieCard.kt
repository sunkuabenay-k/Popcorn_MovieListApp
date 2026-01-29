package com.example.movielist.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.movielist.data.remote.MovieDto
import com.example.movielist.navigation.Routes

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

@Composable
fun SimilarMovieCard(
    movie: MovieDto,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable {
                navController.navigate(Routes.details(movie.id))
            },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            AsyncImage(
                model = IMAGE_BASE_URL + movie.poster_path,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f / 3f),
                contentScale = ContentScale.Crop
            )

            Text(
                text = movie.title,
                modifier = Modifier.padding(8.dp),
                maxLines = 1,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
