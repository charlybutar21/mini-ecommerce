package usecase

import (
	"context"
	"errors"
	"github.com/google/uuid"
	"gorm.io/gorm"
	"order-service/internal/domain/model"
	"order-service/internal/domain/repository"
	"order-service/internal/usecase/dto/request"
)

type orderCreateUseCaseImpl struct {
	orderRepo     repository.OrderRepository
	orderItemRepo repository.OrderItemRepository
	db            *gorm.DB
}

func NewOrderCreateUseCase(
	orderRepo repository.OrderRepository,
	orderItemRepo repository.OrderItemRepository,
	db *gorm.DB,
) OrderCreateUseCase {
	return &orderCreateUseCaseImpl{
		orderRepo:     orderRepo,
		orderItemRepo: orderItemRepo,
		db:            db,
	}
}

func (useCase *orderCreateUseCaseImpl) Execute(ctx context.Context, request *request.OrderCreateRequest) (*uuid.UUID, error) {
	if request == nil {
		return nil, errors.New("request cannot be nil")
	}

	tx := useCase.db.Begin()
	if tx.Error != nil {
		return nil, tx.Error
	}

	defer func() {
		if r := recover(); r != nil {
			tx.Rollback()
			panic(r)
		} else {
			tx.Rollback()
		}
	}()

	customerID, err := uuid.Parse(request.CustomerID)
	if err != nil {
		tx.Rollback()
		return nil, err
	}

	order := &model.Order{
		ID:         uuid.New(),
		CustomerID: customerID,
		OrderDate:  request.OrderDate,
		Status:     request.Status,
	}

	savedOrder, err := useCase.orderRepo.Save(ctx, tx, order)
	if err != nil {
		tx.Rollback()
		return nil, err
	}

	for _, itemRequest := range request.Items {
		orderItem := &model.OrderItem{
			ID:          uuid.New(),
			OrderID:     savedOrder.ID,
			ProductCode: itemRequest.ProductCode,
			ProductName: itemRequest.ProductName,
			Quantity:    itemRequest.Quantity,
			UnitPrice:   itemRequest.UnitPrice,
		}

		if _, err := useCase.orderItemRepo.Save(ctx, tx, orderItem); err != nil {
			return nil, err
		}
	}

	if err = tx.Commit().Error; err != nil {
		return nil, err
	}

	return &savedOrder.ID, nil
}
