Order Processing System Overview

A backend system for an e-commerce platform that allows customers to place orders, track their status, and perform order operations like canceling or retrieving order details.

Tech Stack:
    Java 21
    Spring Boot 3.3.4
    Spring Scheduler (for background job)
    PostgreSQL 17
    JUnit & Mockito (for testing)
    H2 In-Memory Database( for testing)
    Maven (for build & dependency management)

Features Implemented:
    • Create Order: Add a new order with multiple items.      
    • Retrieve Order by ID: Fetch order details using the order ID.      
    • Update Order Status: Background job updates PENDING orders to PROCESSING every 5 minutes.      
    • List All Orders: Optionally filter by status (PENDING, PROCESSING, SHIPPED, DELIVERED).      
    • Cancel Order: Cancel if the order is still in PENDING status.

Steps for running the Project:
    git clone https://github.com/uthralakshmi-k/ecom-order-service.git
    cd order-processing-system
    mvn spring-boot:run

API Endpoints:
    Method	Endpoint	Description
    GET	/orders	List all orders 
    GET	/orders/{id}	Get order details
    GET	/orders?status=	List orders filtered by status
    PUT	/orders/{id}/cancel	Cancel order if in PENDING
    POST	/orders/create	Create new order with multiple items
    (Background)	Every 5 min	Updates PENDING to PROCESSING
  
Notes:
    The background job uses Spring's @Scheduled annotation.
    The in-memory H2 DB resets on each run.
    Tests cover both unit and integration scenarios.

