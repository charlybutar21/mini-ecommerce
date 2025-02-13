package usecase

import (
	"context"
	"order-service/internal/usecase/dto/request"
)

type OrderUpdateUseCase interface {
	Execute(ctx context.Context, request *request.OrderUpdateRequest) error
}
