package handler

import (
	"github.com/google/uuid"
	"github.com/jinzhu/copier"
	"github.com/julienschmidt/httprouter"
	"net/http"
	restRequest "order-service/internal/delivery/rest/dto/request"
	"order-service/internal/delivery/rest/utils"
	"order-service/internal/usecase"
	"order-service/internal/usecase/dto/request"
)

type OrderUpdateHandler struct {
	orderUpdateUseCase usecase.OrderUpdateUseCase
}

func NewOrderUpdateHandler(orderUpdateUseCase usecase.OrderUpdateUseCase) *OrderUpdateHandler {
	return &OrderUpdateHandler{orderUpdateUseCase: orderUpdateUseCase}
}

func (handler *OrderUpdateHandler) ServeHTTP(w http.ResponseWriter, r *http.Request, params httprouter.Params) {
	defer utils.CloseRequestBody(r)

	orderID, err := uuid.Parse(params.ByName("id"))
	if err != nil {
		utils.HttpError(w, http.StatusBadRequest, "Invalid order ID format", err)
		return
	}

	var restReq restRequest.OrderUpdateRestRequest
	if err := utils.DecodeRequestBody(r, &restReq); err != nil {
		utils.HttpError(w, http.StatusBadRequest, "Invalid request payload", err)
		return
	}

	var req request.OrderUpdateRequest
	if err := copier.Copy(&req, &restReq); err != nil {
		utils.HttpError(w, http.StatusInternalServerError, "Failed to copy request", err)
		return
	}
	req.OrderID = orderID

	err = handler.orderUpdateUseCase.Execute(r.Context(), &req)
	if err != nil {
		utils.HttpError(w, http.StatusInternalServerError, "Failed to update order", err)
		return
	}

	utils.WriteJSONResponse(w, http.StatusCreated, nil)
}
