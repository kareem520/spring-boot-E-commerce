# 🛒 Project Overview
Spring-Ecomm is a full-featured, multi-tenant e-commerce marketplace API built with Spring Boot 4. The system is architected to support three distinct user interfaces, each with its own business logic and security constraints, all managed through a unified JWT-based authentication system.

## 🌍 Live Demo 

- **Production URL:** https://spring-boot-e-commerce-production-1434.up.railway.app

- **Interactive API Docs (Swagger):** [API documentation](https://spring-boot-e-commerce-production-1434.up.railway.app/swagger-ui.html) to see all endpoints.

## 🛠️ Tech Stack
- **Backend:** Spring Boot, Java
- **Database:** MySQL
- **Security:** JWT, Spring Security
- **Build Tool:** Maven
- **ORM:** Spring Date JPA
- **Documentation:** OpenAPI 3.0 / Swagger UI


## 👥 System Roles & Modules
The platform is logically divided into three primary modules to ensure separation of concerns and data integrity:

### 1. Admin Module (System Oversight)
 The Control Center of the platform, reserved for administrative staff.

- Category CRUD: Admins have exclusive rights to create or delete categories, provided the category is currently empty.


- User Management: Ability to view user profiles, manage roles, and deactivate/reactivate accounts.


### 2. Seller Module (Merchant Interface)
 A dedicated workspace for vendors to manage their business within the marketplace.

- Inventory Management: Sellers can add products to existing categories and have full CRUD control over their own listings.

- Product Soft-Delete: Sellers can remove products without breaking the history of previous orders.

- Multi-Tenancy Logic: Sellers can host products with the same name as other sellers, as long as they belong to different categories or unique owner profiles.

### 3. User Module (Customer Experience)
A streamlined interface focused on discovery and purchasing.

- Discovery: Any user can browse categories, search products by keyword, or filter by category ID.

- Smart Shopping Cart: Users can add products to a persistent cart. The system ensures that if a seller updates a product's price or description, the items already in the user's cart remain unchanged to maintain the original purchase intent.


- Order Fulfillment: Seamless conversion of cart items into orders


## 🔐 Security & Access Control
The project implements Spring Security with a custom JWT implementation:

- Secret Key: Secured via a high-entropy signature.

- Token Expiry: Set to 8,640,000 seconds for extended session management.

- Initial Setup: The system bootstraps with a default admin account (admin@gmail.com) for immediate configuration.

## 🚦 How to Test the API
Once the application is running, you can explore and test the endpoints using the following methods:


- **Production URL:** https://spring-boot-e-commerce-production-1434.up.railway.app

- **Interactive API Docs (Swagger):** [API documentation](https://spring-boot-e-commerce-production-1434.up.railway.app/swagger-ui.html).

## 2. Initial Authentication
To access protected endpoints (Admin/Seller), you must first authenticate:


- Register: Use the /api/v1/auth/register endpoint to create an account.


- Login: Use the /api/v1/auth/login endpoint with your credentials.

- Authorize: Copy the jwtToken from the login response. In Swagger, click the "Authorize" button at the top and paste the token in the format: Bearer <your_token>.

## 3. Using the Default Admin
For immediate testing of Admin features (like Category CRUD), use the pre-configured credentials:

- Email: admin@gmail.com

- Password: Admin@123
