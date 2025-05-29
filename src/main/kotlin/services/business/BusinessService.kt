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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class BusinessService : BusinessDataSource {
    suspend fun isBusinessExisting(registration_number: String): Boolean =
        Database.business.countDocuments(eq("registration_number", registration_number)) >= 1


    override suspend fun createBusiness(business: Business, dataSpaceId: String): Business {
        if (isBusinessExisting(business.registration_number)) {
            error("Business with ${business.registration_number} already exists")
        }
        val newBusiness = business.copy(dataSpaceId = dataSpaceId)
        Database.business.insertOne(newBusiness)
        return business
    }

    suspend fun getPendingBusiness(dataSpaceId: String): List<Business> {
        return Database.business.find(
            Filters.and(
                eq("status", CompanyStatus.PENDING),
                eq("dataSpaceId", dataSpaceId)
            )

        ).toList()
    }

    suspend fun updateCompanyStatus(
        dataSpaceId: String,
        registration_number: String,
        newStatus: CompanyStatus,
    ): Boolean {
        val result = Database.business.updateOne(
            Filters.and(
                eq("registration_number", registration_number),
                eq("dataSpaceId", dataSpaceId)
            ),
            Updates.set("status", newStatus)
        )
        return result.wasAcknowledged()

    }

    suspend fun approveAndIssueVC(
        accountId: String,
        registrationNumber: String,
    ): Boolean {


        val business = Database.business.find(eq("registration_number", registrationNumber)).firstOrNull()
            ?: throw IllegalStateException("Business not found with registration number: $registrationNumber")

        val businessDid = business.wallet_did

        val issued = issue(business, businessDid)

        if (issued == false) {
            throw IllegalStateException("Failed to issue VC for business with registration number: $registrationNumber")
        }
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
        return result.wasAcknowledged()
    }

    override suspend fun getBusinessById(id: String): Business? {
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