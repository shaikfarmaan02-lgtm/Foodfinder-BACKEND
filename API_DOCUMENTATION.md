# Food Donation System - Complete API Documentation

## Project Overview
This is a complete Spring Boot backend for a Food Donation System with role-based access control, admin approval workflow, and comprehensive CRUD APIs.

## Database Setup
The project uses the *existing* MySQL database: `food_donation_db`

## Database Tables
- `users` - User accounts with roles and approval status
- `food_listing` - Food listings posted by donors
- `food_requests` - Requests made by NGOs for food
- `distributions` - Distribution records
- `feedback` - User feedback on donations
- `contact_messages` - Contact/inquiry messages

---

## 1. USER MODULE

### Roles
- **ADMIN** - Auto-approved on registration
- **DONOR** - Starts as PENDING_APPROVAL
- **NGO** - Starts as PENDING_APPROVAL

### Approval Status
- `PENDING_APPROVAL` - Awaiting admin approval
- `APPROVED` - Account approved, can access system
- `REJECTED` - Account rejected
- `SUSPENDED` - Account temporarily suspended

### API Endpoints

#### Register User
```
POST /api/users/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "role": "DONOR",
  "phone": "9876543210",
  "address": "123 Main St",
  "organization": "NGO Name",
  "city": "Mumbai",
  "state": "Maharashtra",
  "pincode": "400001"
}

Response (201 Created):
{
  "success": true,
  "message": "User registered successfully. Please wait for admin approval.",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "DONOR",
    "approvalStatus": "PENDING_APPROVAL",
    "phone": "9876543210",
    "address": "123 Main St",
    "organization": "NGO Name",
    "city": "Mumbai",
    "state": "Maharashtra",
    "pincode": "400001"
  }
}
```

#### Login
```
POST /api/users/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}

Response (200 OK):
{
  "success": true,
  "message": "Login successful",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "DONOR",
    "approvalStatus": "APPROVED",
    "canAccess": true
  }
}
```

#### Get All Users
```
GET /api/users

Response (200 OK):
{
  "success": true,
  "message": "Users retrieved successfully",
  "data": [...]
}
```

#### Get User by ID
```
GET /api/users/{id}

Response (200 OK):
{
  "success": true,
  "message": "User retrieved successfully",
  "data": {...}
}
```

#### Update User Profile
```
PUT /api/users/{id}
Content-Type: application/json

{
  "name": "Jane Doe",
  "phone": "9876543211",
  "address": "456 Park Ave"
}

Response (200 OK):
{
  "success": true,
  "message": "User profile updated successfully",
  "data": {...}
}
```

#### Approve User (ADMIN ONLY)
```
PUT /api/users/{id}/approve
Headers: X-Admin-Id: {adminId}

Response (200 OK):
{
  "success": true,
  "message": "User approved successfully",
  "data": {
    "approvalStatus": "APPROVED",
    ...
  }
}
```

#### Reject User (ADMIN ONLY)
```
PUT /api/users/{id}/reject
Headers: X-Admin-Id: {adminId}

Response (200 OK):
{
  "success": true,
  "message": "User rejected successfully",
  "data": {
    "approvalStatus": "REJECTED",
    ...
  }
}
```

#### Suspend User (ADMIN ONLY)
```
PUT /api/users/{id}/suspend
Headers: X-Admin-Id: {adminId}

Response (200 OK):
{
  "success": true,
  "message": "User suspended successfully",
  "data": {
    "approvalStatus": "SUSPENDED",
    ...
  }
}
```

#### Delete User (ADMIN ONLY)
```
DELETE /api/users/{id}
Headers: X-Admin-Id: {adminId}

Response (200 OK):
{
  "success": true,
  "message": "User deleted successfully",
  "data": null
}
```

#### Get Users by Role
```
GET /api/users/role/{role}
Where role = DONOR, NGO, or ADMIN

Response (200 OK): [...]
```

#### Get Users by Approval Status
```
GET /api/users/approval/{status}
Where status = PENDING_APPROVAL, APPROVED, REJECTED, or SUSPENDED

Response (200 OK): [...]
```

#### Get User Count
```
GET /api/users/count

Response (200 OK):
{
  "success": true,
  "message": "User count retrieved successfully",
  "data": 25
}
```

---

## 2. FOOD LISTING MODULE (DONOR)

### Eligibility
- Only APPROVED DONOR users can create/update/delete food listings
- Default status = `AVAILABLE`

### API Endpoints

#### Create Food Listing
```
POST /api/food
Headers: X-User-Id: {donorId}
Content-Type: application/json

{
  "foodName": "Rice",
  "quantity": "10 kg",
  "location": "123 Main St, Mumbai",
  "expiryTime": "2024-12-31T18:00:00"
}

Response (201 Created):
{
  "success": true,
  "message": "Food listing created successfully",
  "data": {
    "id": 1,
    "foodName": "Rice",
    "quantity": "10 kg",
    "location": "123 Main St, Mumbai",
    "expiryTime": "2024-12-31T18:00:00",
    "donorId": 1,
    "status": "AVAILABLE",
    "createdAt": "2024-01-15T10:30:00"
  }
}
```

#### Get All Food Listings
```
GET /api/food

Response (200 OK):
{
  "success": true,
  "message": "Food listings retrieved successfully",
  "data": [...]
}
```

#### Get Food Listing by ID
```
GET /api/food/{id}

Response (200 OK):
{
  "success": true,
  "message": "Food listing retrieved successfully",
  "data": {...}
}
```

#### Update Food Listing (DONOR OWNER ONLY)
```
PUT /api/food/{id}
Headers: X-User-Id: {donorId}
Content-Type: application/json

{
  "foodName": "Rice",
  "quantity": "15 kg",
  "location": "456 Park Ave, Mumbai"
}

Response (200 OK):
{
  "success": true,
  "message": "Food listing updated successfully",
  "data": {...}
}
```

#### Delete Food Listing (DONOR OWNER ONLY)
```
DELETE /api/food/{id}
Headers: X-User-Id: {donorId}

Response (200 OK):
{
  "success": true,
  "message": "Food listing deleted successfully",
  "data": null
}
```

#### Get Available Food Listings
```
GET /api/food/available

Response (200 OK):
{
  "success": true,
  "message": "Available food listings retrieved successfully",
  "data": [...]
}
```

#### Get Donor's Food Listings
```
GET /api/food/donor/{donorId}

Response (200 OK):
{
  "success": true,
  "message": "Donor's food listings retrieved successfully",
  "data": [...]
}
```

#### Get Food Listings by Status
```
GET /api/food/status/{status}
Where status = AVAILABLE or CLAIMED

Response (200 OK):
{
  "success": true,
  "message": "Food listings retrieved successfully",
  "data": [...]
}
```

---

## 3. FOOD REQUEST MODULE (NGO)

### Eligibility
- Only APPROVED NGO users can request food
- When request is ACCEPTED, the food status automatically changes to CLAIMED
- NGO can collect food after acceptance

### API Endpoints

#### Create Food Request
```
POST /api/request
Headers: X-User-Id: {ngoId}
Content-Type: application/json

{
  "donationId": 1
}

Response (201 Created):
{
  "success": true,
  "message": "Food request created successfully",
  "data": {
    "id": 1,
    "donationId": 1,
    "ngoId": 2,
    "status": "PENDING",
    "requestTime": "2024-01-15T11:00:00",
    "acceptedTime": null,
    "collectedTime": null,
    "rejectionReason": null
  }
}
```

#### Get All Food Requests
```
GET /api/request

Response (200 OK):
{
  "success": true,
  "message": "Food requests retrieved successfully",
  "data": [...]
}
```

#### Get Request by ID
```
GET /api/request/{id}

Response (200 OK):
{
  "success": true,
  "message": "Food request retrieved successfully",
  "data": {...}
}
```

#### Accept Food Request (DONOR ONLY)
```
PUT /api/request/{id}/accept
Headers: X-User-Id: {donorId}

Response (200 OK):
{
  "success": true,
  "message": "Food request accepted successfully",
  "data": {
    "status": "ACCEPTED",
    "acceptedTime": "2024-01-15T11:30:00",
    ...
  }
}
```

#### Reject Food Request (DONOR ONLY)
```
PUT /api/request/{id}/reject?rejectionReason=Not%20available
Headers: X-User-Id: {donorId}

Response (200 OK):
{
  "success": true,
  "message": "Food request rejected successfully",
  "data": {
    "status": "REJECTED",
    "rejectionReason": "Not available",
    ...
  }
}
```

#### Mark Food as Collected (NGO ONLY)
```
PUT /api/request/{id}/collect
Headers: X-User-Id: {ngoId}

Response (200 OK):
{
  "success": true,
  "message": "Food marked as collected successfully",
  "data": {
    "status": "COLLECTED",
    "collectedTime": "2024-01-15T12:00:00",
    ...
  }
}
```

#### Get NGO's Food Requests
```
GET /api/request/ngo/{ngoId}

Response (200 OK):
{
  "success": true,
  "message": "NGO's food requests retrieved successfully",
  "data": [...]
}
```

#### Get Pending Requests for Donation
```
GET /api/request/donation/{donationId}/pending

Response (200 OK):
{
  "success": true,
  "message": "Pending food requests retrieved successfully",
  "data": [...]
}
```

#### Get All Requests for Donation
```
GET /api/request/donation/{donationId}

Response (200 OK):
{
  "success": true,
  "message": "Food requests for donation retrieved successfully",
  "data": [...]
}
```

#### Delete Food Request
```
DELETE /api/request/{id}

Response (200 OK):
{
  "success": true,
  "message": "Food request deleted successfully",
  "data": null
}
```

---

## 4. DISTRIBUTION MODULE

### API Endpoints

#### Create Distribution
```
POST /api/distribution
Content-Type: application/json

{
  "donationId": 1,
  "foodRequestId": 1,
  "donorId": 1,
  "ngoId": 2,
  "distributionLocation": "NGO Center, Mumbai",
  "notes": "Morning delivery"
}

Response (201 Created):
{
  "success": true,
  "message": "Distribution created successfully",
  "data": {
    "id": 1,
    "donationId": 1,
    "foodRequestId": 1,
    "donorId": 1,
    "ngoId": 2,
    "status": "PENDING",
    "distributionDate": "2024-01-15T10:30:00",
    "deliveredDate": null,
    "distributionLocation": "NGO Center, Mumbai",
    "notes": "Morning delivery"
  }
}
```

#### Get All Distributions
```
GET /api/distribution

Response (200 OK):
{
  "success": true,
  "message": "Distributions retrieved successfully",
  "data": [...]
}
```

#### Get Distribution by ID
```
GET /api/distribution/{id}

Response (200 OK):
{
  "success": true,
  "message": "Distribution retrieved successfully",
  "data": {...}
}
```

#### Start Distribution (IN_TRANSIT)
```
PUT /api/distribution/{id}/start

Response (200 OK):
{
  "success": true,
  "message": "Distribution started successfully",
  "data": {
    "status": "IN_TRANSIT",
    ...
  }
}
```

#### Mark as Delivered
```
PUT /api/distribution/{id}/deliver

Response (200 OK):
{
  "success": true,
  "message": "Distribution marked as delivered successfully",
  "data": {
    "status": "DELIVERED",
    "deliveredDate": "2024-01-15T14:00:00",
    ...
  }
}
```

#### Cancel Distribution
```
PUT /api/distribution/{id}/cancel

Response (200 OK):
{
  "success": true,
  "message": "Distribution cancelled successfully",
  "data": {
    "status": "CANCELLED",
    ...
  }
}
```

#### Get Distributions from Donor
```
GET /api/distribution/donor/{donorId}

Response (200 OK):
{
  "success": true,
  "message": "Distributions from donor retrieved successfully",
  "data": [...]
}
```

#### Get Distributions to NGO
```
GET /api/distribution/ngo/{ngoId}

Response (200 OK):
{
  "success": true,
  "message": "Distributions to NGO retrieved successfully",
  "data": [...]
}
```

#### Get Distributions by Status
```
GET /api/distribution/status/{status}
Where status = PENDING, IN_TRANSIT, DELIVERED, or CANCELLED

Response (200 OK):
{
  "success": true,
  "message": "Distributions retrieved successfully",
  "data": [...]
}
```

#### Get Distributions by Donation
```
GET /api/distribution/donation/{donationId}

Response (200 OK):
{
  "success": true,
  "message": "Distributions for donation retrieved successfully",
  "data": [...]
}
```

#### Delete Distribution
```
DELETE /api/distribution/{id}

Response (200 OK):
{
  "success": true,
  "message": "Distribution deleted successfully",
  "data": null
}
```

---

## 5. FEEDBACK MODULE

### API Endpoints

#### Create Feedback
```
POST /api/feedback
Content-Type: application/json

{
  "userId": 1,
  "donationId": 1,
  "rating": 5,
  "comment": "Excellent food quality and delivery!"
}

Response (201 Created):
{
  "success": true,
  "message": "Feedback created successfully",
  "data": {
    "id": 1,
    "userId": 1,
    "donationId": 1,
    "rating": 5,
    "comment": "Excellent food quality and delivery!",
    "createdAt": "2024-01-15T15:00:00"
  }
}
```

#### Get All Feedback
```
GET /api/feedback

Response (200 OK):
{
  "success": true,
  "message": "Feedback retrieved successfully",
  "data": [...]
}
```

#### Get Feedback by ID
```
GET /api/feedback/{id}

Response (200 OK):
{
  "success": true,
  "message": "Feedback retrieved successfully",
  "data": {...}
}
```

#### Update Feedback
```
PUT /api/feedback/{id}
Content-Type: application/json

{
  "rating": 4,
  "comment": "Good quality"
}

Response (200 OK):
{
  "success": true,
  "message": "Feedback updated successfully",
  "data": {...}
}
```

#### Delete Feedback
```
DELETE /api/feedback/{id}

Response (200 OK):
{
  "success": true,
  "message": "Feedback deleted successfully",
  "data": null
}
```

#### Get Feedback for Donation
```
GET /api/feedback/donation/{donationId}

Response (200 OK):
{
  "success": true,
  "message": "Feedback for donation retrieved successfully",
  "data": [...]
}
```

#### Get Feedback from User
```
GET /api/feedback/user/{userId}

Response (200 OK):
{
  "success": true,
  "message": "Feedback from user retrieved successfully",
  "data": [...]
}
```

#### Get Average Rating for Donation
```
GET /api/feedback/donation/{donationId}/average-rating

Response (200 OK):
{
  "success": true,
  "message": "Average rating retrieved successfully",
  "data": 4.5
}
```

#### Get Feedback Count
```
GET /api/feedback/count

Response (200 OK):
{
  "success": true,
  "message": "Feedback count retrieved successfully",
  "data": 42
}
```

---

## 6. CONTACT MODULE

### API Endpoints

#### Create Contact Message
```
POST /api/contact
Content-Type: application/json

{
  "name": "Jane Smith",
  "email": "jane@example.com",
  "subject": "Issue with delivery",
  "message": "The food was not delivered on time",
  "phone": "9876543210"
}

Response (201 Created):
{
  "success": true,
  "message": "Contact message sent successfully. We will get back to you soon.",
  "data": {
    "id": 1,
    "name": "Jane Smith",
    "email": "jane@example.com",
    "subject": "Issue with delivery",
    "message": "The food was not delivered on time",
    "phone": "9876543210",
    "createdAt": "2024-01-15T16:00:00",
    "resolved": false,
    "adminNotes": null
  }
}
```

#### Get All Contact Messages (ADMIN ONLY)
```
GET /api/contact
Headers: X-Admin-Id: {adminId}

Response (200 OK):
{
  "success": true,
  "message": "Contact messages retrieved successfully",
  "data": [...]
}
```

#### Get Contact by ID
```
GET /api/contact/{id}

Response (200 OK):
{
  "success": true,
  "message": "Contact message retrieved successfully",
  "data": {...}
}
```

#### Get Unresolved Messages (ADMIN ONLY)
```
GET /api/contact/unresolved
Headers: X-Admin-Id: {adminId}

Response (200 OK):
{
  "success": true,
  "message": "Unresolved contact messages retrieved successfully",
  "data": [...]
}
```

#### Mark Contact as Resolved (ADMIN ONLY)
```
PUT /api/contact/{id}/resolve?adminNotes=Issue%20resolved
Headers: X-Admin-Id: {adminId}

Response (200 OK):
{
  "success": true,
  "message": "Contact message marked as resolved successfully",
  "data": {
    "resolved": true,
    "adminNotes": "Issue resolved",
    ...
  }
}
```

#### Delete Contact (ADMIN ONLY)
```
DELETE /api/contact/{id}
Headers: X-Admin-Id: {adminId}

Response (200 OK):
{
  "success": true,
  "message": "Contact message deleted successfully",
  "data": null
}
```

#### Get Contacts by Email
```
GET /api/contact/email/{email}

Response (200 OK):
{
  "success": true,
  "message": "Contact messages retrieved successfully",
  "data": [...]
}
```

#### Search by Subject
```
GET /api/contact/search?subject=delivery

Response (200 OK):
{
  "success": true,
  "message": "Contact messages retrieved successfully",
  "data": [...]
}
```

#### Get Contact Count (ADMIN ONLY)
```
GET /api/contact/count
Headers: X-Admin-Id: {adminId}

Response (200 OK):
{
  "success": true,
  "message": "Contact message count retrieved successfully",
  "data": 15
}
```

#### Get Unresolved Count (ADMIN ONLY)
```
GET /api/contact/count-unresolved
Headers: X-Admin-Id: {adminId}

Response (200 OK):
{
  "success": true,
  "message": "Unresolved contact message count retrieved successfully",
  "data": 5
}
```

---

## Security Rules

### User Validation
- ADMIN: Can access all features
- DONOR: Must be APPROVED to create/update/delete food listings
- NGO: Must be APPROVED to request food

### Login Rules
- PENDING_APPROVAL → "Waiting for admin approval"
- REJECTED → "Account rejected"
- SUSPENDED → "Account suspended"
- APPROVED → Allow login

### Access Control
- Only APPROVED DONOR can create/update/delete food listings
- Only APPROVED NGO can request food
- Only DONOR can accept/reject food requests
- Only NGO can collect food
- Only donor who created listing can update/delete it
- Only ADMIN can approve/reject/suspend users

---

## Response Format

All API responses follow a standard format:

**Success Response:**
```json
{
  "success": true,
  "message": "Operation successful",
  "data": {...}
}
```

**Error Response:**
```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

---

## HTTP Status Codes

- `200 OK` - Successful GET/PUT request
- `201 Created` - Successful POST request
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Authentication failed
- `403 Forbidden` - Access denied (role/approval issues)
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## Project Structure

```
backend/
├── src/main/java/com/foodfinder/backend/
│   ├── config/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   └── WebConfig.java
│   ├── controller/
│   │   ├── UserController.java
│   │   ├── FoodDonationController.java
│   │   ├── FoodRequestController.java
│   │   ├── DistributionController.java
│   │   ├── FeedbackController.java
│   │   └── ContactController.java
│   ├── service/
│   │   ├── UserService.java
│   │   ├── FoodDonationService.java
│   │   ├── FoodRequestService.java
│   │   ├── DistributionService.java
│   │   ├── FeedbackService.java
│   │   └── ContactService.java
│   ├── repository/
│   │   ├── UserRepository.java
│   │   ├── FoodDonationRepository.java
│   │   ├── FoodRequestRepository.java
│   │   ├── DistributionRepository.java
│   │   ├── FeedbackRepository.java
│   │   └── ContactRepository.java
│   ├── model/
│   │   ├── User.java
│   │   ├── FoodDonation.java
│   │   ├── FoodRequest.java
│   │   ├── Distribution.java
│   │   ├── Feedback.java
│   │   ├── Contact.java
│   │   ├── Role.java
│   │   ├── ApprovalStatus.java
│   │   ├── DonationStatus.java
│   │   ├── RequestStatus.java
│   │   └── DistributionStatus.java
│   ├── dto/
│   │   ├── ApiResponse.java
│   │   ├── RegisterRequest.java
│   │   ├── LoginRequest.java
│   │   ├── LoginResponse.java
│   │   ├── FoodListingDTO.java
│   │   ├── FoodRequestDTO.java
│   │   ├── DistributionDTO.java
│   │   ├── FeedbackDTO.java
│   │   └── ContactDTO.java
│   └── BackendApplication.java
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

---

## Testing Notes

### Test User Registration Flow
1. Register as ADMIN role → Auto APPROVED
2. Register as DONOR role → PENDING_APPROVAL
3. Admin approves DONOR
4. DONOR logs in successfully

### Test Food Donation Flow
1. Approved DONOR creates food listing → status = AVAILABLE
2. NGO views available listings
3. Approved NGO requests food → status = PENDING
4. DONOR accepts → Request status = ACCEPTED, Food status = CLAIMED
5. NGO collects → Request status = COLLECTED
6. Distribution record can be created

### Test Approval Workflow
1. New DONOR/NGO registers
2. Admin views pending approvals
3. Admin approves → User can now access system
4. Admin can reject or suspend as needed

---

## Environment Configuration

Edit `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/food_donation_db
spring.datasource.username=root
spring.datasource.password=Farmaan_f1
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# The server will run on http://localhost:8080
```

---

## Enhancements Made

✅ Added ApprovalStatus enum for user approval workflow
✅ Updated User entity with full profile fields
✅ Created comprehensive DTOs for requests/responses
✅ Implemented role-based access control
✅ Added login with approval status validation
✅ Created complete CRUD APIs for all modules
✅ Implemented FoodRequest service (replacing Claim)
✅ Added Distribution module for tracking
✅ Added Feedback module with ratings
✅ Added Contact/Inquiry module
✅ Standardized response format
✅ Added error handling
✅ Added security validation checks
✅ All endpoints follow REST conventions
✅ Complete documentation

---

## Next Steps (Optional Enhancements)

- Add JWT token-based authentication
- Add email verification for registration
- Add SMS notifications
- Add image upload for food donations
- Add analytics dashboard
- Add reporting features
- Add user reviews and ratings
- Add search and filtering capabilities
- Add pagination to list endpoints
- Add audit logs
- Add caching for performance
- Deploy to production server

---

**Project completed successfully!** All endpoints are ready for frontend integration.
