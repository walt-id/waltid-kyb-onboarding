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


    override suspend fun createBusiness(business: Business, adminId: String): Business {
        if (isBusinessExisting(business.registration_number)) {
            error("Business with ${business.registration_number} already exists")
        }
        val newBusiness = business.copy(adminId = adminId)
        Database.business.insertOne(newBusiness)
        return business
    }

    suspend fun getPendingBusiness(AccountId: String): List<Business> {
        println("AccountId: $AccountId")
        println("ObjectId(AccountId): ${ObjectId(AccountId)}")
        return Database.business.find(
            Filters.and(
                eq("status", CompanyStatus.PENDING),
                eq("adminId", AccountId)
            )

        ).toList()
    }

    suspend fun updateCompanyStatus(AccountId: String, businessId: String, newStatus: CompanyStatus): Boolean {
        val obj = ObjectId(businessId)
        val result = Database.business.updateOne(
            Filters.and(
                eq("_id", obj),
                eq("adminId", AccountId)
            ),
            Updates.set("status", newStatus)
        )
        return result.wasAcknowledged()

    }

    override suspend fun getBusinessById(id: String): Business? {
        println("id: $id")
        return Database.business.find(eq("_id", ObjectId(id))).first()
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