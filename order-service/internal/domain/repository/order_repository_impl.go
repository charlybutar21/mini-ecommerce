package repository

import (
	"context"
	"github.com/google/uuid"
	"gorm.io/gorm"
	"order-service/internal/domain/model"
)

type OrderRepositoryImpl struct {
	db *gorm.DB
}

func NewOrderRepository(db *gorm.DB) OrderRepository {
	return &OrderRepositoryImpl{db: db}
}

func (repository *OrderRepositoryImpl) Save(ctx context.Context, tx *gorm.DB, order *model.Order) (*model.Order, error) {
	if err := tx.WithContext(ctx).Create(&order).Error; err != nil {
		return nil, err
	}
	return order, nil
}

func (repository *OrderRepositoryImpl) Update(ctx context.Context, tx *gorm.DB, order *model.Order) error {
	if err := tx.WithContext(ctx).Save(&order).Error; err != nil {
		return err
	}
	return nil
}

func (repository *OrderRepositoryImpl) FindById(ctx context.Context, tx *gorm.DB, orderId uuid.UUID) (*model.Order, error) {
	var order model.Order
	if err := tx.WithContext(ctx).First(&order, "id = ?", orderId).Error; err != nil {
		return nil, err
	}
	return &order, nil
}
