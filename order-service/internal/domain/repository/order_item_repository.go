package repository

import (
	"context"
	"github.com/google/uuid"
	"gorm.io/gorm"
	"order-service/internal/domain/model"
)

type OrderItemRepository interface {
	Save(ctx context.Context, tx *gorm.DB, item *model.OrderItem) (*model.OrderItem, error)
	Update(ctx context.Context, tx *gorm.DB, item *model.OrderItem) error
	FindById(ctx context.Context, tx *gorm.DB, itemId uuid.UUID) (*model.OrderItem, error)
	FindByOrderID(ctx context.Context, db *gorm.DB, orderId uuid.UUID) ([]model.OrderItem, error)
}
