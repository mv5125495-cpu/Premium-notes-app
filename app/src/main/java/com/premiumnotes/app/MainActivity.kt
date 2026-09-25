@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.premiumnotes.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Briefcase
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.premiumnotes.app.data.Note
import com.premiumnotes.app.data.adminMetrics
import com.premiumnotes.app.data.sampleCategories
import com.premiumnotes.app.data.sampleNotes
import com.premiumnotes.app.ui.PremiumNotesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { PremiumNotesTheme { PremiumNotesApp() } }
    }
}

enum class AppScreen { Splash, Login, Signup, Home, Search, Categories, Bookmarks, Note, Profile, Settings, AdminLogin, AdminDashboard }

@Composable
fun PremiumNotesApp() {
    var darkMode by rememberSaveable { mutableStateOf(false) }
    PremiumNotesTheme(darkTheme = darkMode) {
    var screen by rememberSaveable { mutableStateOf(AppScreen.Splash) }
    var selectedNoteId by rememberSaveable { mutableStateOf(1) }
    var bookmarkedIds by rememberSaveable { mutableStateOf(setOf(1, 3)) }
    var splashVisible by rememberSaveable { mutableStateOf(true) }

    if (splashVisible) {
        SplashScreen(onFinished = { splashVisible = false; screen = AppScreen.Login })
        return
    }

    AnimatedContent(targetState = screen, label = "screen") { current ->
        when (current) {
            AppScreen.Login -> LoginScreen(onLogin = { screen = AppScreen.Home }, onSignup = { screen = AppScreen.Signup }, onAdmin = { screen = AppScreen.AdminLogin })
            AppScreen.Signup -> SignupScreen(onBack = { screen = AppScreen.Login }, onComplete = { screen = AppScreen.Home })
            AppScreen.Home, AppScreen.Search, AppScreen.Categories, AppScreen.Bookmarks, AppScreen.Profile, AppScreen.Settings -> MainShell(
                screen = current,
                darkMode = darkMode,
                bookmarkedIds = bookmarkedIds,
                onNavigate = { screen = it },
                onOpenNote = { selectedNoteId = it; screen = AppScreen.Note },
                onToggleBookmark = { id -> bookmarkedIds = if (id in bookmarkedIds) bookmarkedIds - id else bookmarkedIds + id },
                onToggleDark = { darkMode = it }
            )
            AppScreen.Note -> NoteDetailScreen(note = sampleNotes.first { it.id == selectedNoteId }, isBookmarked = selectedNoteId in bookmarkedIds, onBack = { screen = AppScreen.Home }, onToggleBookmark = { bookmarkedIds = if (selectedNoteId in bookmarkedIds) bookmarkedIds - selectedNoteId else bookmarkedIds + selectedNoteId })
            AppScreen.AdminLogin -> AdminLoginScreen(onBack = { screen = AppScreen.Login }, onLogin = { screen = AppScreen.AdminDashboard })
            AppScreen.AdminDashboard -> AdminDashboardScreen(onLogout = { screen = AppScreen.Login })
            AppScreen.Splash -> Unit
        }
    }
    }
}

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var visible by rememberSaveable { mutableStateOf(true) }
    androidx.compose.runtime.LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(900)
        visible = false
        kotlinx.coroutines.delay(250)
        onFinished()
    }
    Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center) {
        AnimatedVisibility(visible, enter = fadeIn() + slideInVertically { it / 3 }, exit = fadeOut()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.size(76.dp).clip(RoundedCornerShape(24.dp)).background(Color.White), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.MenuBook, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(38.dp))
                }
                Spacer(Modifier.height(18.dp))
                Text("Premium Notes", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("Make space for what matters", color = Color.White.copy(alpha = .75f), fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun LoginScreen(onLogin: () -> Unit, onSignup: () -> Unit, onAdmin: () -> Unit) {
    AuthLayout(title = "Welcome back", subtitle = "Your thoughts are waiting for you.") {
        var email by rememberSaveable { mutableStateOf("alex@example.com") }
        var password by rememberSaveable { mutableStateOf("password") }
        OutlinedTextField(email, { email = it }, Modifier.fillMaxWidth(), label = { Text("Email address") }, singleLine = true)
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(password, { password = it }, Modifier.fillMaxWidth(), label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), singleLine = true)
        TextButton({ }, Modifier.align(Alignment.End)) { Text("Forgot password?") }
        Button(onLogin, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp)) { Text("Sign in", fontWeight = FontWeight.SemiBold) }
        Spacer(Modifier.height(22.dp))
        Row(verticalAlignment = Alignment.CenterVertically) { Divider(Modifier.weight(1f)); Text("  or  ", color = MaterialTheme.colorScheme.onSurfaceVariant); Divider(Modifier.weight(1f)) }
        Spacer(Modifier.height(18.dp))
        OutlinedButton(onSignup, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp)) { Text("Create an account") }
        TextButton(onAdmin, Modifier.fillMaxWidth()) { Icon(Icons.Default.Security, null, Modifier.size(16.dp)); Spacer(Modifier.width(6.dp)); Text("Admin access") }
    }
}

@Composable
fun SignupScreen(onBack: () -> Unit, onComplete: () -> Unit) {
    AuthLayout(title = "Create your space", subtitle = "A quieter place for your best ideas.", onBack = onBack) {
        OutlinedTextField("Alex Morgan", {}, Modifier.fillMaxWidth(), label = { Text("Full name") }, singleLine = true)
        Spacer(Modifier.height(14.dp))
        OutlinedTextField("alex@example.com", {}, Modifier.fillMaxWidth(), label = { Text("Email address") }, singleLine = true)
        Spacer(Modifier.height(14.dp))
        OutlinedTextField("Create a password", {}, Modifier.fillMaxWidth(), label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), singleLine = true)
        Spacer(Modifier.height(24.dp))
        Button(onComplete, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp)) { Text("Start writing", fontWeight = FontWeight.SemiBold) }
    }
}

@Composable
fun AuthLayout(title: String, subtitle: String, onBack: (() -> Unit)? = null, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().statusBarsPadding().padding(horizontal = 24.dp).padding(top = 22.dp), verticalArrangement = Arrangement.Center) {
        if (onBack != null) IconButton(onBack) { Icon(Icons.Default.ArrowBack, "Back") }
        Icon(Icons.Default.MenuBook, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(34.dp))
        Spacer(Modifier.height(24.dp))
        Text(title, fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp, bottom = 30.dp))
        content()
    }
}

@Composable
fun MainShell(screen: AppScreen, darkMode: Boolean, bookmarkedIds: Set<Int>, onNavigate: (AppScreen) -> Unit, onOpenNote: (Int) -> Unit, onToggleBookmark: (Int) -> Unit, onToggleDark: (Boolean) -> Unit) {
    Scaffold(bottomBar = { BottomNavigation(screen, onNavigate) }) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (screen) {
                AppScreen.Home -> HomeScreen(bookmarkedIds, onOpenNote, onToggleBookmark, onNavigate)
                AppScreen.Search -> SearchScreen(bookmarkedIds, onOpenNote, onToggleBookmark)
                AppScreen.Categories -> CategoriesScreen(onNavigate, onOpenNote)
                AppScreen.Bookmarks -> BookmarksScreen(bookmarkedIds, onOpenNote, onToggleBookmark)
                AppScreen.Profile -> ProfileScreen(onNavigate)
                AppScreen.Settings -> SettingsScreen(darkMode, onToggleDark, onNavigate)
                else -> Unit
            }
        }
    }
}

@Composable
fun BottomNavigation(current: AppScreen, onNavigate: (AppScreen) -> Unit) {
    Surface(shadowElevation = 8.dp) {
        Row(Modifier.fillMaxWidth().navigationBarsPadding().padding(horizontal = 12.dp, vertical = 10.dp), horizontalArrangement = Arrangement.SpaceAround) {
            NavItem("Home", Icons.Default.Home, current == AppScreen.Home) { onNavigate(AppScreen.Home) }
            NavItem("Search", Icons.Default.Search, current == AppScreen.Search) { onNavigate(AppScreen.Search) }
            NavItem("Saved", Icons.Default.Bookmark, current == AppScreen.Bookmarks) { onNavigate(AppScreen.Bookmarks) }
            NavItem("Profile", Icons.Default.PersonOutline, current == AppScreen.Profile) { onNavigate(AppScreen.Profile) }
        }
    }
}

@Composable
fun NavItem(label: String, icon: ImageVector, selected: Boolean, onClick: () -> Unit) {
    Column(Modifier.clip(RoundedCornerShape(12.dp)).clickable(onClick = onClick).padding(horizontal = 18.dp, vertical = 4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, label, tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
        Text(label, fontSize = 11.sp, color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun HomeScreen(bookmarkedIds: Set<Int>, onOpenNote: (Int) -> Unit, onToggleBookmark: (Int) -> Unit, onNavigate: (AppScreen) -> Unit) {
    LazyColumn(Modifier.fillMaxSize().statusBarsPadding(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Column { Text("Good morning, Alex", color = MaterialTheme.colorScheme.onSurfaceVariant); Text("Your thinking space", fontSize = 26.sp, fontWeight = FontWeight.Bold) }; Box(Modifier.size(44.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) { Text("AM", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) } } }
        item { Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary), shape = RoundedCornerShape(22.dp), modifier = Modifier.fillMaxWidth()) { Column(Modifier.padding(20.dp)) { Text("Your weekly reflection", color = Color.White.copy(alpha = .78f)); Text("You captured 12 ideas this week", color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp)); Row(verticalAlignment = Alignment.CenterVertically) { Text("Keep the momentum going", color = Color.White.copy(alpha = .85f), fontSize = 13.sp); Spacer(Modifier.weight(1f)); Icon(Icons.Default.ArrowForward, null, tint = Color.White) } } } }
        item { SectionHeader("Categories", "See all") { onNavigate(AppScreen.Categories) } }
        item { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) { sampleCategories.take(4).forEach { category -> CategoryTile(category.name, category.count, category.icon, category.color) } } }
        item { SectionHeader("Recent notes", "View all") { onNavigate(AppScreen.Search) } }
        items(sampleNotes.take(3), key = { it.id }) { note -> NoteCard(note.copy(isBookmarked = note.id in bookmarkedIds), onOpenNote, onToggleBookmark) }
    }
}

@Composable
fun SectionHeader(title: String, action: String, onAction: () -> Unit) { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(title, fontSize = 19.sp, fontWeight = FontWeight.Bold); TextButton(onAction) { Text(action) } } }

@Composable
fun CategoryTile(name: String, count: Int, icon: String, color: Long) { val vector = when (icon) { "briefcase" -> Icons.Default.Briefcase; "person" -> Icons.Default.Person; "lightbulb" -> Icons.Default.Lightbulb; else -> Icons.Default.School }; Column(Modifier.width(84.dp).clip(RoundedCornerShape(16.dp)).background(Color(color)).padding(12.dp)) { Icon(vector, null, tint = Color(0xFF293346)); Spacer(Modifier.height(12.dp)); Text(name, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color(0xFF293346)); Text("$count notes", fontSize = 11.sp, color = Color(0xFF596273)) } }

@Composable
fun NoteCard(note: Note, onOpenNote: (Int) -> Unit, onToggleBookmark: (Int) -> Unit) { Card(Modifier.fillMaxWidth().clickable { onOpenNote(note.id) }, shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) { Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) { Box(Modifier.size(4.dp, 72.dp).clip(RoundedCornerShape(4.dp)).background(Color(note.color))); Spacer(Modifier.width(14.dp)); Column(Modifier.weight(1f)) { Text(note.category.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary); Text(note.title, fontWeight = FontWeight.Bold, fontSize = 17.sp, modifier = Modifier.padding(vertical = 4.dp)); Text(note.preview, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis, fontSize = 13.sp); Text(note.updated, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp, modifier = Modifier.padding(top = 9.dp)) }; IconButton({ onToggleBookmark(note.id) }) { Icon(if (note.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder, "Bookmark", tint = if (note.isBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant) } } } }

@Composable
fun SearchScreen(bookmarkedIds: Set<Int>, onOpenNote: (Int) -> Unit, onToggleBookmark: (Int) -> Unit) { var query by rememberSaveable { mutableStateOf("") }; var selected by rememberSaveable { mutableStateOf("All") }; val filtered = sampleNotes.filter { (selected == "All" || it.category == selected) && (query.isBlank() || it.title.contains(query, true) || it.preview.contains(query, true)) }; Column(Modifier.fillMaxSize().statusBarsPadding().padding(20.dp)) { Text("Find a note", fontSize = 28.sp, fontWeight = FontWeight.Bold); OutlinedTextField(query, { query = it }, Modifier.fillMaxWidth().padding(vertical = 16.dp), placeholder = { Text("Search your notes") }, leadingIcon = { Icon(Icons.Default.Search, null) }, singleLine = true, shape = RoundedCornerShape(14.dp)); Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("All", "Work", "Personal", "Ideas").forEach { FilterChip(selected == it, { selected = it }, label = { Text(it) }) } }; LazyColumn(Modifier.padding(top = 16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { items(filtered) { NoteCard(it.copy(isBookmarked = it.id in bookmarkedIds), onOpenNote, onToggleBookmark) } } } }

@Composable
fun CategoriesScreen(onNavigate: (AppScreen) -> Unit, onOpenNote: (Int) -> Unit) { LazyColumn(Modifier.fillMaxSize().statusBarsPadding().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) { item { Text("Categories", fontSize = 28.sp, fontWeight = FontWeight.Bold); Text("A place for every kind of thought", color = MaterialTheme.colorScheme.onSurfaceVariant) }; items(sampleCategories) { category -> Card(Modifier.fillMaxWidth().clickable { onNavigate(AppScreen.Search) }, shape = RoundedCornerShape(18.dp)) { Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) { CategoryTile(category.name, category.count, category.icon, category.color); Spacer(Modifier.weight(1f)); Icon(Icons.Default.KeyboardArrowRight, null) } } }; item { Text("Latest in your library", fontWeight = FontWeight.Bold, fontSize = 19.sp); NoteCard(sampleNotes.first(), onOpenNote) {} } } }

@Composable
fun BookmarksScreen(bookmarkedIds: Set<Int>, onOpenNote: (Int) -> Unit, onToggleBookmark: (Int) -> Unit) { val saved = sampleNotes.filter { it.id in bookmarkedIds }; LazyColumn(Modifier.fillMaxSize().statusBarsPadding().padding(20.dp), contentPadding = PaddingValues(bottom = 20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) { item { Text("Saved notes", fontSize = 28.sp, fontWeight = FontWeight.Bold); Text("Your small library of things worth returning to", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)) }; items(saved) { NoteCard(it.copy(isBookmarked = true), onOpenNote, onToggleBookmark) }; if (saved.isEmpty()) item { EmptyState("No saved notes yet", "Tap the bookmark icon on a note to keep it close.") } } }

@Composable
fun NoteDetailScreen(note: Note, isBookmarked: Boolean, onBack: () -> Unit, onToggleBookmark: () -> Unit) { Scaffold(topBar = { SmallTopAppBar(title = { Text(note.category) }, navigationIcon = { IconButton(onBack) { Icon(Icons.Default.ArrowBack, "Back") } }, actions = { IconButton(onToggleBookmark) { Icon(if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder, "Bookmark") }; IconButton({}) { Icon(Icons.Default.MoreHoriz, "More") } }) }) { padding -> LazyColumn(Modifier.padding(padding).fillMaxSize().padding(horizontal = 24.dp), contentPadding = PaddingValues(bottom = 24.dp)) { item { Box(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)).background(Color(note.color))); Text(note.title, fontSize = 31.sp, lineHeight = 37.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 24.dp)); Text(note.updated, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp, modifier = Modifier.padding(top = 10.dp, bottom = 30.dp)); note.body.split("\n\n").forEach { paragraph -> item { Text(paragraph, fontSize = 17.sp, lineHeight = 28.sp, modifier = Modifier.padding(bottom = 22.dp)) } } } } } }

@Composable
fun ProfileScreen(onNavigate: (AppScreen) -> Unit) { LazyColumn(Modifier.fillMaxSize().statusBarsPadding().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) { item { Text("Profile", fontSize = 28.sp, fontWeight = FontWeight.Bold); Spacer(Modifier.height(14.dp)); Row(verticalAlignment = Alignment.CenterVertically) { Box(Modifier.size(72.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer), contentAlignment = Alignment.Center) { Text("AM", color = MaterialTheme.colorScheme.primary, fontSize = 22.sp, fontWeight = FontWeight.Bold) }; Spacer(Modifier.width(16.dp)); Column { Text("Alex Morgan", fontWeight = FontWeight.Bold, fontSize = 20.sp); Text("alex@example.com", color = MaterialTheme.colorScheme.onSurfaceVariant) } } }; item { Spacer(Modifier.height(10.dp)); ProfileRow(Icons.Default.Settings, "Settings") { onNavigate(AppScreen.Settings) }; ProfileRow(Icons.Default.NotificationsNone, "Notifications") {}; ProfileRow(Icons.Default.Favorite, "Premium plan") {}; ProfileRow(Icons.Default.Send, "Invite a friend") {} } } }

@Composable
fun ProfileRow(icon: ImageVector, label: String, onClick: () -> Unit) { Row(Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).clickable(onClick = onClick).padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, null, tint = MaterialTheme.colorScheme.primary); Spacer(Modifier.width(16.dp)); Text(label, Modifier.weight(1f), fontWeight = FontWeight.Medium); Icon(Icons.Default.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) } }

@Composable
fun SettingsScreen(darkMode: Boolean, onToggleDark: (Boolean) -> Unit, onNavigate: (AppScreen) -> Unit) { Column(Modifier.fillMaxSize().statusBarsPadding().padding(20.dp)) { Text("Settings", fontSize = 28.sp, fontWeight = FontWeight.Bold); Text("Make Premium Notes feel like yours", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp, bottom = 26.dp)); SettingsRow(Icons.Default.DarkMode, "Dark mode", "Use a darker appearance", Switch(checked = darkMode, onCheckedChange = onToggleDark)); SettingsRow(Icons.Default.Tune, "Preferences", "Default category, editor and more"); SettingsRow(Icons.Default.Security, "Privacy & security", "Your notes stay yours"); SettingsRow(Icons.Default.Inbox, "Export notes", "Download a copy of your library") } }

@Composable
fun SettingsRow(icon: ImageVector, title: String, subtitle: String, control: @Composable (() -> Unit)? = null) { Row(Modifier.fillMaxWidth().padding(vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) { Icon(icon, null, tint = MaterialTheme.colorScheme.primary); Spacer(Modifier.width(16.dp)); Column(Modifier.weight(1f)) { Text(title, fontWeight = FontWeight.SemiBold); Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant) }; control?.invoke() ?: Icon(Icons.Default.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant) } }

@Composable
fun AdminLoginScreen(onBack: () -> Unit, onLogin: () -> Unit) { AuthLayout("Admin workspace", "Manage your Premium Notes community.", onBack) { OutlinedTextField("admin@premiumnotes.app", {}, Modifier.fillMaxWidth(), label = { Text("Admin email") }, singleLine = true); Spacer(Modifier.height(14.dp)); OutlinedTextField("••••••••", {}, Modifier.fillMaxWidth(), label = { Text("Password") }, visualTransformation = PasswordVisualTransformation(), singleLine = true); Spacer(Modifier.height(24.dp)); Button(onLogin, Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp)) { Text("Open dashboard") }; Text("Sample access for learning purposes", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)) } }

@Composable
fun AdminDashboardScreen(onLogout: () -> Unit) { var section by rememberSaveable { mutableStateOf("Overview") }; Scaffold(topBar = { SmallTopAppBar(title = { Text("Admin Panel") }, navigationIcon = { Icon(Icons.Default.SupervisedUserCircle, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(start = 16.dp)) }, actions = { TextButton(onClick = onLogout) { Text("Log out") } }) }) { padding -> Row(Modifier.padding(padding).fillMaxSize()) { Column(Modifier.width(112.dp).fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant).padding(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) { listOf("Overview", "Users", "Notes", "Categories", "Announcements").forEach { item -> Text(item, Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp)).clickable { section = item }.background(if (section == item) MaterialTheme.colorScheme.primary else Color.Transparent).padding(10.dp), color = if (section == item) Color.White else MaterialTheme.colorScheme.onSurface, fontSize = 12.sp, fontWeight = if (section == item) FontWeight.Bold else FontWeight.Normal) } }; Column(Modifier.weight(1f).padding(18.dp)) { Text(section, fontSize = 27.sp, fontWeight = FontWeight.Bold); Text("Keep an eye on what is growing", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)); if (section == "Overview") { adminMetrics.forEach { metric -> MetricCard(metric.label, metric.value, metric.change) } } else { AdminSection(section) } } } } }

@Composable
fun MetricCard(label: String, value: String, change: String) { Card(Modifier.fillMaxWidth().padding(bottom = 12.dp), shape = RoundedCornerShape(16.dp)) { Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) { Column(Modifier.weight(1f)) { Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp); Text(value, fontSize = 25.sp, fontWeight = FontWeight.Bold) }; AssistChip(onClick = {}, label = { Text(change, fontSize = 11.sp) }, leadingIcon = { Icon(Icons.Default.TrendingUp, null, Modifier.size(14.dp)) }) } } }

@Composable
fun AdminSection(section: String) { val details = when (section) { "Users" -> "Manage member access, roles, and account activity."; "Notes" -> "Review note volume, reports, and recent activity."; "Categories" -> "Organize the library with clear, useful collections."; else -> "Share product updates and thoughtful messages with users." }; Column { Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) { Column(Modifier.padding(20.dp)) { Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.primary); Text(details, fontSize = 17.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 16.dp)); Text("Sample data is ready for your next implementation step.", color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp)) } }; Spacer(Modifier.height(16.dp)); Button({}, Modifier.fillMaxWidth()) { Text(if (section == "Announcements") "Create announcement" else "View all $section") } } }

@Composable
fun EmptyState(title: String, subtitle: String) { Column(Modifier.fillMaxWidth().padding(top = 80.dp), horizontalAlignment = Alignment.CenterHorizontally) { Icon(Icons.Default.BookmarkBorder, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(42.dp)); Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(top = 14.dp)); Text(subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 6.dp)) } }
