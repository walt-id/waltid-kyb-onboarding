package id.walt.services.business

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import id.walt.database.Database
import id.walt.models.business.Business
import id.walt.models.business.BusinessDataSource
import id.walt.models.business.CompanyStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class BusinessService : BusinessDataSource {
    suspend fun isBusinessExisting(accountId: String): Boolean =
        Database.business.countDocuments(eq("_id", accountId)) >= 1


    override suspend fun createBusiness(business: Business): Business {
        if (isBusinessExisting(business.registration_number)) {
            error("Business with ${business.registration_number} already exists")
        }
        Database.business.insertOne(business)
        return business
    }

    suspend fun getPendingBusiness(): List<Business> {
      return  Database.business.find(eq("status", CompanyStatus.PENDING)).toList()
    }

    suspend fun updateCompanyStatus(businessId: ObjectId, newStatus: CompanyStatus): Boolean {
        val result = Database.business.updateOne(
            eq("_id", businessId),
            Updates.set("status", newStatus.name)
        )
        return result.modifiedCount > 0
    }

    override suspend fun getBusinessById(id: String): Business? {
        return Database.business.find(eq("_id", id)).first()
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