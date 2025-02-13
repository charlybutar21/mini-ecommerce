package config

import (
	"log"
	"os"
)

func GetEnv(key string, defaultValue ...string) string {
	value := os.Getenv(key)

	if value == "" && len(defaultValue) > 0 {
		return defaultValue[0]
	}

	if value == "" {
		log.Fatalf("Missing required environment variable: %s", key)
	}

	return value
}
