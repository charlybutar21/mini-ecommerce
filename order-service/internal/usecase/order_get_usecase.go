package usecase

import (
	"context"
	"github.com/google/uuid"
	"order-service/internal/usecase/dto/response"
)

type OrderGetUseCase interface {
	Execute(ctx context.Context, id uuid.UUID) (*response.OrderGetResponse, error)
}
