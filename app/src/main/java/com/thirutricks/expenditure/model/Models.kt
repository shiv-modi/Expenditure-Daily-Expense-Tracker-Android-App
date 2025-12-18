package com.thirutricks.expenditure.model

import com.google.gson.annotations.SerializedName
import java.util.Locale

data class ApiResponse(
    val status: String,
    val message: String
)

data class LoginRequest(
    val email: String,
    val password: String,
    val submit: String = "Login"
)

data class LoginResponse(
    val status: String,
    val message: String,
    @SerializedName("access_token") val accessToken: String? // Nullable if login fails
)

data class Category(
    @SerializedName("categoryid") val categoryId: Int,
    @SerializedName("categoryname") val categoryName: String,
    val mode: String
)

data class CategoryResponse(
    val status: String,
    val categories: List<Category>?
)

data class Expense(
    @SerializedName("expenseid") val id: Int,
    @SerializedName("dateexpense") val date: String,
    @SerializedName("category") val categoryId: Int,
    @SerializedName("cost") val amount: Double,
    val description: String
)

data class Income(
    @SerializedName("incomeid") val id: Int,
    @SerializedName("incomeDate") val date: String,
    @SerializedName("category") val categoryId: Int,
    @SerializedName("incomeAmount") val amount: Double,
    val description: String
)

data class Transaction(
    @SerializedName("categoryname") val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("date") val date: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("type") val type: String
)

data class TransactionData(
    val transactions: List<Transaction>
)

data class TransactionResponse(
    val status: String,
    val data: TransactionData
)

data class Lending(
    @SerializedName("name") val name: String,
    @SerializedName("amount") val amount: Double,
    @SerializedName("description") val description: String,
    @SerializedName("date") val dateOfLending: String,
    @SerializedName("status") val status: String
) {
    /**
     * Returns the capitalized status for display purposes.
     * Lazily initialized and cached to avoid repeated string operations.
     */
    val displayStatus: String by lazy {
        status.replaceFirstChar { 
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() 
        }
    }
}

data class LendingData(
    val lending: List<Lending>
)

data class LendingResponse(
    val status: String,
    val data: LendingData
)

data class DashboardUser(
    val name: String,
    val email: String
)

data class DashboardChart(
    val labels: List<String>?,
    val data: List<Any>? // Data seems empty in example, using Any for safety or List<Double> if confirmed.
)

data class DashboardCategory(
    val category: String,
    @SerializedName("total_expense") val totalExpense: Double
)

data class DashboardData(
    val user: DashboardUser,
    @SerializedName("today_expense") val todayExpense: Double,
    @SerializedName("yesterday_expense") val yesterdayExpense: Double,
    @SerializedName("monthly_expense") val monthlyExpense: Double,
    @SerializedName("total_expense") val totalExpense: Double,
    @SerializedName("total_income") val totalIncome: Double,
    val balance: Double,
    val chart: DashboardChart?,
    val categories: List<DashboardCategory>
)

data class DashboardResponse(
    val status: String,
    val data: DashboardData
)
