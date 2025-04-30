package id.walt.services.business

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Updates
import id.walt.database.Database
import id.walt.models.business.Business
import id.walt.models.business.BusinessDataSource
import id.walt.models.business.CompanyStatus
import id.walt.services.credentials.IssuerVcCredentialService.issue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.json.JsonObject
import org.bson.types.ObjectId

class BusinessService : BusinessDataSource {
    suspend fun isBusinessExisting(registration_number: String): Boolean =
        Database.business.countDocuments(eq("registration_number", registration_number)) >= 1


    override suspend fun createBusiness(business: Business, adminId: String): Business {
        if (isBusinessExisting(business.registration_number)) {
            error("Business with ${business.registration_number} already exists")
        }
        val newBusiness = business.copy(adminId = adminId)
        Database.business.insertOne(newBusiness)
        return business
    }

    suspend fun getPendingBusiness(AccountId: String): List<Business> {
        return Database.business.find(
            Filters.and(
                eq("status", CompanyStatus.PENDING),
                eq("adminId", AccountId)
            )

        ).toList()
    }

    suspend fun updateCompanyStatus(AccountId: String, registration_number: String, newStatus: CompanyStatus): Boolean {
        val result = Database.business.updateOne(
            Filters.and(
                eq("registration_number", registration_number),
                eq("adminId", AccountId)
            ),
            Updates.set("status", newStatus)
        )
        return result.wasAcknowledged()

    }

    suspend fun approveAndIssueVC(
        accountId: String,
        registrationNumber: String,
        credentialTypes: List<String>,
        customCredential: JsonObject? = null,
    ): Boolean {
        val result = Database.business.updateOne(
            Filters.and(
                eq("registration_number", registrationNumber),
                eq("adminId", accountId)
            ),
            Updates.combine(
                Updates.set("status", CompanyStatus.APPROVED),
                Updates.set("approved", true),
                Updates.set("approved_by", accountId)
            )
        )

        val business = Database.business.find(eq("registration_number", registrationNumber)).first()
        val businessDid = business.wallet_did

        for (type in credentialTypes) {
            val isCustom = type == "custom"
            issue(business, businessDid, type, if (isCustom) customCredential else null)
        }


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