# Quick Start Testing Guide

## Prerequisites
- MySQL running with `food_donation_db` database
- Spring Boot application running on http://localhost:8080
- Postman or similar tool for API testing

---

## 1. User Registration & Approval Workflow

### Step 1: Register as ADMIN (Auto-approved)
```
POST http://localhost:8080/api/users/register
{
  "name": "Admin User",
  "email": "admin@example.com",
  "password": "admin123",
  "role": "ADMIN"
}
```
**Note**: ADMIN is auto-approved. You get `id: 1` (in most cases)

### Step 2: Register as DONOR
```
POST http://localhost:8080/api/users/register
{
  "name": "Donor John",
  "email": "donor1@example.com",
  "password": "donor123",
  "role": "DONOR",
  "phone": "9876543210",
  "address": "123 Main St"
}
```
**Status**: PENDING_APPROVAL (you get `id: 2`)

### Step 3: Register as NGO
```
POST http://localhost:8080/api/users/register
{
  "name": "NGO Helper",
  "email": "ngo1@example.com",
  "password": "ngo123",
  "role": "NGO",
  "organization": "Help Foundation"
}
```
**Status**: PENDING_APPROVAL (you get `id: 3`)

### Step 4: Test Login Before Approval
```
POST http://localhost:8080/api/users/login
{
  "email": "donor1@example.com",
  "password": "donor123"
}
```
**Expected**: `"canAccess": false, "message": "Waiting for admin approval"`

### Step 5: Admin Approves DONOR
```
PUT http://localhost:8080/api/users/2/approve
Headers: X-Admin-Id: 1
```
**Response**: `"approvalStatus": "APPROVED"`

### Step 6: DONOR Login After Approval
```
POST http://localhost:8080/api/users/login
{
  "email": "donor1@example.com",
  "password": "donor123"
}
```
**Expected**: `"canAccess": true, "message": "Login successful"`

---

## 2. Food Donation Flow

### Step 1: DONOR Creates Food Listing
```
POST http://localhost:8080/api/food
Headers: X-User-Id: 2
{
  "foodName": "Rice",
  "quantity": "20 kg",
  "location": "123 Main St, Mumbai",
  "expiryTime": "2024-12-31T18:00:00"
}
```
**Status**: AVAILABLE

### Step 2: Get All Available Food
```
GET http://localhost:8080/api/food/available
```

### Step 3: Get Specific Food Listing
```
GET http://localhost:8080/api/food/1
```

### Step 4: DONOR Updates Listing
```
PUT http://localhost:8080/api/food/1
Headers: X-User-Id: 2
{
  "quantity": "25 kg"
}
```

---

## 3. NGO Request & Collection Flow

### Step 1: Admin Approves NGO
```
PUT http://localhost:8080/api/users/3/approve
Headers: X-Admin-Id: 1
```

### Step 2: NGO Creates Food Request
```
POST http://localhost:8080/api/request
Headers: X-User-Id: 3
{
  "donationId": 1
}
```
**Status**: PENDING

### Step 3: Get Pending Requests
```
GET http://localhost:8080/api/request/donation/1/pending
```

### Step 4: DONOR Accepts Request
```
PUT http://localhost:8080/api/request/1/accept
Headers: X-User-Id: 2
```
**Food Status Changed to**: CLAIMED
**Request Status**: ACCEPTED

### Step 5: NGO Collects Food
```
PUT http://localhost:8080/api/request/1/collect
Headers: X-User-Id: 3
```
**Request Status**: COLLECTED

---

## 4. Distribution Workflow

### Step 1: Create Distribution Record
```
POST http://localhost:8080/api/distribution
{
  "donationId": 1,
  "foodRequestId": 1,
  "donorId": 2,
  "ngoId": 3,
  "distributionLocation": "NGO Center, Mumbai"
}
```
**Status**: PENDING

### Step 2: Start Distribution
```
PUT http://localhost:8080/api/distribution/1/start
```
**Status**: IN_TRANSIT

### Step 3: Mark Delivered
```
PUT http://localhost:8080/api/distribution/1/deliver
```
**Status**: DELIVERED

---

## 5. Feedback Module

### Step 1: Submit Feedback
```
POST http://localhost:8080/api/feedback
{
  "userId": 3,
  "donationId": 1,
  "rating": 5,
  "comment": "Great food quality!"
}
```

### Step 2: Get Average Rating
```
GET http://localhost:8080/api/feedback/donation/1/average-rating
```

### Step 3: Update Feedback
```
PUT http://localhost:8080/api/feedback/1
{
  "rating": 4,
  "comment": "Good quality"
}
```

---

## 6. Contact/Inquiry Module

### Step 1: Submit Contact Message
```
POST http://localhost:8080/api/contact
{
  "name": "John Smith",
  "email": "john@example.com",
  "subject": "Delivery Issue",
  "message": "Food was not delivered on time",
  "phone": "9876543210"
}
```

### Step 2: Admin Gets Unresolved Messages
```
GET http://localhost:8080/api/contact/unresolved
Headers: X-Admin-Id: 1
```

### Step 3: Admin Resolves Message
```
PUT http://localhost:8080/api/contact/1/resolve?adminNotes=Issue resolved with donor
Headers: X-Admin-Id: 1
```

---

## 7. Admin Approval Management

### Get All Pending Approvals
```
GET http://localhost:8080/api/users/approval/PENDING_APPROVAL
```

### Approve User
```
PUT http://localhost:8080/api/users/{id}/approve
Headers: X-Admin-Id: 1
```

### Reject User
```
PUT http://localhost:8080/api/users/{id}/reject
Headers: X-Admin-Id: 1
```

### Suspend User
```
PUT http://localhost:8080/api/users/{id}/suspend
Headers: X-Admin-Id: 1
```

---

## Common Error Scenarios

### ❌ DONOR Not Approved
```
POST http://localhost:8080/api/food
{ ... }
```
**Response**: `401 Forbidden - "Only approved donors can create food listings"`

### ❌ NGO Not Approved
```
POST http://localhost:8080/api/request
{ ... }
```
**Response**: `401 Forbidden - "Only approved NGOs can request food"`

### ❌ Invalid Credentials
```
POST http://localhost:8080/api/users/login
{
  "email": "nonexistent@example.com",
  "password": "any"
}
```
**Response**: `401 Unauthorized - "User not found with email"`

### ❌ Resource Not Found
```
GET http://localhost:8080/api/food/999
```
**Response**: `404 Not Found - "Donation not found with id: 999"`

---

## Statistics Endpoints

### Get User Count
```
GET http://localhost:8080/api/users/count
```

### Get Feedback Count
```
GET http://localhost:8080/api/feedback/count
```

### Get Contact Count
```
GET http://localhost:8080/api/contact/count
Headers: X-Admin-Id: 1
```

### Get Unresolved Contact Count
```
GET http://localhost:8080/api/contact/count-unresolved
Headers: X-Admin-Id: 1
```

---

## Filter/Search Endpoints

### Get Users by Role
```
GET http://localhost:8080/api/users/role/DONOR
GET http://localhost:8080/api/users/role/NGO
GET http://localhost:8080/api/users/role/ADMIN
```

### Get Food by Status
```
GET http://localhost:8080/api/food/status/AVAILABLE
GET http://localhost:8080/api/food/status/CLAIMED
```

### Search Contacts
```
GET http://localhost:8080/api/contact/search?subject=delivery
```

### Get Contacts by Email
```
GET http://localhost:8080/api/contact/email/john@example.com
```

---

## Typical Response Format

### Success (200 OK)
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... }
}
```

### Error (4xx/5xx)
```json
{
  "success": false,
  "message": "Error description",
  "data": null
}
```

---

## Notes for Frontend Integration

1. **Store User ID** after login for subsequent requests (use X-User-Id header)
2. **Store Admin ID** for admin operations (use X-Admin-Id header)
3. **Handle canAccess flag** in login response to control UI access
4. **Implement role-based navigation** - show different options for ADMIN/DONOR/NGO
5. **Check approvalStatus** before allowing operations
6. **Validate response.success** flag before processing data
7. **Use message field** to show user feedback
8. **Implement error boundaries** for all API failures
9. **Add loading states** during API calls
10. **Cache user data** locally after successful login

---

## Database Verification

To verify setup, check MySQL:

```sql
USE food_donation_db;
SHOW TABLES;
SELECT * FROM users;
SELECT * FROM food_listing;
```

Ensure all tables exist and are populated after testing.

---

**Ready for testing!** Start with the User Registration & Approval workflow, then proceed through Food Donation flow.
