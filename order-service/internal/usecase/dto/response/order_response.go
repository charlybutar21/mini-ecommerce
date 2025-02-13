package response

import (
	"github.com/google/uuid"
	"time"
)

type OrderItemResponse struct {
	ID          uuid.UUID `json:"id"`
	ProductCode string    `json:"productCode"`
	ProductName string    `json:"productName"`
	Quantity    int       `json:"quantity"`
	UnitPrice   float64   `json:"unitPrice"`
	TotalPrice  float64   `json:"totalPrice"`
}

type OrderGetResponse struct {
	ID          uuid.UUID           `json:"id"`
	CustomerID  uuid.UUID           `json:"customerID"`
	OrderDate   time.Time           `json:"orderDate"`
	Status      int                 `json:"status"`
	TotalAmount float64             `json:"totalAmount"`
	CreatedAt   time.Time           `json:"createdAt"`
	UpdatedAt   time.Time           `json:"updatedAt"`
	Items       []OrderItemResponse `json:"items"`
}
