package response

import (
	"github.com/google/uuid"
	"time"
)

type OrderItemRestResponse struct {
	ID          uuid.UUID `json:"id"`
	ProductCode string    `json:"productCode"`
	ProductName string    `json:"productName"`
	Quantity    int       `json:"quantity"`
	UnitPrice   float64   `json:"unitPrice"`
	TotalPrice  float64   `json:"totalPrice"`
}

type OrderGetRestResponse struct {
	ID          uuid.UUID               `json:"id"`
	CustomerID  uuid.UUID               `json:"customerID"`
	OrderDate   time.Time               `json:"orderDate"`
	Status      int                     `json:"status"`
	TotalAmount float64                 `json:"totalAmount"`
	CreatedAt   time.Time               `json:"createdAt"`
	UpdatedAt   time.Time               `json:"updatedAt"`
	Items       []OrderItemRestResponse `json:"items"`
}
