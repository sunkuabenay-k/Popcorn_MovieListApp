package com.example.movielist.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.movielist.BuildConfig
import com.example.movielist.data.local.MovieEntity
import com.example.movielist.data.remote.MovieDetailsDto
import com.example.movielist.data.remote.RetrofitInstance
import com.example.movielist.repository.MovieRepositoryImpl
import kotlinx.coroutines.launch

private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w780"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    navController: NavHostController,
    movieId: Int,
    movieRepository: MovieRepositoryImpl
) {
    var selectedTab by remember { mutableStateOf(0) }
    var movie by remember { mutableStateOf<MovieDetailsDto?>(null) }
    var isFavorite by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var userId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(movieId) {
        userId = movieRepository.currentUserId()
        movie = RetrofitInstance.api.getMovieDetails(
            movieId = movieId,
            apiKey = BuildConfig.TMDB_API_KEY
        )
        isFavorite = movieRepository.isFavoriteOnce(movieId)
    }

    movie?.let { m ->

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(m.title, maxLines = 1) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (isFavorite) {
                                        movieRepository.removeFromFavorites(m.toEntity(userId!!))
                                    } else {
                                        movieRepository.addToFavorites(m.toEntity(userId!!))
                                    }
                                    isFavorite = !isFavorite
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isFavorite)
                                    Icons.Filled.Favorite
                                else
                                    Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite"
                            )
                        }
                    }
                )
            }
        ) { padding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {

                /** Poster **/
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = IMAGE_BASE_URL + m.backdrop_path,
                        contentDescription = m.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                /** Title **/
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = m.title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                /** Meta row **/
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = buildMetaText(m),
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                /** Play Button **/
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("▶  Play Now", fontSize = 16.sp)
                    }
                }

                /** Add to List **/
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = {},
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Add to My List")
                    }
                }

                /** Overview **/
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Description",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = m.overview,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }

                /** Extra bottom spacing **/
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    DetailsTabs(
                        selectedTab = selectedTab,
                        onTabSelected = { selectedTab = it }
                    )
                }

                item {
                    when (selectedTab) {
                        0 -> OverviewDetails()
                        1 -> {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "More Like This (coming soon)",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActionItem("My List")
        ActionItem("Rate")
        ActionItem("Share")
    }
}

@Composable
private fun ActionItem(label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = label,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp)
    }
}

@Composable
private fun DetailsTabs(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf("Overview", "More Like This")

    TabRow(
        selectedTabIndex = selectedTab,
        modifier = Modifier.fillMaxWidth()
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = { Text(title) }
            )
        }
    }
}

@Composable
private fun OverviewDetails() {
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "Overview Details",
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    )

    Spacer(modifier = Modifier.height(8.dp))

    Text(
        text = "More detailed plot points and character backgrounds can be presented here. " +
                "This section expands on the initial synopsis, providing deeper insights for the viewer.",
        fontSize = 14.sp,
        lineHeight = 20.sp
    )

    Spacer(modifier = Modifier.height(16.dp))

    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Key Themes:",
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text("• Ambition and ethics")
            Text("• Loyalty and betrayal")
            Text("• The pursuit of justice")
        }
    }
}




/** ---------- Helpers ---------- */

private fun buildMetaText(movie: MovieDetailsDto): String {
    val year = movie.release_date?.take(4) ?: "—"
    val runtime = movie.runtime?.let { "${it / 60}h ${it % 60}m" } ?: ""
    val genre = movie.genres.firstOrNull()?.name ?: ""
    val match = (movie.vote_average * 10).toInt()

    return "$match% Match  ·  $year  ·  $genre  ·  $runtime"
}

private fun MovieDetailsDto.toEntity(userId: String): MovieEntity {
    return MovieEntity(
        id = id,
        userId = userId,
        title = title,
        posterPath = poster_path,
        rating = vote_average
    )
}

