package usecase

import (
	"context"
	"github.com/google/uuid"
	"gorm.io/gorm"
	"order-service/internal/domain/model"
	"order-service/internal/domain/repository"
	"order-service/internal/usecase/dto/response"
)

type orderGetUseCaseImpl struct {
	orderRepo     repository.OrderRepository
	orderItemRepo repository.OrderItemRepository
	db            *gorm.DB
}

func NewOrderGetUseCase(
	orderRepo repository.OrderRepository,
	orderItemRepo repository.OrderItemRepository,
	db *gorm.DB,
) OrderGetUseCase {
	return &orderGetUseCaseImpl{
		orderRepo:     orderRepo,
		orderItemRepo: orderItemRepo,
		db:            db,
	}
}

func (useCase *orderGetUseCaseImpl) Execute(ctx context.Context, id uuid.UUID) (*response.OrderGetResponse, error) {
	order, err := useCase.orderRepo.FindById(ctx, useCase.db, id)
	if err != nil {
		return nil, err
	}

	items, err := useCase.orderItemRepo.FindByOrderID(ctx, useCase.db, order.ID)
	if err != nil {
		return nil, err
	}

	return buildResponse(order, items), nil
}

func buildResponse(order *model.Order, items []model.OrderItem) *response.OrderGetResponse {
	itemResponses := make([]response.OrderItemResponse, 0, len(items))
	for _, item := range items {
		itemResponses = append(itemResponses, response.OrderItemResponse{
			ID:          item.ID,
			ProductCode: item.ProductCode,
			ProductName: item.ProductName,
			Quantity:    item.Quantity,
			UnitPrice:   item.UnitPrice,
		})
	}

	return &response.OrderGetResponse{
		ID:         order.ID,
		CustomerID: order.CustomerID,
		OrderDate:  order.OrderDate,
		Status:     order.Status,
		CreatedAt:  order.CreatedAt,
		UpdatedAt:  order.UpdatedAt,
		Items:      itemResponses,
	}
}
