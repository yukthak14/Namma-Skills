package com.example.nammaskills

data class Course(
    val id: String,
    val title: String,
    val category: String,
    val duration: String,
    val isShortTerm: Boolean,
    val centerName: String,
    val location: String,
    val eligibility: String,
    val jobGuarantee: Boolean,
    val description: String,
    val imageUrl: String
)

data class SuccessStory(
    val id: String,
    val name: String,
    val courseName: String,
    val company: String,
    val testimonial: String,
    val imageUrl: String
)

data class SkillCenter(
    val id: String,
    val name: String,
    val address: String,
    val contact: String,
    val latitude: Double,
    val longitude: Double
)

data class CandidateProfile(
    val name: String,
    val email: String,
    val phone: String,
    val education: String,
    val experience: String,
    val appliedCourse: String
)
