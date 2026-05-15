package com.example.nammaskills

object SampleData {
    val courses = listOf(
        Course(
            id = "1",
            title = "Mobile Repairing",
            category = "Vocational",
            duration = "3 Months",
            isShortTerm = true,
            centerName = "Govt. Skill Center, Tumakuru",
            location = "Tumakuru",
            eligibility = "10th Pass",
            jobGuarantee = true,
            description = "Learn to repair all types of smartphones and tablets. Hands-on training provided.",
            imageUrl = "https://images.unsplash.com/photo-1581092921461-eab62e841f89?w=400&q=80"
        ),
        Course(
            id = "2",
            title = "Welding Specialist",
            category = "Technical",
            duration = "6 Months",
            isShortTerm = false,
            centerName = "IT Industrial Hub",
            location = "Dharwad",
            eligibility = "8th Pass",
            jobGuarantee = true,
            description = "Advanced arc and gas welding techniques for industrial applications.",
            imageUrl = "https://images.unsplash.com/photo-1504328345606-18bbc8c9d7d1?w=400&q=80"
        ),
        Course(
            id = "3",
            title = "Fashion Designing",
            category = "Creative",
            duration = "3 Months",
            isShortTerm = true,
            centerName = "Women Empowerment Center",
            location = "Mysuru",
            eligibility = "None",
            jobGuarantee = false,
            description = "Master the art of garment construction and modern fashion trends.",
            imageUrl = "https://images.unsplash.com/photo-1558769132-cb1aea458c5e?w=400&q=80"
        ),
        Course(
            id = "4",
            title = "Python Programming",
            category = "Coding",
            duration = "3 Months",
            isShortTerm = true,
            centerName = "Digital India Lab",
            location = "Bengaluru",
            eligibility = "12th Pass",
            jobGuarantee = true,
            description = "Basic to advanced Python programming with project-based learning.",
            imageUrl = "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=400&q=80"
        )
    )

    val successStories = listOf(
        SuccessStory(
            id = "1",
            name = "Ramesh Kumar",
            courseName = "Mobile Repairing",
            company = "Self-Employed (Ramesh Mobiles)",
            testimonial = "This course changed my life. I now earn ₹20,000 per month in my village.",
            imageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200&q=80"
        ),
        SuccessStory(
            id = "2",
            name = "Priya S.",
            courseName = "Fashion Designing",
            company = "Usha Garments",
            testimonial = "I learned sewing and design, and now I work for a top export house.",
            imageUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200&q=80"
        )
    )

    val centers = listOf(
        SkillCenter(
            id = "c1",
            name = "Tumakuru Skill Center",
            address = "Main Road, Opp City Bus Stand",
            contact = "0816-227XXXX",
            latitude = 13.3392,
            longitude = 77.1140
        ),
        SkillCenter(
            id = "c2",
            name = "Mysuru Women Training",
            address = "KD Road, Mysuru",
            contact = "0821-251XXXX",
            latitude = 12.2958,
            longitude = 76.6394
        )
    )
}
