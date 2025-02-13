package main

import (
	"github.com/joho/godotenv"
	"log"
	"net/http"
	"order-service/internal/delivery/rest/router"
	"order-service/internal/domain/repository"
	"order-service/internal/infrastructure/database"
	"order-service/internal/usecase"
)

func main() {
	log.Println("Starting Order Service")

	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	}

	db, err := database.InitDB()
	if err != nil {
		log.Fatalf("Error initializing database: %v", err)
	}

	// Initialize all repositories
	orderRepo := repository.NewOrderRepository(db)
	orderItemRepo := repository.NewOrderItemRepository(db)

	// Initialize all use cases
	orderCreateUseCase := usecase.NewOrderCreateUseCase(orderRepo, orderItemRepo, db)
	orderGetUseCase := usecase.NewOrderGetUseCase(orderRepo, orderItemRepo, db)
	orderUpdateUseCase := usecase.NewOrderUpdateUseCase(orderRepo, orderItemRepo, db)

	// Initialize all routers
	orderRouter := router.NewOrderRouter(orderCreateUseCase, orderGetUseCase, orderUpdateUseCase)

	// Start the HTTP server
	log.Println("Server is running on :8081")
	httpRouter := router.RouteOrder(orderRouter)
	if err := http.ListenAndServe(":8081", httpRouter); err != nil {
		log.Fatalf("Error starting server: %s", err)
	}
}
