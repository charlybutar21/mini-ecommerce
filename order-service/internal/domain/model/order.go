package model

import (
	"time"

	"github.com/google/uuid"
)

const (
	StatusPending   = 1
	StatusProcessed = 2
	StatusShipped   = 3
	StatusCompleted = 4
	StatusCanceled  = 5
)

type Order struct {
	ID         uuid.UUID `json:"id"`
	CustomerID uuid.UUID `json:"customer_id"`
	OrderDate  time.Time `json:"order_date"`
	Status     int       `json:"status"`
	CreatedAt  time.Time `json:"created_at"`
	UpdatedAt  time.Time `json:"updated_at"`
}
