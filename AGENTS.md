# Project Guidelines for AI Agents (AGENTS.md)

Welcome to the **sp-wallet** Kotlin Multiplatform project! These guidelines define the architectural
principles, tech stack conventions, and coding standards for AI assistants working in this
repository.

## Tech Stack & Architecture

- **Project Type:** Kotlin Multiplatform (KMP)
- **UI Framework:** Compose Multiplatform (`org.jetbrains.compose`) with Material 3.
- **Dependency Injection:** Koin (`io.insert-koin`).
- **Database & Storage:** Room (Multiplatform) & AndroidX DataStore.
- **Networking:** Ktor Client & Kotlinx Serialization.
- **Navigation:** Jetpack Navigation Compose (`androidx.navigation.compose`).
- **Concurrency:** Kotlin Coroutines & Flow (`StateFlow`, `SharedFlow`).

## Project Structure

- `sharedCore/`: Shared business logic, database entities, repositories, platform abstractions
  (`expect/actual`), and core data sources.
- `sharedApp/`: Shared presentation layer, ViewModels, navigation graphs (`RootNavHost`), and UI
  components (`commonMain`).
- `androidApp/`: Android-specific application entry point (`MainActivity`, `SpWalletApp`) and
  platform bindings.
- `iosApp/`: iOS-specific SwiftUI wrapper and iOS entry point (`MainViewController`).

## Coding Standards & Best Practices

1. **Architecture (MVI):**
    - Keep UI components stateless where possible, lifting state up to ViewModels.
    - Use `StateFlow` for exposing UI state from ViewModels.
    - Use Koin view model injection (`koinViewModel()`) for managing dependencies in Compose.

2. **Kotlin Multiplatform (KMP):**
    - Place business logic, UI screens, and navigation in `commonMain` whenever possible.
    - Use `expect/actual` only when platform-specific APIs are strictly necessary (e.g., database
      file paths, platform context).

3. **Code Quality & Safety:**
    - Write clean, idiomatic Kotlin following official Kotlin coding conventions.
    - Never use shell commands (`sed`, `perl`, etc.) to edit files — always use the IDE's built-in
      file modification tools (`replace_file_content`, `write_file`).
    - Keep functions concise, handle coroutine exceptions properly, and ensure lifecycle awareness.
   - Never use comments, except where it is absolutely crucial

## Communication Language

- Communicate with the developer in **Russian** (unless specified otherwise), while keeping code
  identifiers, variable names, and technical terms in standard English/Kotlin.
