package repository

import (
	"context"
	"github.com/google/uuid"
	"gorm.io/gorm"
	"order-service/internal/domain/model"
)

type OrderItemRepositoryImpl struct {
	db *gorm.DB
}

func NewOrderItemRepository(db *gorm.DB) OrderItemRepository {
	return &OrderItemRepositoryImpl{db: db}
}

func (repository *OrderItemRepositoryImpl) Save(ctx context.Context, tx *gorm.DB, item *model.OrderItem) (*model.OrderItem, error) {
	if err := tx.WithContext(ctx).Create(&item).Error; err != nil {
		return nil, err
	}
	return item, nil
}

func (repository *OrderItemRepositoryImpl) Update(ctx context.Context, tx *gorm.DB, item *model.OrderItem) error {
	if err := tx.WithContext(ctx).Save(&item).Error; err != nil {
		return err
	}
	return nil
}

func (repository *OrderItemRepositoryImpl) FindById(ctx context.Context, tx *gorm.DB, itemId uuid.UUID) (*model.OrderItem, error) {
	var item model.OrderItem
	if err := tx.WithContext(ctx).First(&item, "id = ?", itemId).Error; err != nil {
		return nil, err
	}
	return &item, nil
}

func (repository *OrderItemRepositoryImpl) FindByOrderID(ctx context.Context, db *gorm.DB, orderId uuid.UUID) ([]model.OrderItem, error) {
	var items []model.OrderItem
	if err := db.WithContext(ctx).Where("order_id = ?", orderId).Find(&items).Error; err != nil {
		return nil, err
	}
	return items, nil
}
