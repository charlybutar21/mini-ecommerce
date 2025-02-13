package request

import (
	"time"
)

type OrderItemCreateRestRequest struct {
	ProductCode string  `json:"productCode" binding:"required"`
	ProductName string  `json:"productName" binding:"required"`
	Quantity    int     `json:"quantity" binding:"required,gt=0"`
	UnitPrice   float64 `json:"unitPrice" binding:"required,gt=0"`
}

type OrderCreateRestRequest struct {
	CustomerID string                       `json:"customerID" binding:"required,uuid"`
	OrderDate  time.Time                    `json:"orderDate" binding:"required"`
	Status     int                          `json:"status" binding:"required"`
	Items      []OrderItemCreateRestRequest `json:"items" binding:"required,dive,required"`
}

type OrderItemUpdateRestRequest struct {
	ItemID      string  `json:"itemID" binding:"required"`
	ProductCode string  `json:"productCode" binding:"required"`
	ProductName string  `json:"productName" binding:"required"`
	Quantity    int     `json:"quantity" binding:"required,gt=0"`
	UnitPrice   float64 `json:"unitPrice" binding:"required,gt=0"`
}

type OrderUpdateRestRequest struct {
	CustomerID string                       `json:"customerID" binding:"required,uuid"`
	OrderDate  time.Time                    `json:"orderDate" binding:"required"`
	Status     int                          `json:"status" binding:"required"`
	Items      []OrderItemUpdateRestRequest `json:"items" binding:"required,dive,required"`
}
