package com.example.nammaskills

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.nammaskills.ui.theme.NammaSkillsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NammaSkillsTheme {
                MainScreen()
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector?) {
    object Home : Screen("home", "Courses", Icons.Default.Home)
    object Centers : Screen("centers", "Centers", Icons.Default.LocationOn)
    object Success : Screen("success", "Success", Icons.Default.Star)
    object Apply : Screen("apply/{courseId}", "Apply", null)
    object Summary : Screen("summary/{name}/{email}/{phone}/{education}/{courseTitle}", "Summary", null)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Namma-Skill", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = { 
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            // Hide bottom bar on form/summary screens if desired, but keeping for now
            BottomNavigationBar(navController, currentRoute) 
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { 
                CourseFinderScreen(onApplyClick = { courseId -> 
                    navController.navigate("apply/$courseId")
                }) 
            }
            composable(Screen.Centers.route) { CenterMapScreen() }
            composable(Screen.Success.route) { SuccessStoriesScreen() }
            composable(
                route = Screen.Apply.route,
                arguments = listOf(navArgument("courseId") { type = NavType.StringType })
            ) { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId")
                val course = SampleData.courses.find { it.id == courseId }
                if (course != null) {
                    ApplicationFormScreen(course = course, onSubmitted = { profile ->
                        navController.navigate("summary/${profile.name}/${profile.email}/${profile.phone}/${profile.education}/${profile.appliedCourse}")
                    })
                }
            }
            composable(
                route = Screen.Summary.route,
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("email") { type = NavType.StringType },
                    navArgument("phone") { type = NavType.StringType },
                    navArgument("education") { type = NavType.StringType },
                    navArgument("courseTitle") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                CandidateSummaryScreen(
                    name = backStackEntry.arguments?.getString("name") ?: "",
                    email = backStackEntry.arguments?.getString("email") ?: "",
                    phone = backStackEntry.arguments?.getString("phone") ?: "",
                    education = backStackEntry.arguments?.getString("education") ?: "",
                    courseTitle = backStackEntry.arguments?.getString("courseTitle") ?: "",
                    onDone = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController, currentRoute: String?) {
    val items = listOf(Screen.Home, Screen.Centers, Screen.Success)
    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon!!, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseFinderScreen(onApplyClick: (String) -> Unit) {
    var selectedCategory by remember { mutableStateOf("All") }
    var selectedDuration by remember { mutableStateOf("All") }
    val categories = listOf("All", "Vocational", "Technical", "Coding", "Creative")
    val durations = listOf("All", "Short-Term", "Long-Term")

    val filteredCourses = SampleData.courses.filter {
        (selectedCategory == "All" || it.category == selectedCategory) &&
        (selectedDuration == "All" || (selectedDuration == "Short-Term" && it.isShortTerm) || (selectedDuration == "Long-Term" && !it.isShortTerm))
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
        item {
            Text("Find Your Trade", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Filter by Category", style = MaterialTheme.typography.titleMedium)
            LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = { Text(category) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            Text("Filter by Duration", style = MaterialTheme.typography.titleMedium)
            LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
                items(durations) { duration ->
                    FilterChip(
                        selected = selectedDuration == duration,
                        onClick = { selectedDuration = duration },
                        label = { Text(duration) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(filteredCourses) { course ->
            CourseCard(course, onApplyClick)
        }
    }
}

@Composable
fun CourseCard(course: Course, onApplyClick: (String) -> Unit) {
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = course.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = course.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                if (course.jobGuarantee) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Job Guarantee",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        " Job Ready",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF4CAF50)
                    )
                }
            }
            Text(course.centerName, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text("Duration: ${course.duration} | Eligibility: ${course.eligibility}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                Button(
                    onClick = {
                        Toast.makeText(context, "Interest pinged! Trainer will call you back.", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Interested")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { onApplyClick(course.id) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Apply Now")
                }
            }
        }
    }
}

@Composable
fun ApplicationFormScreen(course: Course, onSubmitted: (CandidateProfile) -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var education by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registration Form", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Course: ${course.title}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }
        )
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = education,
            onValueChange = { education = it },
            label = { Text("Highest Qualification") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) }
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (name.isNotBlank() && phone.isNotBlank()) {
                    onSubmitted(CandidateProfile(name, email, phone, education, "", course.title))
                } else {
                    Toast.makeText(context, "Please enter your name and phone number", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Generate Summary", fontSize = 18.sp)
        }
    }
}

@Composable
fun CandidateSummaryScreen(
    name: String,
    email: String,
    phone: String,
    education: String,
    courseTitle: String,
    onDone: () -> Unit
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Candidate Summary", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Application Ready for Submission", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SummaryRow("Name", name)
                SummaryRow("Email", email)
                SummaryRow("Phone", phone)
                SummaryRow("Qualification", education)
                Divider(modifier = Modifier.padding(vertical = 12.dp))
                SummaryRow("Selected Trade", courseTitle)
                SummaryRow("Status", "Pending Review")
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = {
                Toast.makeText(context, "Application sent to Center successfully!", Toast.LENGTH_LONG).show()
                onDone()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Confirm & Submit to Center")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SummaryRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(label, modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
        Text(value, modifier = Modifier.weight(1.5f))
    }
}

@Composable
fun CenterMapScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
        item {
            Text("Nearby Skill Centers", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(SampleData.centers) { center ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(center.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                        Text(center.address)
                        Text("Contact: ${center.contact}", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessStoriesScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp)) {
        item {
            Text("Aspirational Stories", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Locals who transformed their lives through Skill India.", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(SampleData.successStories) { story ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(modifier = Modifier.padding(16.dp)) {
                    AsyncImage(
                        model = story.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(40.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(story.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text(story.courseName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                        Text("Placed at: ${story.company}", fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("\"${story.testimonial}\"", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NammaSkillsTheme {
        MainScreen()
    }
}
