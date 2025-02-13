package utils

import (
	"encoding/json"
	"log"
	"net/http"
	restResponse "order-service/internal/delivery/rest/dto/response"
)

func CloseRequestBody(r *http.Request) {
	if err := r.Body.Close(); err != nil {
		log.Printf("[ERROR] Failed to close request body: %v", err)
	}
}

func DecodeRequestBody(r *http.Request, v interface{}) error {
	return json.NewDecoder(r.Body).Decode(v)
}

func HttpError(w http.ResponseWriter, statusCode int, message string, err error) {
	log.Printf("[ERROR] %s: %v", message, err)
	http.Error(w, message, statusCode)
}

func WriteJSONResponse(w http.ResponseWriter, statusCode int, data interface{}) {
	response := restResponse.WebResponse{
		Code:   statusCode,
		Status: http.StatusText(statusCode),
		Data:   data,
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(statusCode)

	if err := json.NewEncoder(w).Encode(response); err != nil {
		log.Printf("[ERROR] Failed to encode response: %v", err)
	}
}
