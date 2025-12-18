package com.thirutricks.expenditure.api

import com.thirutricks.expenditure.model.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ExpenseApiService {

    // Auth
    @POST("includes/api/login.php")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    // Dashboard
    @GET("includes/api/dashboard.php")
    fun getDashboard(): Call<DashboardResponse>

    // Expenses
    @FormUrlEncoded
    @POST("includes/api/add-expense.php")
    fun addExpense(
        @Field("dateexpense") date: String,
        @Field("category") categoryId: Int,
        @Field("costitem") cost: Double,
        @Field("category-description") description: String // Parameter name mismatch in doc vs here? Doc says 'category-description'
    ): Call<ApiResponse>

    @POST("includes/api/update-expense.php")
    fun updateExpense(@Body expense: Expense): Call<ApiResponse>

    @FormUrlEncoded
    @POST("includes/api/delete-expense.php")
    fun deleteExpense(@Field("id") id: Int): Call<ApiResponse>

    // Income
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
    @FormUrlEncoded
    @POST("includes/api/add-category.php")
    fun addCategory(
        @Field("category-name") name: String,
        @Field("mode") mode: String
    ): Call<ApiResponse>

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
    @GET("includes/api/transactions.php")
    fun getTransactions(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("type") type: String // "all", "expense", "income"
    ): Call<TransactionResponse>

    // Lending List
    @GET("includes/api/lending-list.php")
    fun getLendingList(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("status") status: String // "all", "pending", "received"
    ): Call<LendingResponse>
}
