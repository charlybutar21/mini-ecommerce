package handler

import (
	"github.com/google/uuid"
	"github.com/jinzhu/copier"
	"github.com/julienschmidt/httprouter"
	"log"
	"net/http"
	restResponse "order-service/internal/delivery/rest/dto/response"
	"order-service/internal/delivery/rest/utils"
	"order-service/internal/usecase"
)

type OrderGetHandler struct {
	orderGetUseCase usecase.OrderGetUseCase
}

func NewOrderGetHandler(orderGetUseCase usecase.OrderGetUseCase) *OrderGetHandler {
	return &OrderGetHandler{orderGetUseCase: orderGetUseCase}
}

func (handler *OrderGetHandler) ServeHTTP(w http.ResponseWriter, r *http.Request, params httprouter.Params) {
	defer utils.CloseRequestBody(r)

	id, err := uuid.Parse(params.ByName("id"))
	if err != nil {
		utils.HttpError(w, http.StatusBadRequest, "Invalid order ID format", err)
		return
	}

	orderResponse, err := handler.orderGetUseCase.Execute(r.Context(), id)
	if err != nil {
		http.Error(w, "Order not found", http.StatusNotFound)
		log.Printf("[ERROR] Order not found: %v", err)
		return
	}

	var orderRestResponse restResponse.OrderGetRestResponse
	if err := copier.Copy(&orderRestResponse, &orderResponse); err != nil {
		utils.HttpError(w, http.StatusInternalServerError, "Failed to copy request", err)
		return
	}

	utils.WriteJSONResponse(w, http.StatusOK, orderRestResponse)
}
