package id.walt.models.business

interface BusinessDataSource {
    suspend fun createBusiness(business: Business): Business
    suspend fun getBusinessById(id: String): Business?
    suspend fun deleteBusinessById(id: String): Boolean
    suspend fun getBusinesses(): List<Business>
    suspend fun updateBusiness(business: Business): Business


}