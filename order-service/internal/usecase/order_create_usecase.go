package usecase

import (
	"context"
	"github.com/google/uuid"
	"order-service/internal/usecase/dto/request"
)

type OrderCreateUseCase interface {
	Execute(ctx context.Context, request *request.OrderCreateRequest) (*uuid.UUID, error)
}
