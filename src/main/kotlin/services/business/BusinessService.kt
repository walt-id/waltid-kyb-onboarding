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

    suspend fun approveAndIssueVC(AccountId: String, businessId: String): Boolean {
        val obj = ObjectId(businessId)
        val result = Database.business.updateOne(
            Filters.and(
                eq("_id", obj),
                eq("adminId", AccountId)
            ),
            Updates.combine(
                Updates.set("status", CompanyStatus.APPROVED),
                Updates.set("approved", true),
                Updates.set("approved_by", AccountId)
            )
        )

        val business = Database.business.find(eq("_id", obj)).first()
        val business_did = business.wallet_did

        // Issue VC

        val issueVC = issue(
            legal_name = business.legal_name,
            business_type = business.business_type,
            registration_address = business.registration_address,
            registration_number = business.registration_number,
            phone_number = business.phone_number,
            website = business.website,
            business_did = business_did
        )

        println(
            "VC issued: $issueVC"
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