package request

import (
	"github.com/google/uuid"
	"time"
)

type OrderItemCreateRequest struct {
	ProductCode string
	ProductName string
	Quantity    int
	UnitPrice   float64
}

type OrderCreateRequest struct {
	CustomerID string
	OrderDate  time.Time
	Status     int
	Items      []OrderItemCreateRequest
}

type OrderItemUpdateRequest struct {
	ItemID      string
	ProductCode string
	ProductName string
	Quantity    int
	UnitPrice   float64
}

type OrderUpdateRequest struct {
	OrderID    uuid.UUID
	CustomerID string
	OrderDate  time.Time
	Status     int
	Items      []OrderItemUpdateRequest
}
