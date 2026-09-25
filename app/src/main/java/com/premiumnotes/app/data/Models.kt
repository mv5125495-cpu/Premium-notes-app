package com.premiumnotes.app.data

data class Note(
    val id: Int,
    val title: String,
    val preview: String,
    val body: String,
    val category: String,
    val updated: String,
    val color: Long,
    val isBookmarked: Boolean = false
)

data class Category(val name: String, val count: Int, val icon: String, val color: Long)

data class AdminMetric(val label: String, val value: String, val change: String)

val sampleCategories = listOf(
    Category("Work", 12, "briefcase", 0xFFDBEAFE),
    Category("Personal", 8, "person", 0xFFFFE4E6),
    Category("Ideas", 15, "lightbulb", 0xFFFEF3C7),
    Category("Learning", 6, "school", 0xFFDFF7EE)
)

val sampleNotes = listOf(
    Note(1, "Q4 product strategy", "A focused plan for our next season of growth and customer value.", "The next quarter is about creating less, but making every release count. We will focus on clarity, thoughtful defaults, and listening closely to our customers.\n\nThree priorities:\n1. Make the daily workflow feel effortless.\n2. Build trust through transparent communication.\n3. Leave room for small experiments.", "Work", "Today, 9:42 AM", 0xFFDBEAFE, true),
    Note(2, "The art of slow mornings", "Small rituals that make space for a calmer, more intentional day.", "A slow morning is not about doing less. It is about choosing what deserves your attention before the world chooses for you.\n\nStart with water, a page of writing, and one clear intention. The rest can follow.", "Personal", "Yesterday", 0xFFFFE4E6),
    Note(3, "Ideas for the next big thing", "A collection of sparks, questions, and half-formed possibilities.", "Capture ideas before judging them. The best concepts often arrive as questions: What if this were simpler? What would make this joyful? What would we keep if we had to remove half?", "Ideas", "Monday", 0xFFFEF3C7, true),
    Note(4, "Reading notes: creative focus", "Thoughts from a quiet afternoon with a very good book.", "Focus is a practice of returning. Each time attention wanders, the work is simply to come back with curiosity instead of judgment.", "Learning", "Oct 12", 0xFFDFF7EE),
    Note(5, "Weekend packing list", "A tiny checklist for a weekend away by the coast.", "Camera, comfortable layers, headphones, notebook, charger, and a little room for the unexpected.", "Personal", "Oct 08", 0xFFFFE4E6)
)

val adminMetrics = listOf(
    AdminMetric("Total users", "2,840", "+12.5%"),
    AdminMetric("Active notes", "18,492", "+8.2%"),
    AdminMetric("Categories", "24", "+2 new"),
    AdminMetric("Announcements", "06", "2 drafts")
)
