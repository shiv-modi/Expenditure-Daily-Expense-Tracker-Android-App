package com.thirutricks.expenditure.api

import com.thirutricks.expenditure.model.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * ExpenseApiService defines all API endpoints for the Expenditure app.
 * Includes authentication, dashboard, expense/income management, and lending features.
 */
interface ExpenseApiService {

    // Authentication
    /**
     * Authenticates user and returns JWT token.
     * @param request LoginRequest containing email and password
     * @return LoginResponse with access token on success
     */
    @POST("includes/api/login.php")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // Dashboard
    /**
     * Fetches dashboard data including balance, expenses, income, and categories.
     * @return DashboardResponse with user's financial overview
     */
    @GET("includes/api/dashboard.php")
    fun getDashboard(): Call<DashboardResponse>

    // Expenses
    /**
     * Adds a new expense entry.
     * @param date Date of expense (format: yyyy-MM-dd)
     * @param categoryId Category ID for the expense
     * @param cost Amount of the expense
     * @param description Description of the expense
     * @return ApiResponse indicating success or failure
     */
    @FormUrlEncoded
    @POST("includes/api/add-expense.php")
    fun addExpense(
        @Field("dateexpense") date: String,
        @Field("category") categoryId: Int,
        @Field("costitem") cost: Double,
        @Field("category-description") description: String
    ): Call<ApiResponse>

    @POST("includes/api/update-expense.php")
    fun updateExpense(@Body expense: Expense): Call<ApiResponse>

    @FormUrlEncoded
    @POST("includes/api/delete-expense.php")
    fun deleteExpense(@Field("id") id: Int): Call<ApiResponse>

    // Income
    /**
     * Adds a new income entry.
     * @param date Date of income (format: yyyy-MM-dd)
     * @param categoryId Category ID for the income
     * @param amount Amount of the income
     * @param description Description of the income
     * @return ApiResponse indicating success or failure
     */
    @FormUrlEncoded
    @POST("includes/api/add-income.php")
    fun addIncome(
        @Field("incomeDate") date: String,
        @Field("category") categoryId: Int,
        @Field("incomeAmount") amount: Double,
        @Field("description") description: String
    ): Call<ApiResponse>

    @POST("includes/api/update-income.php")
    fun updateIncome(@Body income: Income): Call<ApiResponse>

    @FormUrlEncoded
    @POST("includes/api/delete-income.php")
    fun deleteIncome(@Field("id") id: Int): Call<ApiResponse>

    // Categories
    /**
     * Retrieves list of expense/income categories.
     * @param mode Filter by "Expense" or "Income", null returns all categories
     * @return CategoryResponse with list of categories
     */
    @GET("includes/api/get-categories.php")
    fun getCategories(@Query("mode") mode: String?): Call<CategoryResponse>

    // Lending
    @FormUrlEncoded
    @POST("includes/api/lending.php")
    fun addLending(
        @Field("name") name: String,
        @Field("date") date: String,
        @Field("amount") amount: Double,
        @Field("description") description: String,
        @Field("status") status: String
    ): Call<ApiResponse>

    // Import/Export
    @GET("includes/api/export-csv.php")
    fun exportCsv(): Call<ResponseBody> // Need ResponseBody for file download

    @Multipart
    @POST("includes/api/import-csv.php")
    fun importCsv(@Part file: MultipartBody.Part): Call<ApiResponse>

    // Transactions List
    /**
     * Retrieves paginated list of transactions.
     * @param page Page number for pagination
     * @param limit Number of items per page
     * @param type Filter by transaction type: "all", "expense", or "income"
     * @return TransactionResponse with list of transactions
     */
    @GET("includes/api/transactions.php")
    fun getTransactions(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("type") type: String
    ): Call<TransactionResponse>

    // Lending List
    /**
     * Retrieves paginated list of lending records.
     * @param page Page number for pagination
     * @param limit Number of items per page
     * @param status Filter by status: "all", "pending", or "received"
     * @return LendingResponse with list of lending records
     */
    @GET("includes/api/lending-list.php")
    fun getLendingList(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("status") status: String
    ): Call<LendingResponse>
}
