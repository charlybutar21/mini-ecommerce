myapp/
├── cmd/                   
│   ├── rest/              # Entry point untuk REST API server
│   │   └── main.go
│   ├── grpc/              # Entry point untuk gRPC server
│   │   └── main.go
│   └── worker/            # (Opsional) Entry point untuk background worker (misal, Kafka consumer/producer)
│       └── main.go
├── internal/              
│   ├── config/            # Konfigurasi aplikasi (env, file config, dsb)
│   │   └── config.go
│   ├── domain/            # Layer domain (Entities, Value Objects) sesuai dengan prinsip DDD
│   │   ├── model/         # Model entitas inti aplikasi
│   │   │   └── user.go
│   │   └── repository/    # Interface repository yang mendefinisikan kontrak akses data
│   │       └── user_repository.go
│   ├── usecase/           # Business logic / interactor layer (sesuai dengan Clean Architecture)
│   │   └── user_usecase.go
│   ├── delivery/          # Layer penyampaian (interface adapters) untuk berbagai protokol
│   │   ├── rest/          # HTTP handler untuk REST API
│   │   │   └── user_handler.go
│   │   └── grpc/          # gRPC handler (server implementation)
│   │       └── user_handler.go
│   └── infrastructure/    # Implementasi teknologi eksternal
│       ├── database/      # Contoh: koneksi dan implementasi ke database (misal PostgreSQL)
│       │   └── postgres.go
│       ├── kafka/         # Implementasi Kafka producer dan consumer
│       │   ├── producer.go
│       │   └── consumer.go
│       └── external/      # Client untuk komunikasi dengan service eksternal (misal, REST API client)
│           └── http_client.go
├── pkg/                   
│   ├── logger/            # Library logging yang reusable
│   │   └── logger.go
│   └── utils/             # Fungsi utilitas yang dapat dipakai di berbagai bagian aplikasi
│       └── time.go
├── api/                   # Definisi API
│   ├── grpc/              # File Protobuf untuk gRPC
│   │   └── user.proto
│   └── openapi/           # Spesifikasi OpenAPI untuk REST API
│       └── user.yaml
├── deployments/           # File deployment (Docker, Kubernetes, dsb)
│   ├── docker/
│   │   └── Dockerfile
│   └── k8s/
│       └── deployment.yaml
├── scripts/               # Skrip untuk automasi (build, migration, dsb)
│   └── migrate.sh
├── docs/                  # Dokumentasi proyek (arsitektur, panduan, dsb)
│   └── architecture.md
├── go.mod
└── go.sum



cmd/
Berisi entry point aplikasi. Pisahkan masing-masing binary sesuai fungsinya: REST API, gRPC, dan worker (misalnya untuk Kafka).

internal/
Tempat implementasi logika inti aplikasi yang tidak boleh diakses secara langsung dari luar.

config/
Mengelola konfigurasi aplikasi, seperti membaca variabel environment atau file konfigurasi.

domain/
Berisi entitas, value objects, dan interface repository yang mendefinisikan kontrak data. Ini adalah inti dari business logic yang bebas dari teknologi atau framework.

usecase/
Layer interactor yang mengimplementasikan business logic sesuai dengan aturan aplikasi. Di sinilah prinsip SOLID (misal: Dependency Inversion) diterapkan dengan mendefinisikan kontrak antar layer.

delivery/
Merupakan layer adapter yang menangani interaksi dengan dunia luar. Di sini disediakan implementasi untuk REST API (HTTP handler) dan gRPC. Dengan memisahkan ini, kita dapat dengan mudah mengubah protokol komunikasi tanpa mengganggu business logic.

infrastructure/
Implementasi teknologi eksternal, seperti:

database/: koneksi dan query ke database.
kafka/: producer dan consumer untuk Kafka.
external/: client untuk mengakses service lain (misal, melalui REST API).
pkg/
Berisi library atau paket yang bersifat generik dan bisa digunakan ulang di banyak tempat.
Contohnya, modul untuk logging atau utilitas umum.

api/
Tempat mendefinisikan kontrak API, seperti file Protobuf untuk gRPC dan spesifikasi OpenAPI untuk REST API.

deployments/
File-file dan skrip yang berkaitan dengan deployment, misalnya Dockerfile dan manifest Kubernetes.

scripts/
Skrip automasi untuk kebutuhan build, migrasi database, dsb.

docs/
Dokumentasi proyek, termasuk arsitektur dan panduan implementasi.