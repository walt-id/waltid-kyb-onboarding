package id.walt.services.business

import com.mongodb.client.model.Filters.eq
import id.walt.database.Database
import id.walt.models.business.Business
import id.walt.models.business.BusinessDataSource

class BusinessService : BusinessDataSource {
    suspend fun isBusinessExisting(accountId: String): Boolean =
        Database.accounts.countDocuments(eq("_id", accountId)) >= 1
    override suspend fun createBusiness(business: Business): Business {
//        Database.accounts.insertOne(business)
//        return business
        TODO("Not yet implemented")
    }

    override suspend fun getBusinessById(id: String): Business? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBusinessById(id: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getBusinesses(): List<Business> {
        TODO("Not yet implemented")
    }

    override suspend fun updateBusiness(business: Business): Business {
        TODO("Not yet implemented")
    }

}