package repository

import (
	"context"
	"github.com/google/uuid"
	"gorm.io/gorm"
	"order-service/internal/domain/model"
)

type OrderRepository interface {
	Save(ctx context.Context, tx *gorm.DB, order *model.Order) (*model.Order, error)
	Update(ctx context.Context, tx *gorm.DB, order *model.Order) error
	FindById(ctx context.Context, tx *gorm.DB, orderId uuid.UUID) (*model.Order, error)
}
