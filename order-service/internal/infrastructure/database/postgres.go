package database

import (
	"fmt"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"log"
	"order-service/internal/config"
)

type ConnectionConfig struct {
	Host     string
	User     string
	Password string
	Name     string
	Port     string
	SSLMode  string
	TimeZone string
}

func LoadConnectionConfig() *ConnectionConfig {
	return &ConnectionConfig{
		Host:     config.GetEnv("DB_HOST"),
		User:     config.GetEnv("DB_USER"),
		Password: config.GetEnv("DB_PASSWORD"),
		Name:     config.GetEnv("DB_NAME"),
		Port:     config.GetEnv("DB_PORT"),
		SSLMode:  config.GetEnv("DB_SSLMODE"),
		TimeZone: config.GetEnv("DB_TIMEZONE"),
	}
}

func InitDB() (*gorm.DB, error) {
	connectionConfig := LoadConnectionConfig()

	dsn := fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=%s TimeZone=%s",
		connectionConfig.Host, connectionConfig.User, connectionConfig.Password, connectionConfig.Name, connectionConfig.Port, connectionConfig.SSLMode, connectionConfig.TimeZone)

	connection, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})
	if err != nil {
		log.Fatalf("Failed to connect to the database: %v", err)
		return nil, err
	}

	fmt.Println("Successfully connected to the database!")

	return connection, nil
}
