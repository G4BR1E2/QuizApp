## **Quiz app - a modern Android app built with Kotlin and Jetpack Compose**

This project is a mobile Android app that allows you to play quizzes with the topics and questions you create yourself. It also allows you to view your past attempts.
The app also has input validation when entering or editing topics and questions. 
After validating your input, the app saves the created topics and questions along with your attempts on your device using Room database.
It was built in Android Studio using Kotlin and Jetpack Compose, the modern and reccommended way by Android.
The build system that the project is using is gradle.

How to try it:
1. Download android studio, open the project, wait for it to build then run it using the emulator
2. Download the APK on an android device and run the app  

How to use the app:
1. Browse topics: Tap the Topics tab in the bottom navigation bar.
<img width="180" height="390" alt="Screenshot_20261115_142216_QuizApp" src="https://github.com/user-attachments/assets/a37c9f07-d725-4ac0-83a6-1d27dcc314b5" />

2. Create a topic: Tap "Add topic" to add a new subject (e.g., Science, History, Programming).
<img width="180" height="390" alt="Screenshot_20261115_142231_QuizApp" src="https://github.com/user-attachments/assets/a8c37050-cadf-4415-8a05-a42e181f7925" />
   
3. Add questions: Tap any topic to open its questions screen, then tap "Add question" to add fill in or multiple choice questions.
<img width="180" height="390" alt="Screenshot_20261115_142529_QuizApp" src="https://github.com/user-attachments/assets/b6582d60-38cd-4718-9c40-9888d858d807" />
   
4. Play quizzes: Tap the Quiz tab in the bottom navigation bar and click play on any of the topics you created.
<img width="180" height="390" alt="Screenshot_20261115_142750_QuizApp" src="https://github.com/user-attachments/assets/59cd8702-283f-436d-b755-5d6f67f914cb" />
<img width="180" height="390" alt="Screenshot_20261115_142841_QuizApp" src="https://github.com/user-attachments/assets/fac104e9-df40-4480-b687-b6e39d5b7149" />

5. View your attempts by tapping the Attempts tab in the top navigation bar after finishing a quiz.
<img width="180" height="390" alt="Screenshot_20261115_225901_QuizApp" src="https://github.com/user-attachments/assets/672c68cd-b358-4f39-b0f9-573012c678f2" />

- **Language**: [Kotlin](https://kotlinlang.org/) (v2.4+)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Build System**: Gradle 9.4 with Kotlin DSL & Version Catalogs (`libs.versions.toml`)
