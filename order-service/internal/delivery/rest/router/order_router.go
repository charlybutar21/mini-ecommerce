package router

import (
	"github.com/julienschmidt/httprouter"
	"order-service/internal/delivery/rest/handler"
	"order-service/internal/usecase"
)

type OrderRouter struct {
	OrderCreateHandler *handler.OrderCreateHandler
	OrderGetHandler    *handler.OrderGetHandler
	OrderUpdateHandler *handler.OrderUpdateHandler
}

func NewOrderRouter(
	orderCreateUseCase usecase.OrderCreateUseCase,
	orderGetUseCase usecase.OrderGetUseCase,
	orderUpdateUseCase usecase.OrderUpdateUseCase,
) *OrderRouter {
	return &OrderRouter{
		OrderCreateHandler: handler.NewOrderCreateHandler(orderCreateUseCase),
		OrderGetHandler:    handler.NewOrderGetHandler(orderGetUseCase),
		OrderUpdateHandler: handler.NewOrderUpdateHandler(orderUpdateUseCase),
	}
}

func RouteOrder(router *OrderRouter) *httprouter.Router {
	r := httprouter.New()
	r.POST("/api/orders", router.OrderCreateHandler.ServeHTTP)
	r.GET("/api/orders/:id", router.OrderGetHandler.ServeHTTP)
	r.PUT("/api/orders/:id", router.OrderUpdateHandler.ServeHTTP)

	return r
}
