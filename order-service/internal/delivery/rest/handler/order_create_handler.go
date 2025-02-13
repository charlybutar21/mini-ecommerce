package handler

import (
	"github.com/jinzhu/copier"
	"github.com/julienschmidt/httprouter"
	"net/http"
	restRequest "order-service/internal/delivery/rest/dto/request"
	"order-service/internal/delivery/rest/utils"
	"order-service/internal/usecase"
	"order-service/internal/usecase/dto/request"
)

type OrderCreateHandler struct {
	orderCreateUseCase usecase.OrderCreateUseCase
}

func NewOrderCreateHandler(orderCreateUseCase usecase.OrderCreateUseCase) *OrderCreateHandler {
	return &OrderCreateHandler{orderCreateUseCase: orderCreateUseCase}
}

func (handler *OrderCreateHandler) ServeHTTP(w http.ResponseWriter, r *http.Request, _ httprouter.Params) {
	defer utils.CloseRequestBody(r)

	var restReq restRequest.OrderCreateRestRequest
	if err := utils.DecodeRequestBody(r, &restReq); err != nil {
		utils.HttpError(w, http.StatusBadRequest, "Invalid request payload", err)
		return
	}

	var req request.OrderCreateRequest
	if err := copier.Copy(&req, &restReq); err != nil {
		utils.HttpError(w, http.StatusInternalServerError, "Failed to copy request", err)
		return
	}

	orderID, err := handler.orderCreateUseCase.Execute(r.Context(), &req)
	if err != nil {
		utils.HttpError(w, http.StatusInternalServerError, "Failed to create order", err)
		return
	}

	utils.WriteJSONResponse(w, http.StatusCreated, orderID)
}
