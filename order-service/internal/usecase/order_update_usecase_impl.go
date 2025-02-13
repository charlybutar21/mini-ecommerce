package usecase

import (
	"context"
	"errors"
	"github.com/google/uuid"
	"gorm.io/gorm"
	"order-service/internal/domain/model"
	"order-service/internal/domain/repository"
	"order-service/internal/usecase/dto/request"
	"time"
)

type orderUpdateUseCaseImpl struct {
	orderRepo     repository.OrderRepository
	orderItemRepo repository.OrderItemRepository
	db            *gorm.DB
}

func NewOrderUpdateUseCase(
	orderRepo repository.OrderRepository,
	orderItemRepo repository.OrderItemRepository,
	db *gorm.DB,
) OrderUpdateUseCase {
	return &orderUpdateUseCaseImpl{
		orderRepo:     orderRepo,
		orderItemRepo: orderItemRepo,
		db:            db,
	}
}

func (useCase *orderUpdateUseCaseImpl) Execute(ctx context.Context, request *request.OrderUpdateRequest) error {
	if request == nil {
		return errors.New("request cannot be nil")
	}

	customerID, err := uuid.Parse(request.CustomerID)
	if err != nil {
		return err
	}

	tx := useCase.db.Begin()
	if tx.Error != nil {
		return tx.Error
	}

	defer func() {
		if r := recover(); r != nil {
			tx.Rollback()
			panic(r)
		} else {
			tx.Rollback()
		}
	}()

	order, err := useCase.orderRepo.FindById(ctx, tx, request.OrderID)
	if err != nil {
		tx.Rollback()
		return err
	}

	order.CustomerID = customerID
	order.OrderDate = request.OrderDate
	order.Status = request.Status
	order.UpdatedAt = time.Now()

	for _, itemRequest := range request.Items {
		if itemRequest.ItemID != "" {
			itemID, err := uuid.Parse(itemRequest.ItemID)
			if err != nil {
				tx.Rollback()
				return err
			}
			existingItem, err := useCase.orderItemRepo.FindById(ctx, tx, itemID)
			if err != nil {
				tx.Rollback()
				return err
			}

			existingItem.ProductCode = itemRequest.ProductCode
			existingItem.ProductName = itemRequest.ProductName
			existingItem.Quantity = itemRequest.Quantity
			existingItem.UnitPrice = itemRequest.UnitPrice
			existingItem.UpdatedAt = time.Now()

			if err := useCase.orderItemRepo.Update(ctx, tx, existingItem); err != nil {
				return err
			}
		} else {
			newItem := model.OrderItem{
				ID:          uuid.New(),
				OrderID:     order.ID,
				ProductCode: itemRequest.ProductCode,
				ProductName: itemRequest.ProductName,
				Quantity:    itemRequest.Quantity,
				UnitPrice:   itemRequest.UnitPrice,
				CreatedAt:   time.Now(),
				UpdatedAt:   time.Now(),
			}

			if _, err := useCase.orderItemRepo.Save(ctx, tx, &newItem); err != nil {
				return err
			}
		}
	}

	if err := useCase.orderRepo.Update(ctx, tx, order); err != nil {
		return err
	}

	return tx.Commit().Error
}
