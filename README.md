# Expenditure - Daily Expense Tracker Android App

A modern Android expense tracking application built with Kotlin that helps users manage their daily expenses, income, and lending activities.

## Features

- **User Authentication**: Secure login with JWT token-based authentication
- **Dashboard**: View comprehensive financial overview including:
  - Current balance
  - Total income and expenses
  - Today's, yesterday's, and monthly expenses
  - Category-wise expense breakdown
- **Expense Management**: Add and track daily expenses with categories
- **Income Management**: Record and monitor income sources
- **Lending/Borrowing**: Keep track of money lent to others
- **Transaction History**: View all financial transactions in one place

## Tech Stack

- **Language**: Kotlin
- **Architecture**: MVVM pattern with ViewBinding
- **Networking**: Retrofit2 + OkHttp3
- **JSON Parsing**: Gson
- **Async Operations**: Kotlin Coroutines
- **UI**: Material Design Components
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)

## Dependencies

- AndroidX Core KTX
- AndroidX AppCompat
- Material Components
- ConstraintLayout
- Retrofit 2.9.0
- OkHttp Logging Interceptor 4.11.0
- Kotlin Coroutines 1.7.3
- Lifecycle ViewModel KTX 2.6.2

## Project Structure

```
app/src/main/java/com/thirutricks/expenditure/
├── api/
│   ├── ExpenseApiService.kt    # API interface definitions
│   └── RetrofitClient.kt        # Retrofit configuration
├── model/
│   └── Models.kt                # Data models
├── utils/
│   └── SessionManager.kt        # Session and token management
├── MainActivity.kt              # Main container with bottom navigation
├── LoginActivity.kt             # User authentication
├── DashboardFragment.kt         # Financial overview
├── TransactionsFragment.kt      # Transaction history
├── LendingFragment.kt           # Lending management
├── AddExpenseActivity.kt        # Add new expense
├── AddIncomeActivity.kt         # Add new income
└── [Adapters]                   # RecyclerView adapters
```

## Setup Instructions

1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Update the `BASE_URL` in `RetrofitClient.kt` with your backend API URL
5. Build and run the application

## API Integration

The app connects to a backend API for data management. Ensure your backend implements the following endpoints:
- `/includes/api/login.php` - User authentication
- `/includes/api/dashboard.php` - Dashboard data
- `/includes/api/add-expense.php` - Add expense
- `/includes/api/add-income.php` - Add income
- `/includes/api/transactions.php` - List transactions
- `/includes/api/lending-list.php` - List lending records
- And more...

## Security Features

- JWT token authentication with expiry validation
- Secure token storage using SharedPreferences
- ProGuard rules for release builds
- Input validation for all user inputs

## Building for Release

The project includes ProGuard configuration for code obfuscation and optimization. To build a release version:

```bash
./gradlew assembleRelease
```

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
