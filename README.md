# waltid-kyb-onboarding

## Configuration

You can configure the application using the `file.conf` files located in the `confi` directory. This files contains
various settings for the application, including server port, database connection details, and other environment-specific
configurations.


## Building & Running

To build or run the project, use one of the following tasks:

| Task                          | Description                                                          |
|-------------------------------|----------------------------------------------------------------------|
| `./gradlew test`              | Run the tests                                                        |
| `./gradlew build`             | Build everything                                                     |
| `run`                         | Run the server                                                       |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:7004
```

## Project Endpoints

### Dataspace operator registration

```JSON
curl -X 'POST' \
'http://127.0.0.1:7004/v1/operator/register' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"email": "user@example.com",
"password": "securepassword123",
"company": "Walt ID",
"role": "admin",
"dataSpace": "walt"
}'

```

### Dataspace operator login

```JSON
curl -X 'POST' \
'http://127.0.0.1:7004/v1/operator/login' \
-H 'accept: application/json' \
-H 'Content-Type: application/json' \
-d '{
"email": "user@example.com",
"password": "securepassword123"
}'
```

Use the token received in the response to authenticate subsequent requests.

### Business application registration

```JSON
curl -X 'POST' \
'http://127.0.0.1:7004/v1/business/register' \
-H 'accept: */*' \
-H 'Content-Type: application/json' \
-d '{
"legal_name": "Walt ID",
"wallet_did": "did:key:z6MkjoRhq1jSNJdLiruSXrFFxagqrztZaXHqHGUTKJbcNywp", // Replace with the actual wallet DID 
"business_type": "Technology",
"registration_address": "Walt Street 1",
"registration_number": "1234567890",
"email": "walt@id",
"country_code": "US",
"lei_code": "1234567890",
"phone_number": "+1234567890",
"website": "https://walt.id",
"adminId": "5f7b1b3b7f7b7b7b7b7b7b7b" // Replace with the actual admin ID / dataspace operator ID
}'
```

### Business application approval

When the business application is approved, the operator can approve the application using the following endpoint:

```JSON
{
  "registration_number": "ABC123",
  // Replace with the actual registration number of the company
  "credentialTypes": [
    "GaiaXTermsAndConditions",
    "LegalPerson"
  ]
}
```

the specified verifiable credentials will be issued to the business application. The operator can also specify the
credential types to be issued in the request body.
for this feature (silent issuance) to work , the receiving wallet should enable the silent-exchange feature.




